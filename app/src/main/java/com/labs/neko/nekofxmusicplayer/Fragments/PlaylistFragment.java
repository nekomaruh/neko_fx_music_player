package com.labs.neko.nekofxmusicplayer.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.labs.neko.nekofxmusicplayer.Adapters.MusicListRecyclerAdapter;
import com.labs.neko.nekofxmusicplayer.Globals;
import com.labs.neko.nekofxmusicplayer.Models.Music;
import com.labs.neko.nekofxmusicplayer.R;

public class PlaylistFragment extends Fragment {

    private Globals globals = Globals.getInstance();

    public PlaylistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.listView_playList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.Adapter adapter = new MusicListRecyclerAdapter(R.layout.item_list_music, globals.getMusicList(), new MusicListRecyclerAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Music music) {
                globals.play(music.getTrack());
                globals.setHasTrack(true);
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

}
