package com.codevitaliy.partyupp.api;

import java.io.Serializable;
import java.util.Map;

public class Challenge implements Serializable {

    public enum Category {
        Simple,
        Timer
    }

    public int id;
    public Category category;
    public Map<String, String> data;

}
