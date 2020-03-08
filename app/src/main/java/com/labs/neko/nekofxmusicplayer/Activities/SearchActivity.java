package com.labs.neko.nekofxmusicplayer.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;

import com.labs.neko.nekofxmusicplayer.Adapters.MusicListRecyclerAdapter;
import com.labs.neko.nekofxmusicplayer.Globals;
import com.labs.neko.nekofxmusicplayer.ItemDivider;
import com.labs.neko.nekofxmusicplayer.Models.Music;
import com.labs.neko.nekofxmusicplayer.R;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final Globals globals = Globals.getInstance();

        SearchView searchView = findViewById(R.id.searchView);
        RecyclerView recyclerView = findViewById(R.id.recyclerView_search);
        final MusicListRecyclerAdapter adapter = globals.getAdapter();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchActivity.this);

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        ItemDivider itemDivider = new ItemDivider(SearchActivity.this);
        recyclerView.addItemDecoration(itemDivider);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();

                final ArrayList<Music> filterList = new ArrayList<>();
                for (Music music : globals.getMusicList()) {
                    String title = music.getTitle().toLowerCase();
                    String artist = music.getArtist().toLowerCase();
                    if (title.contains(newText) || artist.contains(newText)) {
                        filterList.add(music);
                    }
                }
                adapter.updateList(filterList);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
