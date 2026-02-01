package pl.kansas.go.infrastructure.persistence.entity;

import jakarta.persistence.*;
import pl.kansas.go.domain.model.Stone;
import pl.kansas.go.domain.model.MoveType;

import java.util.UUID;

/**
 * Database Spring Move entity.
 */
@Entity
@Table(name = "moves")
public class MoveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID gameId;
    private int sequenceNumber;
    private int x;
    private int y;

    @Enumerated(EnumType.STRING)
    private Stone stone;

    @Enumerated(EnumType.STRING)
    private MoveType type;

    public MoveEntity() {
    }

    public MoveEntity(UUID gameId, int sequenceNumber, int x, int y, Stone stone, MoveType type) {
        this.gameId = gameId;
        this.sequenceNumber = sequenceNumber;
        this.x = x;
        this.y = y;
        this.stone = stone;
        this.type = type;
    }

    public UUID getGameId() {
        return gameId;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Stone getStone() {
        return stone;
    }

    public MoveType getType() {
        return type;
    }
}
