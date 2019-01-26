package com.andoresu.themoviedb.core.movies.data;

import android.annotation.SuppressLint;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.util.Log;

import com.andoresu.themoviedb.client.ServiceGenerator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Movie implements Serializable {

    @SerializedName("id")
    @Expose
    @PrimaryKey
    public Integer id;
    @SerializedName("overview")
    @Expose
    @ColumnInfo
    public String overview;
    @SerializedName("popularity")
    @Expose
    @ColumnInfo
    public Float popularity;
    @SerializedName("release_date")
    @Expose
    @ColumnInfo
    public Date releaseDate;
    @SerializedName("title")
    @Expose
    @ColumnInfo
    public String title;
    @SerializedName("vote_average")
    @Expose
    @ColumnInfo
    public Float voteAverage;
    @ColumnInfo
    public String allGenres;
    @ColumnInfo
    public String poster;
    @ColumnInfo
    public String backdrop;
    @ColumnInfo
    public Boolean favorite;


    @SerializedName("adult")
    @Expose
    @Ignore
    public Boolean adult;
    @SerializedName("backdrop_path")
    @Expose
    @Ignore
    public String backdropPath;
    @SerializedName("budget")
    @Expose
    @Ignore
    public Integer budget;
    @SerializedName("genres")
    @Expose
    @Ignore
    public List<Genre> genres;
    @SerializedName("genre_ids")
    @Expose
    @Ignore
    public List<Integer> genreIds;
    @SerializedName("homepage")
    @Expose
    @Ignore
    public String homepage;
    @SerializedName("imdb_id")
    @Expose
    @Ignore
    public String imdbId;
    @SerializedName("original_language")
    @Expose
    @Ignore
    public String originalLanguage;
    @SerializedName("original_title")
    @Expose
    @Ignore
    public String originalTitle;
    @SerializedName("poster_path")
    @Expose
    @Ignore
    public String posterPath;
    @SerializedName("production_companies")
    @Expose
    @Ignore
    public List<ProductionCompany> productionCompanies = null;
    @SerializedName("production_countries")
    @Expose
    @Ignore
    public List<ProductionCountry> productionCountries = null;
    @SerializedName("revenue")
    @Expose
    @Ignore
    public Integer revenue;
    @SerializedName("runtime")
    @Expose
    @Ignore
    public Integer runtime;
    @SerializedName("spoken_languages")
    @Expose
    @Ignore
    public List<SpokenLanguage> spokenLanguages = null;
    @SerializedName("status")
    @Expose
    @Ignore
    public String status;
    @SerializedName("tagline")
    @Expose
    @Ignore
    public String tagline;
    @SerializedName("video")
    @Expose
    @Ignore
    public Boolean video;
    @SerializedName("vote_count")
    @Expose
    @Ignore
    public Integer voteCount;
    @SerializedName("videos")
    @Expose
    @Ignore
    public Videos videos;
    @SerializedName("casts")
    @Expose
    @Ignore
    public Casts casts;


    public Movie() {}

    public String getPosterThumbPath(){
        return ServiceGenerator.API_IMAGE_URL + "w154" + posterPath;
    }

    public String getBackdropPath(){
        if(backdrop != null){
            return backdrop;
        }
        return ServiceGenerator.API_IMAGE_URL + "w342" + backdropPath;
    }

    public Video getYoutubeVideo(){
        if(videos != null && videos.results != null && !videos.results.isEmpty()){
            for(Video video : videos.results){
                if(video.isFromYoutube()){
                    return video;
                }
            }
        }
        return null;
    }

    public int getScore(){
        return (int) (voteAverage * 10);
    }

    public String getReleaseDate(){
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        return dateFormat.format(releaseDate);
    }

    @Override
    public String toString() {
        String s = "";
        s += "id: " + id + "\n";
        s += "allGenres: " + allGenres + "\n";
        s += "poster: " + poster + "\n";
        return s;
    }

    public String getYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(releaseDate);
        return "(" + calendar.get(Calendar.YEAR) + ")";
    }

    public void setGenres(List<Genre> allGenres){
        this.genres = new ArrayList<>();
        for(Integer genreId : genreIds){
            for(Genre genre : allGenres){
                if(genreId.equals(genre.id)){
                    this.genres.add(genre);
                }
            }
        }
    }

    public void init(){
        this.allGenres = getGenres();
    }

    public String getGenres(){
        if(genres != null){
            String s = "";
            for(Genre genre : genres){
                s += genre.name + ", ";
            }
            return s.substring(0, s.length() - 2);
        }
        return allGenres;

    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Movie){
            return this.id.equals(((Movie) obj).id);
        }
        return false;
    }
}
