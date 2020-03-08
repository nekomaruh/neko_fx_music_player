package com.labs.neko.nekofxmusicplayer.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.labs.neko.nekofxmusicplayer.Activities.ScrollingActivity;
import com.labs.neko.nekofxmusicplayer.Adapters.MusicGridRecyclerAdapter;
import com.labs.neko.nekofxmusicplayer.Adapters.MusicListRecyclerAdapter;
import com.labs.neko.nekofxmusicplayer.Globals;
import com.labs.neko.nekofxmusicplayer.Lists.Artist;
import com.labs.neko.nekofxmusicplayer.Models.Music;
import com.labs.neko.nekofxmusicplayer.R;

import java.util.ArrayList;

public class ArtistFragment extends Fragment {

    private Globals globals = Globals.getInstance();


    public ArtistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_artist, container, false);
        ArrayList<Artist> artist = globals.getArtistList();

        ArrayList<Music> artistGridList = new ArrayList<>();


        for(int x = 0; x< artist.size(); x++){
            Artist currentArtist = artist.get(x);
            Music music = currentArtist.getMusicList().get(0);
            artistGridList.add(music);
        }


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), calculateNumberOfColumns(2));

        RecyclerView recyclerView = view.findViewById(R.id.gridView_artistList);
        RecyclerView.Adapter adapter = new MusicGridRecyclerAdapter(R.layout.item_list_grid,artistGridList,"artist",new MusicGridRecyclerAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(view.getContext(), ScrollingActivity.class);
                intent.putExtra("artist_position",position);
                startActivity(intent);
            }
        });


        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(gridLayoutManager);

        if(globals.getAdapterArtist()!=null){
            recyclerView.setAdapter(globals.getAdapterArtist());
        }else{
            globals.setAdapterArtist((MusicGridRecyclerAdapter) adapter);
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
