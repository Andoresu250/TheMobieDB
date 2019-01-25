package com.andoresu.themoviedb.core.movies.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FavoriteRequest implements Serializable {

    public static final String MEDIA_TYPE_MOVIE = "movie";

    @SerializedName("media_type")
    @Expose
    public String mediaType;
    @SerializedName("media_id")
    @Expose
    public Integer mediaId;
    @SerializedName("favorite")
    @Expose
    public Boolean favorite;

    public FavoriteRequest(Movie movie){
        this.mediaType = MEDIA_TYPE_MOVIE;
        this.mediaId = movie.id;
        this.favorite = true;
    }

}
