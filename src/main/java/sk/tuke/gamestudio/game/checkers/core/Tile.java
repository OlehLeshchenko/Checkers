package sk.tuke.gamestudio.game.checkers.core;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;

import java.io.Serializable;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Commoner.class, name = "Commoner"),
        @JsonSubTypes.Type(value = King.class, name = "King")
})
public class Tile implements Serializable {
    private final int posX;
    private final int posY;

    public Tile(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

}
