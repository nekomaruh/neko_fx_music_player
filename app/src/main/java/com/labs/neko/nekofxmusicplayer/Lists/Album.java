package com.labs.neko.nekofxmusicplayer.Lists;

import com.labs.neko.nekofxmusicplayer.Models.Music;

import java.util.ArrayList;

public class Album {

    private String title;
    private ArrayList<Music> musicList;

    public Album(String title){
        this.title = title;
        this.musicList = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Music> getMusicList() {
        return musicList;
    }

}
