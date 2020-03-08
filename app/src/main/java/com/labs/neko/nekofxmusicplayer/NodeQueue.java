package com.labs.neko.nekofxmusicplayer;

import com.labs.neko.nekofxmusicplayer.Models.Music;

public class NodeQueue {
    private Music music;
    private NodeQueue next;

    public NodeQueue(Music music){
        this.music = music;
        this.next = null;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public NodeQueue getNext() {
        return next;
    }

    public void setNext(NodeQueue next) {
        this.next = next;
    }
}
