package com.codevitaliy.partyupp.gallery;

import java.util.ArrayList;
import java.util.Date;

public class GalleryGameModel {
    private int gameId;
    private Integer startPhotoId; // Can be null
    private Integer endPhotoId; // Can be null
    private ArrayList<String> players;
    private Date startTime;
    private Date endTime; // Can be null

    public GalleryGameModel(int gameId, Integer startPhotoId, Integer endPhotoId, ArrayList<String> players, Date startTime, Date endTime) {
        this.gameId = gameId;
        this.startPhotoId = startPhotoId;
        this.endPhotoId = endPhotoId;
        this.players = players;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and setters
    // Getter method for gamePhoto
    public Integer getStartPhotoId() { return startPhotoId; }
    public Integer getEndPhotoId() { return endPhotoId; }

    public ArrayList<String> getPlayers() {
        return players;
    }
    public Date getStartTime() { return startTime; }
    public Date getEndTime() { return endTime; }

    public int getGameId() {
        return gameId;
    }
}

