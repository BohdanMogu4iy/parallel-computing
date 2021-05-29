import json
import select
import socket
import configparser
from weatherAPI import getWeather

config = configparser.ConfigParser()
config.read("config.ini")
SERVER_ADDRESS = (config['server']['hostname'], int(config['server']['port']))
MAX_CONNECTIONS = int(config['server']['maxConnections'])

INPUT_STREAMS = list()
OUTPUT_STREAMS = list()
RESPONSES = {}


def getServerSocket():
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    if config['server']['setBlocking']:
        server.setblocking(0)
    server.bind(SERVER_ADDRESS)
    server.listen(MAX_CONNECTIONS)
    print(f"Server is running on {SERVER_ADDRESS}")
    return server


def handleInputStreams(clients, server):
    for resource in clients:
        if resource is server:
            connection, client_address = resource.accept()
            connection.setblocking(0)
            INPUT_STREAMS.append(connection)
            print(f"New connection from {client_address}")
        else:
            requestData = ''
            try:
                requestData = resource.recv(1024)
            except ConnectionResetError as error:
                print(f"Error : {error}")
                pass
            if requestData:
                RESPONSES[resource] = bytes(bytearray(requestData)[4:])
                print(f"Server got data:\n{RESPONSES[resource]} from {resource.getpeername()}")
                if resource not in OUTPUT_STREAMS:
                    OUTPUT_STREAMS.append(resource)
            else:
                clearResource(resource)


def clearResource(resource):
    if resource in OUTPUT_STREAMS:
        OUTPUT_STREAMS.remove(resource)
    if resource in INPUT_STREAMS:
        INPUT_STREAMS.remove(resource)
    resource.close()


def handleOutputStreams(clients):
    for resource in clients:
        try:
            if RESPONSES[resource]:
                data = json.loads(RESPONSES[resource])
                RESPONSES[resource] = None
                apiData = {}
                location = data['location']
                if location:
                    apiData['city'] = location
                res = getWeather(apiData)
                responseData = bytearray(res, encoding="UTF-8")
                responseData[0:0] = len(responseData).to_bytes(length=4, byteorder='big')
                resource.send(bytes(responseData))
                print(f"Server sent data to {resource.getpeername()}")
        except OSError:
            clearResource(resource)


if __name__ == '__main__':
    server_socket = getServerSocket()
    INPUT_STREAMS.append(server_socket)
    while INPUT_STREAMS:
        r, w, e = select.select(INPUT_STREAMS, OUTPUT_STREAMS, INPUT_STREAMS)
        handleInputStreams(r, server_socket)
        handleOutputStreams(w)
