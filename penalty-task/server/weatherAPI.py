import configparser
import requests

config = configparser.ConfigParser()
config.read("config.ini")

URL = config['api']['url']
API_KEY = config['api']['apiKey']


def getWeather(data):
    payload = {'appid': API_KEY}
    if data['city']:
        payload['q'] = data['city']
        result = requests.get(URL, payload)
        return result.text
