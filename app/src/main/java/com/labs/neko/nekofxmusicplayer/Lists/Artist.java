package com.labs.neko.nekofxmusicplayer.Lists;

import com.labs.neko.nekofxmusicplayer.Models.Music;

import java.util.ArrayList;

public class Artist {

    private String artist;
    private ArrayList<Music> musicList;

    public Artist(String artist){
        this.artist = artist;
        this.musicList = new ArrayList<>();
    }

    public String getArtist() {
        return artist;
    }

    public ArrayList<Music> getMusicList() {
        return musicList;
    }

}
