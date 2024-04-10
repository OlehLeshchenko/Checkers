package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Save;
import sk.tuke.gamestudio.game.checkers.core.Field;

import java.io.*;
import java.util.List;

public abstract class SaveService {
    /**
     * adds a new players to storage
     *
     * @param save save object to be added
     */
    public abstract void addSave(Save save);

    /**
     * Get players from the storage for the game named <code>game</code> and player name
     *
     * @param game   name of the game
     * @param player name of the player
     * @return list of the all player saves
     */
    public abstract List<Save> getSaves(String game, String player);

    /**
     * deletes all saves in the storage (for all games)
     */
    public abstract void reset();

    /**
     * Serializes the given game field object into a byte array representation.
     * Serialization is the process of converting an object into a byte stream,
     * which can be stored or transmitted and later reconstructed to its original form.
     *
     * @param field the game field object to be serialized
     * @return a byte array representing the serialized form of the game field
     */
    public byte[] serialize(Field field) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(field);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new GameStudioException(e);
        }
    }

    /**
     * Deserializes the given byte array representation into a game field object.
     * Deserialization is the process of reconstructing an object from its serialized byte form.
     *
     * @param bytes the byte array representing the serialized form of the game field
     * @return the reconstructed game field object
     */
    public Field deserialize(byte[] bytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Field) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new GameStudioException(e);
        }
    }
}
