package com.andoresu.themoviedb.core.movies.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Dates implements Serializable {

    @SerializedName("maximum")
    @Expose
    public Date maximum;
    @SerializedName("minimum")
    @Expose
    public Date minimum;

    public Dates(){}


}
