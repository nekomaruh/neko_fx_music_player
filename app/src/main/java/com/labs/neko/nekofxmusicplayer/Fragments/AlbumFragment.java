package com.labs.neko.nekofxmusicplayer.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.labs.neko.nekofxmusicplayer.Activities.ScrollingActivity;
import com.labs.neko.nekofxmusicplayer.Adapters.MusicGridRecyclerAdapter;
import com.labs.neko.nekofxmusicplayer.Globals;
import com.labs.neko.nekofxmusicplayer.Lists.Album;
import com.labs.neko.nekofxmusicplayer.Models.Music;
import com.labs.neko.nekofxmusicplayer.R;

import java.util.ArrayList;


public class AlbumFragment extends Fragment {

    private Globals globals = Globals.getInstance();

    public AlbumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_album, container, false);
        ArrayList<Album> album = globals.getAlbumList();


        ArrayList<Music> musicList = new ArrayList<>();

        for(int x = 0; x< album.size(); x++){
            Album currentAlbum = album.get(x);
            Music music = currentAlbum.getMusicList().get(0);
            musicList.add(music);
        }

        RecyclerView recyclerView = view.findViewById(R.id.gridView_albumList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),calculateNumberOfColumns(2));
        RecyclerView.Adapter adapter = new MusicGridRecyclerAdapter(R.layout.item_list_grid,musicList, "album",new MusicGridRecyclerAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(view.getContext(), ScrollingActivity.class);
                intent.putExtra("album_position",position);
                startActivity(intent);
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);


        if(globals.getAdapterAlbum()!=null){
            recyclerView.setAdapter(globals.getAdapterAlbum());
        }else{
            globals.setAdapterAlbum((MusicGridRecyclerAdapter) adapter);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

    protected int calculateNumberOfColumns(int base){
        int columns = base;
        String screenSize = getScreenSizeCategory();

        if(screenSize.equals("small")){
            if(base!=1){
                columns = columns-1;
            }
        }else if (screenSize.equals("normal")){
            // Do nothing
        }else if(screenSize.equals("large")){
            columns += 1;
        }else if (screenSize.equals("xlarge")){
            columns += 2;
        }

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            columns = (int) (columns * 1.5);
        }

        return columns;
    }

    // Custom method to get screen size category
    protected String getScreenSizeCategory(){
        int screenLayout = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        switch(screenLayout){
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                // small screens are at least 426dp x 320dp
                return "small";
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                // normal screens are at least 470dp x 320dp
                return "normal";
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                // large screens are at least 640dp x 480dp
                return "large";
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                // xlarge screens are at least 960dp x 720dp
                return "xlarge";
            default:
                return "undefined";
        }
    }
}
