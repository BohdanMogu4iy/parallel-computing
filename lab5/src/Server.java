import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Server implements Runnable
{
    // Этот порт будет прослушиваться сервером
    private final int port;

    // Выделение памяти под буфер
    private final ByteBuffer buffer = ByteBuffer.allocate( 16384 );

    // Инициализация вариантов лабораторных
    private final LabVariants<String> variants;

    public Server( int port) {
        this.port = port;
        this.variants = new LabVariants<String>(Arrays.asList("first", "second", "third", "fourth", "fifth"), 1);
    }

    public void listen(){
        new Thread( this ).start();
    }

    public void run() {
        try {
            // Я создаю ServerSocketChannel
            ServerSocketChannel ssc = ServerSocketChannel.open();

            // канал должен быть non-blocking
            ssc.configureBlocking( false );

            // Подключаем ServerSocket к каналу
            ServerSocket serverSocket = ssc.socket();
            InetSocketAddress isa = new InetSocketAddress( port );
            serverSocket.bind(isa);

            // Создаем новый обьект Селектор
            Selector selector = Selector.open();

            // Регистрируем селектор для ServerSocketChannel
            ssc.register( selector, SelectionKey.OP_ACCEPT );
            System.out.println( "Server is running on port : " + port );

            while (true) {
                // Если есть активности
                int num = selector.select();

                // Если нету активностей продолжаем
                if (num == 0) {
                    continue;
                }

                // Нужно получить ключи активностей
                Set<SelectionKey> keys = selector.selectedKeys();
                for (SelectionKey key : keys) {
                    // Проверка типа активности
                    if ((key.readyOps() & SelectionKey.OP_ACCEPT) ==
                            SelectionKey.OP_ACCEPT) {

                        // Прослушиваем InputStream
                        Socket socket = serverSocket.accept();
                        System.out.println("Server got connection from : " + socket.getRemoteSocketAddress());

                        // Проверка на non-blocking
                        SocketChannel sc = socket.getChannel();
                        sc.configureBlocking(false);

                        // регистрируем селектор на чтение
                        sc.register(selector, SelectionKey.OP_READ);
                    } else if ((key.readyOps() & SelectionKey.OP_READ) ==
                            SelectionKey.OP_READ) {

                        SocketChannel sc = null;

                        try {
                            // Обрабатываем входные данные
                            sc = (SocketChannel) key.channel();
                            boolean status = processInput(sc);

                            // Если соединение закрыто, то закрываем сокет
                            if (!status) {
                                key.cancel();

                                Socket s = null;
                                try {
                                    s = sc.socket();
                                    System.out.println("Server closed socket from : " + s.getRemoteSocketAddress() );
                                    s.close();
                                } catch (IOException ie) {
                                    System.err.println("Server Error closing socket " + s + " : " + ie);
                                }
                            }
                        } catch (IOException ie) {

                            key.cancel();

                            try {
                                System.out.println("Server closed socket channel from : " + sc.getRemoteAddress());
                                sc.close();
                            } catch (IOException ie2) {
                                System.err.println("Server Error I/O : " + ie2);
                            }
                        }
                    }
                }

                // Удаляем ключи
                keys.clear();
            }
        } catch( IOException ie ) {
            System.err.println("Server Error I/O : " + ie);
            ie.printStackTrace();
        }
    }

    // Обработка входных данных
    private boolean processInput( SocketChannel sc ) throws IOException {
        // Записываем данные в буфер (которые пришли из InputStream)
        buffer.clear();
        sc.read( buffer );
        buffer.flip();

        // Если дата пустая то false
        if (buffer.limit() == 0){
            return false;
        }

        // Считываем данные записаные в буфер
        byte[] data = buffer.array();

        JSONObject requestJson = new JSONObject(new String(data, StandardCharsets.UTF_8));
        JSONObject requestHeaders = (JSONObject) requestJson.get("headers");
        JSONObject requestBody = (JSONObject) requestJson.get("body");
        String endpoint = (String) requestHeaders.get("endpoint");

        System.out.println("Server got request on endpoint : " + "\nwith data :\n" + requestJson.toString(2));

        JSONObject responseJson = new JSONObject();
        JSONObject responseBody = new JSONObject();
        JSONObject responseHeaders = new JSONObject();

        // Обрабатываем варианты доступных запросов
        switch (endpoint) {
            case "availableVariants" -> {
                responseHeaders.put("status", 200);
                responseBody.put("variants", variants.getAvailableVariants());
                responseBody.put("info", "You have got all available variants");
            }
            case "getVariant" -> {
                responseHeaders.put("status", 200);
                String student = (String) requestBody.get("student");
                String variant = (String) requestBody.get("variant");
                int var = variants.getVariant(student, variant);
                switch (var){
                    case 0 -> {
                        responseBody.put("info", "You have got :" + variant);
                    }
                    case 1 -> {
                        responseBody.put("info", "There is no such variant");
                    }
                    case 2 -> {
                        responseBody.put("info", "There is no more available variants");
                    }
                    case 3 -> {
                        responseBody.put("info", "This variant is already chosen");
                    }
                    case 4 -> {
                        responseBody.put("info", "You can have no more variant");
                    }
                }
            }
        }

        responseJson.put("headers", responseHeaders);
        responseJson.put("body", responseBody);

        // Записываем данные в буфер и после отправляем буфер через outputStream
        writeBuffer(sc, responseJson.toString().getBytes(StandardCharsets.UTF_8));

        System.out.println( "Server processed request from : " + sc.getRemoteAddress());

        return true;
    }

    private void writeBuffer(SocketChannel sc, byte[] data) throws IOException{
        buffer.clear();
        buffer.put(data);
        buffer.flip();

        sc.write(buffer);
    }

    static public void main(String[] args) throws Exception {
        int port = Integer.parseInt( args[0] );
        // Запускаем сервер на порту port
        Server server = new Server( port );
        server.listen();
    }
}
