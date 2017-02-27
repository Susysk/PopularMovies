package com.example.android.popularmovies.movies;

/**
 * Created by Susy on 18/02/2017.
 */

public class Trailer {
    public Trailer(String id, String iso_639_1, String key, String name, String site, String size){
        this.id=id;
        this.iso_639_1=iso_639_1;
        this.key=key;
        this.name=name;
        this.site=site;
        this.size=size;

    }
    public Trailer(){

    }
    public Trailer(String s){
        this.name=s;
    }
    public  String id;
    public String iso_639_1;
    public  String key;
    public  String name;


    public  String getIso_639_1() {
        return iso_639_1;
    }

    public  void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public String getKey() {
        return key;
    }

    public  void setKey(String key) {
        this.key = key;
    }

    public  String getName() {
        return name;
    }

    public  void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public  void setSite(String site) {
        this.site = site;
    }

    public  String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getId() {

        return id;
    }

    public  void setId(String id) {
        this.id = id;
    }

    public String site;

    public  String getType() {
        return type;
    }

    public  void setType(String type) {
        this.type = type;
    }

    public  String type;
    public  String size;

    @Override
    public String toString() {
        return this.name;
    }
}
