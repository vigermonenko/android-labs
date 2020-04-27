package com.vgermonenko.music.services;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.vgermonenko.music.MusicItemSerializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class JsonFileService {
    private static final String MUSIC_ITEMS_ARRAY_NAME = "music_items";
    private static final String IMAGE_URL_SECTION = "image_url";
    private static final String TITLE_SECTION = "title";
    private static final String DESCRIPTION_SECTION = "description";

    private final String fileName;
    private final Context context;


    public JsonFileService(String fileToWorkWith, Context context) {
        this.fileName = fileToWorkWith;
        this.context = context;
    }


    public void updateFile(List<MusicItemSerializer> items) {
        try {
            JSONArray jsonArray = serializeObjects(items);
            JSONObject instance = new JSONObject();
            instance.put(MUSIC_ITEMS_ARRAY_NAME, jsonArray);
            tryWriteToFile(instance.toString());
        } catch (Exception ex) {

        }

    }

    public List<MusicItemSerializer> readFromFile() {
        String plainText = tryReadFromFile();
        if (plainText == null || plainText.equals("")) {
            return Collections.emptyList();
        }

        try {
            JSONObject jsonObject =  new JSONObject(plainText);
            JSONArray jsonItems = jsonObject.getJSONArray(MUSIC_ITEMS_ARRAY_NAME);
            return deserializeObjects(jsonItems);
        } catch (JSONException ex) {
            return Collections.emptyList();
        }
    }

    private List<MusicItemSerializer> deserializeObjects(JSONArray jsonArray) throws JSONException {
        List<MusicItemSerializer> musicItems = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i += 1) {
            JSONObject jsonItem = jsonArray.getJSONObject(i);
            String imageUrl = jsonItem.getString(IMAGE_URL_SECTION);
            String title = jsonItem.getString(TITLE_SECTION);
            String description = jsonItem.getString(DESCRIPTION_SECTION);

            musicItems.add(new MusicItemSerializer(title, description, Uri.parse(imageUrl)));
        }

        return musicItems;
    }

    private JSONArray serializeObjects(List<MusicItemSerializer> items) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (MusicItemSerializer item : items) {
                JSONObject jsonItem = new JSONObject();
                jsonItem.put(IMAGE_URL_SECTION, item.getImageUrl().toString());
                jsonItem.put(TITLE_SECTION, item.getTitle()).toString();
                jsonItem.put(DESCRIPTION_SECTION, item.getDescription());
                jsonArray.put(jsonItem);
            }
        } catch (JSONException ex) {
        }
        return jsonArray;
    }

    private String tryReadFromFile() {
        try {
            FileInputStream inputStream = context.openFileInput(fileName);
            String result = new BufferedReader(new InputStreamReader(inputStream)).readLine();
            inputStream.close();
            return result;
        } catch (IOException e) {
            return "";
        }
    }

    private void tryWriteToFile(String text) {
        try {
            FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(text.getBytes());
            outputStream.close();
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
    }
}
