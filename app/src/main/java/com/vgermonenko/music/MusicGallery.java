package com.vgermonenko.music;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class MusicGallery extends Fragment {
    private FloatingActionButton addButton;

    public static RecyclerView musicRecycleView;
    public static ArrayList<MusicItemSerializer> musicItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_gallery, container, false);

        addButton = view.findViewById(R.id.add_button);

        musicRecycleView = view.findViewById(R.id.music_recycler);
        musicRecycleView.setAdapter(new MusicItemAdapter(musicItems));
        musicRecycleView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_frame, new AddMusic()).commit();
            }
        });

        return view;
    }

    public static void AddMusicItem(String title, String description, Uri imageUrl) {
        musicItems.add(new MusicItemSerializer(title, description, imageUrl));
        MusicItemAdapter adapter = new MusicItemAdapter(musicItems);
        musicRecycleView.setAdapter(adapter);
    }
}
