package com.labs.neko.nekofxmusicplayer.Models;

public class Music{

    private String title, artist, path, cover, album;
    private short track;

    public Music(String title, String artist, String path, String cover, String album, short track) {
        this.title = title;
        this.artist = artist;
        this.path = path;
        this.cover = cover;
        this.album = album;
        this.track = track;
    }

    public String getTitle() {
        return title;
    }


    public String getArtist() {
        return artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCover() {
        return cover;
    }

    public String getAlbum() {
        return album;
    }

    public short getTrack() {
        return track;
    }

    public void setTrack(short track) {
        this.track = track;
    }
}

