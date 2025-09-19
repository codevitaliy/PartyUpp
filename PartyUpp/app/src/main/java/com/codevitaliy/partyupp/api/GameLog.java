package com.codevitaliy.partyupp.api;

import java.util.Date;
import java.util.List;

public class GameLog {
    private Integer gameId;
    private List<String> players;
    private Date startTime;
    private Date endTime;
    private Integer startPhotoId;
    private Integer endPhotoId;

    public Integer getGameId() { return gameId; }

    public Integer getEndPhotoId() {
        return endPhotoId;
    }

    public Integer getStartPhotoId() {
        return startPhotoId;
    }

    public Date getEndTime() {
        return endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public List<String> getPlayers() {
        return players;
    }
}
