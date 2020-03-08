package com.labs.neko.nekofxmusicplayer.Activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.labs.neko.nekofxmusicplayer.Globals;
import com.labs.neko.nekofxmusicplayer.Lists.Album;
import com.labs.neko.nekofxmusicplayer.Lists.Artist;
import com.labs.neko.nekofxmusicplayer.Models.Music;
import com.labs.neko.nekofxmusicplayer.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SplashActivity extends AppCompatActivity {

    public final int MULTIPLE_PERMISSIONS = 10; // code you want.

    String[] permissions= new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (checkPermissions()){
            hideNavigationBar();
            loadExternalStorage();
        }

    }

    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        if(permissions.length == 0){
            return;
        }
        boolean allPermissionsGranted = true;
        if(grantResults.length>0){
            for(int grantResult: grantResults){
                if(grantResult != PackageManager.PERMISSION_GRANTED){
                    allPermissionsGranted = false;
                    break;
                }
            }
        }
        if(!allPermissionsGranted){
            boolean somePermissionsForeverDenied = false;
            for(String permission: permissions){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
                    //denied
                    Log.e("denied", permission);
                    finish();
                }else{
                    if(ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED){
                        //allowed
                        Log.e("allowed", permission);
                    } else{
                        //set to never ask again
                        Log.e("set to never ask again", permission);
                        somePermissionsForeverDenied = true;
                    }
                }
            }
            if(somePermissionsForeverDenied){
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Permissions Required")
                        .setMessage("You have forcefully denied some of the required permissions " +
                                "for this action. Please open settings, go to permissions and allow them.")
                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
        } else {
            switch (requestCode) {
                case MULTIPLE_PERMISSIONS:{
                    if (grantResults.length > 0) {
                        if(grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_DENIED){
                            finish();
                        }else{
                            loadExternalStorage();
                        }
                    }
                }
            }
        }

    }


    private void hideNavigationBar(){
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
    }


    public void loadExternalStorage(){
        Globals globals = Globals.getInstance();
        globals.createMediaPlayer();
        ArrayList<Music> musicList = new ArrayList<>();
        ArrayList<Artist> artistList = new ArrayList<>();
        ArrayList<Album> albumList = new ArrayList<>();

        ContentResolver contentResolver = this.getContentResolver();
        Uri songURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songURI,null,null,null,null);
        short track = 0;
        if(songCursor!=null && songCursor.moveToFirst()){
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songPath = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int songCover = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int songAlbum = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            do{
                boolean isAlbumInList = false;
                boolean isArtistInList = false;
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentPath = songCursor.getString(songPath);
                String currentCover = songCursor.getString(songCover);
                String currentAlbum = songCursor.getString(songAlbum);
                Music music = new Music(currentTitle,currentArtist,currentPath, currentCover,currentAlbum,track);
                musicList.add(music);

                for(int i = 0; i< albumList.size(); i++){
                    Album list = albumList.get(i);
                    String album = list.getTitle();

                    if(album!=null && album.equals(currentAlbum)){
                        list.getMusicList().add(musicList.get(track));
                        isAlbumInList = true;
                    }
                }

                for(int i = 0; i< artistList.size(); i++){
                    Artist list = artistList.get(i);
                    String artist = list.getArtist();
                    if(artist!=null && artist.equals(currentArtist)){
                        list.getMusicList().add(musicList.get(track));
                        isArtistInList = true;
                    }
                }

                if(!isAlbumInList){
                    Album newAlbum = new Album(currentAlbum);
                    newAlbum.getMusicList().add(musicList.get(track));
                    albumList.add(newAlbum);
                }

                if(!isArtistInList){
                    Artist newArtist = new Artist(currentArtist);
                    newArtist.getMusicList().add(musicList.get(track));
                    artistList.add(newArtist);
                }

                track++;

            }while(songCursor.moveToNext());
            songCursor.close();
        }

        Collections.sort(musicList, new Comparator<Music>() {
            @Override
            public int compare(Music o1, Music o2) {
                return o1.getTitle().compareToIgnoreCase(o2.getTitle());
            }
        });

        Collections.sort(albumList, new Comparator<Album>() {
            @Override
            public int compare(Album o1, Album o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });

        Collections.sort(artistList, new Comparator<Artist>() {
            @Override
            public int compare(Artist o1, Artist o2) {
                return o1.getArtist().compareTo(o2.getArtist());
            }
        });



        globals.setMusicList(musicList);
        globals.setAlbumList(albumList);
        globals.setArtistList(artistList);



        startActivity(new Intent(SplashActivity.this,MainActivity.class));
        finish();
    }

    protected void onResume() {
        super.onResume();
        hideNavigationBar();
    }

}
