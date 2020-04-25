package com.vgermonenko.music;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MusicItemAdapter extends RecyclerView.Adapter<MusicItemAdapter.MusicItemViewHolder> {
    ArrayList<MusicItemSerializer> musicItems;


    public MusicItemAdapter(ArrayList<MusicItemSerializer> musicItems) {
        this.musicItems = musicItems;
    }


    @NonNull
    @Override
    public MusicItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_card, parent, false);
        return new MusicItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicItemViewHolder holder, int position) {
        holder.image.setImageURI(musicItems.get(position).getImageUrl());
        holder.titleText.setText(musicItems.get(position).getTitle());
        holder.descriptionText.setText(musicItems.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return musicItems.size();
    }


    public class MusicItemViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView descriptionText;
        ImageView image;


        public MusicItemViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.music_card_image);
            titleText = itemView.findViewById(R.id.music_card_title);
            descriptionText = itemView.findViewById(R.id.music_card_description);
        }
    }

}
