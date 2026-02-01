package pl.kansas.go.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kansas.go.application.GameFactory;
import pl.kansas.go.application.GameService;
import pl.kansas.go.domain.repository.GameRepository;

@Configuration
public class BeanConfiguration {

    @Bean
    public GameFactory gameFactory() {
        return new GameFactory();
    }

    @Bean
    public GameService gameService(GameFactory gameFactory, GameRepository gameRepository) {
        return new GameService(gameFactory, gameRepository);
    }
}
