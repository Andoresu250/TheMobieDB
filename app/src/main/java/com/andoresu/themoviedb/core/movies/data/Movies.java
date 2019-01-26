package com.andoresu.themoviedb.core.movies.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Movies implements Serializable{

    @SerializedName("results")
    @Expose
    public List<Movie> results;
    @SerializedName("page")
    @Expose
    public Integer page;
    @SerializedName("total_results")
    @Expose
    public Integer totalResults;
    @SerializedName("dates")
    @Expose
    public Dates dates;
    @SerializedName("total_pages")
    @Expose
    public Integer totalPages;

    public void setGenres(List<Genre> genres){
        for(Movie movie : results){
            movie.setGenres(genres);
        }
    }

    public Movies(){}

    public Movies(List<Movie> results){
        this.results = results;
        this.page = 1;
        this.totalResults = results.size();
        this.totalPages = 1;
    }


}