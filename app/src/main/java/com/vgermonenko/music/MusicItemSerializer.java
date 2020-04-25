package com.vgermonenko.music;

import android.net.Uri;

public class MusicItemSerializer {
    private String title;
    private String description;

    private Uri imageUrl;

    public MusicItemSerializer(String title, String description, Uri imageUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Uri getImageUrl() {
        return imageUrl;
    }
}
