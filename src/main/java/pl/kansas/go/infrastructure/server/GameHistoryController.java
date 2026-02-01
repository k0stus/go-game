package pl.kansas.go.infrastructure.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.kansas.go.application.GameService;
import pl.kansas.go.infrastructure.server.dto.MoveDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST controller for retrieving game history and list of games.
 */
@RestController
public class GameHistoryController {

    private final GameService gameService;

    public GameHistoryController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/api/games")
    public List<UUID> getAllExperiments() {
        return gameService.getAllGameIds();
    }

    @GetMapping("/api/games/{gameId}/moves")
    public List<MoveDto> getGameMoves(@PathVariable UUID gameId) {
        return gameService.getGameHistory(gameId).stream()
                .map(MoveDto::new)
                .collect(Collectors.toList());
    }
}
