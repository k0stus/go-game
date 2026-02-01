package pl.kansas.go.client.bot;

import pl.kansas.go.client.ClientApp;

/**
 * Punkt wejścia aplikacji bota Go.
 */
public class BotApplication {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Go Bot...");

        ClientApp client = new ClientApp();
        client.connect("localhost", 5123);

        BotGameGateway gateway = new BotGameGateway(client);
        new BotController(gateway);

        System.out.println("Go Bot connected.");
    }
}
