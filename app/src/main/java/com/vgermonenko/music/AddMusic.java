package com.vgermonenko.music;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;


public class AddMusic extends Fragment {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    ImageView previewImage;
    Uri imageUrl;

    EditText albumTitleText;
    EditText albumDescriptionText;

    Button acceptButton;
    Button cancelButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_music, container, false);

        Activity activity = getActivity();
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

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
                startActivityForResult(Intent.createChooser(intent, "Select File"), 0);
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = albumTitleText.getText().toString();
                String description = albumDescriptionText.getText().toString();

                try {
                    MusicGallery.AddMusicItem(title, description, copyImage(imageUrl));
                    MusicGallery.updateFileStorage(getContext());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

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

    private Uri copyImage(Uri imageUrl) throws IOException {
        File sourceImage = new File(getRealPathFromURI(imageUrl));
        boolean exists = sourceImage.exists();
        File targetImage = new File(getActivity().getFilesDir().getPath() + "/images/" + sourceImage.getName());
        if (!targetImage.exists()) {
            targetImage.createNewFile();
        }

        FileChannel sourceImageChannel = new FileInputStream(sourceImage).getChannel();
        FileChannel targetImageChannel = new FileOutputStream(targetImage).getChannel();
        targetImageChannel.transferFrom(sourceImageChannel, 0 , sourceImageChannel.size());
        return Uri.parse(targetImage.getPath());
    }

    private void cleanInputFields(){
        albumTitleText.setText("");
        albumDescriptionText.setText("");
        previewImage.setImageURI(null);
        acceptButton.setActivated(false);
    }

    private String getRealPathFromURI(Uri contentUri) {

        String[] proj = { MediaStore.Video.Media.DATA };
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
