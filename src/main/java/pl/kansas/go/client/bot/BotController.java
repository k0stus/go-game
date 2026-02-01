package pl.kansas.go.client.bot;

import pl.kansas.go.domain.model.Move;

/**
 * Główna pętla decyzyjna bota.
 */
public class BotController {

    private final BotGameGateway gateway;
    private final RandomMoveStrategy strategy = new RandomMoveStrategy();

    public BotController(BotGameGateway gateway) {
        this.gateway = gateway;

        new Thread(this::loop).start();
    }

    private void loop() {
        while (true) {
            try {
                Thread.sleep(300); // żeby nie spamować CPU

                if (gateway.isFinished()) {
                    System.out.println("[BOT] Game finished.");
                    return;
                }

                if (gateway.isMyTurn() && gateway.getBoard() != null) {
                    Move move = strategy.findMove(
                            gateway.getBoard(),
                            gateway.getMyStone()
                    );
                    gateway.sendMove(move);
                }

            } catch (Exception e) {
                System.out.println("[BOT] Error: " + e.getMessage());
            }
        }
    }
}
