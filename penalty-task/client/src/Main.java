import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String [] args) {
        String serverName = args[0];
        int port = Integer.parseInt(args[1]);

        ClientTCP client = new ClientTCP(1, serverName, port);
        try {
            client.connect();
            JSONObject weatherLondon = client.request(new JSONObject("{location: 'London'}"));
            if (weatherLondon != null) {
                System.out.println("weather in " + "London :" + "\n" + weatherLondon.toString(2));
            }
            JSONObject weatherParis = client.request(new JSONObject("{location: 'Paris'}"));
            if (weatherParis != null) {
                System.out.println("weather in " + "London :" + "\n" + weatherParis.toString(2));
            }
            client.closeConnection();
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}