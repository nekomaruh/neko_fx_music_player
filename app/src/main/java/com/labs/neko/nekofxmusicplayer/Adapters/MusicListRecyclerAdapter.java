package com.labs.neko.nekofxmusicplayer.Adapters;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.labs.neko.nekofxmusicplayer.Globals;
import com.labs.neko.nekofxmusicplayer.Models.Music;
import com.labs.neko.nekofxmusicplayer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MusicListRecyclerAdapter extends RecyclerView.Adapter<MusicListRecyclerAdapter.ViewHolder> {

    private int layout;
    private List<Music> musicList;
    private onItemClickListener itemClickListener;
    private final static Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

    public MusicListRecyclerAdapter(int layout, List<Music> musicList,onItemClickListener itemClickListener) {
        this.layout = layout;
        this.musicList = musicList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MusicListRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicListRecyclerAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(musicList.get(i),i,itemClickListener);
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvArtist;
        private ImageView ivArt;
        private Button btnMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvTitle = itemView.findViewById(R.id.textView_title);
            this.tvArtist = itemView.findViewById(R.id.textView_artist);
            this.ivArt = itemView.findViewById(R.id.imageView_cover);
            this.btnMenu = itemView.findViewById(R.id.button_moreVerticalList);
        }

        public void bind(final Music music, final int i, final onItemClickListener listener){
            String currentName = music.getTitle();
            String currentArtist = music.getArtist();
            String currentArt = music.getCover();

            long parseLong = Long.parseLong(currentArt);
            Uri uri = ContentUris.withAppendedId(sArtworkUri,parseLong);

            Picasso.get().load(uri)
                    .resize(128,128)
                    .noFade().centerCrop()
                    .error(R.drawable.ic_album_black_24dp)
                    .placeholder(R.drawable.ic_album_black_24dp)
                    .into(this.ivArt);

            this.tvTitle.setText(currentName);
            this.tvArtist.setText(currentArtist);

            btnMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final PopupMenu popup = new PopupMenu(v.getContext(), btnMenu);
                    popup.inflate(R.menu.music_menu);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.music_menu_playnow:
                                    Globals.getInstance().play(musicList.get(i).getTrack());
                                    return true;
                                case R.id.music_menu_playnext:
                                    Globals.getInstance().getQueue().pushFirst(music);
                                    return true;
                                case R.id.music_menu_addqueue:
                                    Globals.getInstance().getQueue().pushEnd(music);
                                    return true;
                                case R.id.music_menu_addplaylist:
                                    return true;
                                case R.id.music_menu_share:
                                    String sharePath = music.getPath();
                                    Uri uriPath = Uri.parse(sharePath);
                                    Intent share = new Intent(Intent.ACTION_SEND);
                                    share.setType("audio/*");
                                    share.putExtra(Intent.EXTRA_STREAM, uriPath);
                                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    v.getContext().startActivity(Intent.createChooser(share, "Share Sound File"));
                                    return true;
                                default:
                                    return onMenuItemClick(item);
                            }

                        }
                    });
                    popup.show();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(music);
                }
            });


        }
    }

    public interface onItemClickListener{
        void onItemClick(Music music);
    }

    public void updateList(ArrayList<Music> list){
        musicList = new ArrayList<>();
        musicList.addAll(list);
        notifyDataSetChanged();
    }
}
