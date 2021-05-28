import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Main {
    static public void main(String[] args) throws Exception {
        Map<ClientTCP, String> studentsVariants = new HashMap<>();
        String serverName = args[0];
        int port = Integer.parseInt(args[1]);

        Server server = new Server(port);
        server.listen();

        Thread.sleep(2000);

        ClientTCP[] clientList = new ClientTCP[]
                {
                        new ClientTCP(0, serverName, port),
                        new ClientTCP(10, serverName, port),
                        new ClientTCP(3, serverName, port),

                };
        for (ClientTCP client : clientList){
            try {
                // Подключаем наших клиентов к серверу
                client.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            // Делаем запрос, чтоб получить все варианты
            JSONObject availableRequest = clientList[0].request(createRequest("{endpoint : availableVariants}", "{student : " + "Student(" + clientList[0].clientId + ")}"));
            JSONObject requestBody = parse(availableRequest, "body");
            JSONArray variantsList = requestBody.getJSONArray("variants");
            int variantIndex = (int) (Math.random() * (variantsList.length()));
            int variantIndex2 = (int) (Math.random() * (variantsList.length()));
            // Делаем запрос, чтоб client[0] получил вариант
            clientList[0].request(createRequest("{endpoint : getVariant}", "{variant : " + variantsList.get(variantIndex) + "," + "student : " + "Student(" + clientList[0].clientId + ")}"));
            // Делаем запрос, чтоб client[10] получил вариант
            clientList[1].request(createRequest("{endpoint : getVariant}", "{variant : " + variantsList.get(variantIndex) + "," + "student : " + "Student(" + clientList[1].clientId + ")}"));
            // Делаем запрос, чтоб client[0] получил вариант
            clientList[0].request(createRequest("{endpoint : getVariant}", "{variant : " + variantsList.get(variantIndex2) + "," + "student : " + "Student(" + clientList[0].clientId + ")}"));
            // Делаем запрос, чтоб client[3] получил вариант
            clientList[2].request(createRequest("{endpoint : getVariant}", "{variant : " + variantsList.get(variantIndex) + "," + "student : " + "Student(" + clientList[2].clientId + ")}"));
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        for (ClientTCP client : clientList){
            try {
                // Отключаем клиентов от сервера
                client.closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    static JSONObject createRequest(String headers, String data){
        JSONObject requestHeaders = new JSONObject(headers);
        JSONObject requestBody = new JSONObject(data);
        JSONObject request = new JSONObject();
        request.put("headers", requestHeaders);
        request.put("body", requestBody);
        return request;
    }

    static JSONObject parse(JSONObject request, String tag){
        return (JSONObject) request.get(tag);
    }
}
