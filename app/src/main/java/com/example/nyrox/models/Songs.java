package com.example.nyrox.models;

public class Songs {
    private String id, title, artist, songUrl, imageUrl, keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }


    public String getId() {
        return id;
    }

    public Songs() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Songs(String id, String title, String artist, String songUrl, String imageUrl, String keyword) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.songUrl = songUrl;
        this.imageUrl = imageUrl;
        this.keyword = keyword;
    }
}
