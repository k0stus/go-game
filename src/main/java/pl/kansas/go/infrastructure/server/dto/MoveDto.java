package pl.kansas.go.infrastructure.server.dto;

import pl.kansas.go.domain.model.Move;
import pl.kansas.go.domain.model.MoveType;
import pl.kansas.go.domain.model.Stone;

public class MoveDto {
    public int x;
    public int y;
    public Stone stone;
    public MoveType moveType;

    public MoveDto() {
    }

    public MoveDto(Move move) {
        this.x = move.getX();
        this.y = move.getY();
        this.stone = move.getStone();
        this.moveType = move.getMoveType();
    }

    public Move toDomain() {
        if (moveType == MoveType.PASS) {
            return new Move(MoveType.PASS, stone);
        } else if (moveType == MoveType.SURRENDER) {
            return new Move(MoveType.SURRENDER, stone);
        }
        return new Move(x, y, stone);
    }
}
