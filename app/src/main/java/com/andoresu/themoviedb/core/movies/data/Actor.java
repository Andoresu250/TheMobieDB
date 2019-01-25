package com.andoresu.themoviedb.core.movies.data;

import com.andoresu.themoviedb.client.ServiceGenerator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Actor implements Serializable {

    @SerializedName("cast_id")
    @Expose
    public Integer castId;
    @SerializedName("character")
    @Expose
    public String character;
    @SerializedName("credit_id")
    @Expose
    public String creditId;
    @SerializedName("gender")
    @Expose
    public Integer gender;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("order")
    @Expose
    public Integer order;
    @SerializedName("profile_path")
    @Expose
    public String profilePath;

    public String getProfilePath(){
        return ServiceGenerator.API_IMAGE_URL + "w342" + profilePath;
    }

}