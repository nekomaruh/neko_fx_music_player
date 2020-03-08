package com.labs.neko.nekofxmusicplayer.Activities;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.labs.neko.nekofxmusicplayer.Adapters.MusicListRecyclerAdapter;
import com.labs.neko.nekofxmusicplayer.Globals;
import com.labs.neko.nekofxmusicplayer.Models.Music;
import com.labs.neko.nekofxmusicplayer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ScrollingActivity extends AppCompatActivity {

    private Globals globals = Globals.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Playing", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        ImageView ivHeader = findViewById(R.id.imageView_collapsing_toolbar);
        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbar_layout);


        int album_position = getIntent().getIntExtra("album_position",-1);
        int artist_position = getIntent().getIntExtra("artist_position",-1);

        final ArrayList<Music> musicList;

        if(album_position!=-1){
            musicList = globals.getAlbumList().get(album_position).getMusicList();
        }else{
            musicList = globals.getArtistList().get(artist_position).getMusicList();
        }


        String artID = musicList.get(0).getCover();
        String titleAlbum = musicList.get(0).getAlbum();
        String titleArtist = musicList.get(0).getArtist();

        Uri uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), Long.parseLong(artID));

        toolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);


        RecyclerView recyclerView = findViewById(R.id.listView_contentScrolling);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ScrollingActivity.this);
        RecyclerView.Adapter adapter = new MusicListRecyclerAdapter(R.layout.item_list_music, musicList, new MusicListRecyclerAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Music music) {
                globals.play(music);
                globals.setHasTrack(true);
            }
        });

        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        if(album_position!=-1){
            toolbarLayout.setTitle(titleAlbum);
            loadImages(ivHeader,uri);
        }else{
            toolbarLayout.setTitle(titleArtist);
            loadImages(ivHeader,uri);
        }
    }

    private void loadImages(ImageView imageView, Uri uri){
        Picasso.get().load(uri)
                .resize(512, 512)
                .noFade().centerCrop()
                .error(R.drawable.ic_album_black_24dp)
                .placeholder(R.drawable.ic_album_black_24dp)
                .into(imageView);
    }

}
