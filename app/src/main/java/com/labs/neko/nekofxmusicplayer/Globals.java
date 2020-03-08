package com.labs.neko.nekofxmusicplayer;

import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.EnvironmentalReverb;
import android.media.audiofx.Equalizer;
import android.media.audiofx.LoudnessEnhancer;
import android.media.audiofx.Virtualizer;
import android.net.Uri;

import com.labs.neko.nekofxmusicplayer.Adapters.MusicGridRecyclerAdapter;
import com.labs.neko.nekofxmusicplayer.Adapters.MusicListRecyclerAdapter;
import com.labs.neko.nekofxmusicplayer.Lists.Album;
import com.labs.neko.nekofxmusicplayer.Lists.Artist;
import com.labs.neko.nekofxmusicplayer.Models.Music;

import java.io.IOException;
import java.util.ArrayList;

public class Globals{

    private static Globals globals;

    private ArrayList<Music> musicList;
    private ArrayList<Album> albumList;
    private ArrayList<Artist> artistList;
    private MusicListRecyclerAdapter adapter;
    private MusicGridRecyclerAdapter adapterArtist;
    private MusicGridRecyclerAdapter adapterAlbum;
    private Queue queue;
    private int track;
    private Music music;

    private MediaPlayer mediaPlayer;
    private Equalizer equalizer;
    private Virtualizer virtualizer;
    private BassBoost bassBoost;
    private EnvironmentalReverb environmentalReverb;
    //private LoudnessEnhancer loudnessEnhancer;
    private boolean hasTrack = false;

    public static synchronized Globals getInstance(){
        if(globals==null){
            globals = new Globals();
        }
        return globals;
    }

    public void createMediaPlayer(){
        mediaPlayer = new MediaPlayer();
        equalizer = new Equalizer(0,mediaPlayer.getAudioSessionId());
        virtualizer = new Virtualizer(0,mediaPlayer.getAudioSessionId());
        bassBoost = new BassBoost(0,mediaPlayer.getAudioSessionId());
        environmentalReverb = new EnvironmentalReverb(0,0);
        mediaPlayer.attachAuxEffect(environmentalReverb.getId());
        //loudnessEnhancer = new LoudnessEnhancer(mediaPlayer.getAudioSessionId());
        mediaPlayer.setAuxEffectSendLevel(1.0f);
        queue = new Queue();
    }

    public void play(int position){
        this.track = position;
        this.music = musicList.get(position);
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(this.music.getPath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play(Music music){
        this.music = music;
        this.track = music.getTrack();
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(music.getPath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playQueue(){
        mediaPlayer.reset();
        track = queue.popMusic().getTrack();
        try {
            mediaPlayer.setDataSource(musicList.get(track).getPath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Music> getMusicList() {
        return musicList;
    }

    public void setMusicList(ArrayList<Music> musicList) {
        this.musicList = musicList;
    }

    public void setAlbumList(ArrayList<Album> album) {
        this.albumList = album;
    }

    public void setArtistList(ArrayList<Artist> artist) {
        this.artistList = artist;
    }

    public ArrayList<Album> getAlbumList() {
        return albumList;
    }

    public ArrayList<Artist> getArtistList() {
        return artistList;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public EnvironmentalReverb getEnvironmentalReverb() {
        return environmentalReverb;
    }

    public Equalizer getEqualizer() {
        return equalizer;
    }

    public Virtualizer getVirtualizer() {
        return virtualizer;
    }

    public BassBoost getBassBoost() {
        return bassBoost;
    }

    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    public void setHasTrack(boolean hasTrack) {
        this.hasTrack = hasTrack;
    }

    public boolean isHasTrack() {
        return hasTrack;
    }

    public Queue getQueue() {
        return queue;
    }

    public MusicListRecyclerAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(MusicListRecyclerAdapter adapter) {
        this.adapter = adapter;
    }

    public Music getMusic() {
        return music;
    }

    public MusicGridRecyclerAdapter getAdapterAlbum() {
        return adapterAlbum;
    }

    public void setAdapterAlbum(MusicGridRecyclerAdapter adapterAlbum) {
        this.adapterAlbum = adapterAlbum;
    }

    public MusicGridRecyclerAdapter getAdapterArtist() {
        return adapterArtist;
    }

    public void setAdapterArtist(MusicGridRecyclerAdapter adapterArtist) {
        this.adapterArtist = adapterArtist;
    }
}
