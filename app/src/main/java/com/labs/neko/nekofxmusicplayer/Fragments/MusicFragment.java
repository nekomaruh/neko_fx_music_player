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
import com.labs.neko.nekofxmusicplayer.ItemDivider;
import com.labs.neko.nekofxmusicplayer.Models.Music;
import com.labs.neko.nekofxmusicplayer.R;


public class MusicFragment extends Fragment {

    public MusicFragment(){
        // Required empty public constructor
    }

    private Globals globals = Globals.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.listView_music_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        RecyclerView.Adapter adapter = new MusicListRecyclerAdapter(R.layout.item_list_music, globals.getMusicList(), new MusicListRecyclerAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Music music) {
                globals.play(music);
                globals.setHasTrack(true);
            }
        });

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        //recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.addItemDecoration(new ItemDivider(view.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(linearLayoutManager);

        if(globals.getAdapter()!=null){
            recyclerView.setAdapter(globals.getAdapter());
        }else{
            globals.setAdapter((MusicListRecyclerAdapter) adapter);
            recyclerView.setAdapter(adapter);
        }


        return view;
    }

}
