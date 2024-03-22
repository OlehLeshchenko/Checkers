package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Save;
import sk.tuke.gamestudio.game.checkers.core.Field;

import java.util.List;

public interface SaveService {
    /**
     * adds a new players to storage
     * @param save save object to be added
     */
    void addSave(Save save);

    /**
     * Get players from the storage for the game named <code>game</code>
     * @param game name of the game
     * @return list of the all saves
     */
    List<Save> getSaves(String game);

    /**
     * deletes all saves in the storage (for all games)
     */
    void reset();

    /**
     * Serializes the given game field object into a byte array representation.
     * Serialization is the process of converting an object into a byte stream,
     * which can be stored or transmitted and later reconstructed to its original form.
     *
     * @param field the game field object to be serialized
     * @return a byte array representing the serialized form of the game field
     */
    byte[] serialize(Field field);

    /**
     * Deserializes the given byte array representation into a game field object.
     * Deserialization is the process of reconstructing an object from its serialized byte form.
     *
     * @param bytes the byte array representing the serialized form of the game field
     * @return the reconstructed game field object
     */
    Field deserialize(byte[] bytes);
}
