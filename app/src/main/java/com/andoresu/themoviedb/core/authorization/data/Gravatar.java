package com.andoresu.themoviedb.core.authorization.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Gravatar implements Serializable {
    @SerializedName("hash")
    @Expose
    public String hash;
}
