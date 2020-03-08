package com.labs.neko.nekofxmusicplayer.Adapters;

import android.content.ContentUris;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.labs.neko.nekofxmusicplayer.Models.Music;
import com.labs.neko.nekofxmusicplayer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MusicGridRecyclerAdapter extends RecyclerView.Adapter<MusicGridRecyclerAdapter.ViewHolder> {

    private int layout;
    private List<Music> musicList;
    private Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
    private String type;
    private onItemClickListener itemClickListener;

    public MusicGridRecyclerAdapter(int layout, List<Music> musicList, String type, onItemClickListener itemClickListener) {
        this.layout = layout;
        this.musicList = musicList;
        this.type = type;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout,viewGroup,false);
        return new MusicGridRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(musicList.get(i),itemClickListener);
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private ImageView ivCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvTitle = itemView.findViewById(R.id.textView_musicGrid);
            this.ivCover = itemView.findViewById(R.id.imageView_grid);
        }

        public void bind(final Music music, final onItemClickListener listener){
            String currentTitle = "";
            if(type.equals("album")){
                currentTitle = music.getAlbum();
            }
            if(type.equals("artist")){
                currentTitle = music.getArtist();
            }
            String currentCover = music.getCover();

            long parseLong = Long.parseLong(currentCover);
            Uri uri = ContentUris.withAppendedId(sArtworkUri, parseLong);

            this.tvTitle.setText(currentTitle);

            Picasso.get().load(uri)
                    .resize(256, 256)
                    .noFade().centerCrop()
                    .error(R.drawable.ic_album_black_24dp)
                    .placeholder(R.drawable.ic_album_black_24dp)
                    .into(this.ivCover);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void updateList(ArrayList<Music> list){
        musicList = new ArrayList<>();
        musicList.addAll(list);
        notifyDataSetChanged();
    }
}
