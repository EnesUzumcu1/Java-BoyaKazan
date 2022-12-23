package com.enesuzumcu.boyakazan.model;

public class Player {
    public static Player player1;
    public static Player player2;

    public String name;
    public Integer color;

    public Player(String name, Integer color) {
        this.name = name;
        this.color = color;
    }
}
