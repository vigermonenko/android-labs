package com.vgermonenko.music;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class AddMusic extends Fragment {
    ImageView previewImage;
    Uri imageUrl;

    EditText albumTitleText;
    EditText albumDescriptionText;

    Button acceptButton;
    Button cancelButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_music, container, false);

        previewImage = view.findViewById(R.id.image_preview);

        albumTitleText = view.findViewById(R.id.album_title);
        albumDescriptionText = view.findViewById(R.id.album_description);

        acceptButton = view.findViewById(R.id.accept_button);
        Button cancelButton = view.findViewById(R.id.cancel_button);

        previewImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                startActivityForResult(intent, 0);
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = albumTitleText.getText().toString();
                String description = albumDescriptionText.getText().toString();
                MusicGallery.AddMusicItem(title, description, imageUrl);

                cleanInputFields();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MusicGallery()).commit();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanInputFields();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MusicGallery()).commit();
            }
        });

        acceptButton.setActivated(false);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == Activity.RESULT_OK && requestCode == 0 && data != null) {
            previewImage.setImageURI(data.getData());
            this.imageUrl = data.getData();
            acceptButton.setActivated(true);
        }
    }

    private void cleanInputFields(){
        albumTitleText.setText("");
        albumDescriptionText.setText("");
        previewImage.setImageURI(null);
        acceptButton.setActivated(false);
    }
}
