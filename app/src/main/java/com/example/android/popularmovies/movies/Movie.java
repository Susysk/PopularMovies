package com.example.android.popularmovies.movies;

import android.os.Parcel;
import android.os.Parcelable;
public class Movie implements Parcelable {
    public String title;
    public String original_title;
    public  String original_language;
    public  String overview;
    public String popularity;
    public String release;
    public String posterPath;
    public  String path;
    public String vote_average;
    public String vote_count;
    public  String revenue;
    public int id;

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    public Movie() {
    }
    private Movie(Parcel parcel){
        this.id = parcel.readInt();
        this.title = parcel.readString();
        this.original_title = parcel.readString();
        this.overview = parcel.readString();
        this.path = parcel.readString();
        this.release = parcel.readString();
        this.vote_average = parcel.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }
    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    public String getRevenue() {
        return revenue;
    }
    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(original_title);
        dest.writeString(posterPath);
        dest.writeLong(id);
        dest.writeString(release);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(path);
        dest.writeString(vote_average);

        }
    }