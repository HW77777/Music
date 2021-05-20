package com.example.music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Myadapter extends ArrayAdapter {
    private static final String TAG="Myadapter";
    public Myadapter(@NonNull Context context, int resource, ArrayList<Song> songs) {
        super(context, resource,songs);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.list,parent,false);
        }

        Song s= (Song) getItem(position);
        TextView song = (TextView) itemView.findViewById(R.id.item_mymusic_song);
        TextView singer = (TextView) itemView.findViewById(R.id.item_mymusic_singer);
        TextView duration = (TextView) itemView.findViewById(R.id.item_mymusic_duration);
        TextView album = (TextView) itemView.findViewById(R.id.item_mymusic_album);
        TextView path = (TextView) itemView.findViewById(R.id.czxx);

        song.setText(s.getSong());
        singer.setText(s.getSinger());
        album.setText(s.getAlbum());
        duration.setText(s.getDuration());
        path.setText(s.getPath());

        return itemView;
    }
}
