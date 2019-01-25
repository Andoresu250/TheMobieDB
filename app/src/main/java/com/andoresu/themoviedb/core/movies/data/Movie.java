package com.andoresu.themoviedb.core.movies.data;

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

public class Movie implements Serializable {

    @SerializedName("adult")
    @Expose
    public Boolean adult;
    @SerializedName("backdrop_path")
    @Expose
    public String backdropPath;
    @SerializedName("budget")
    @Expose
    public Integer budget;
    @SerializedName("genres")
    @Expose
    public List<Genre> genres;
    @SerializedName("genre_ids")
    @Expose
    public List<Integer> genreIds;
    @SerializedName("homepage")
    @Expose
    public String homepage;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("imdb_id")
    @Expose
    public String imdbId;
    @SerializedName("original_language")
    @Expose
    public String originalLanguage;
    @SerializedName("original_title")
    @Expose
    public String originalTitle;
    @SerializedName("overview")
    @Expose
    public String overview;
    @SerializedName("popularity")
    @Expose
    public Float popularity;
    @SerializedName("poster_path")
    @Expose
    public String posterPath;
    @SerializedName("production_companies")
    @Expose
    public List<ProductionCompany> productionCompanies = null;
    @SerializedName("production_countries")
    @Expose
    public List<ProductionCountry> productionCountries = null;
    @SerializedName("release_date")
    @Expose
    public Date releaseDate;
    @SerializedName("revenue")
    @Expose
    public Integer revenue;
    @SerializedName("runtime")
    @Expose
    public Integer runtime;
    @SerializedName("spoken_languages")
    @Expose
    public List<SpokenLanguage> spokenLanguages = null;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("tagline")
    @Expose
    public String tagline;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("video")
    @Expose
    public Boolean video;
    @SerializedName("vote_average")
    @Expose
    public Float voteAverage;
    @SerializedName("vote_count")
    @Expose
    public Integer voteCount;
    @SerializedName("videos")
    @Expose
    public Videos videos;
    @SerializedName("casts")
    @Expose
    public Casts casts;


    public Movie() {}

    public String getPosterPath(){
        return ServiceGenerator.API_IMAGE_URL + "w342" + posterPath;
    }

    public String getPosterThumbPath(){
        return ServiceGenerator.API_IMAGE_URL + "w154" + posterPath;
    }

    public String getBackdropPath(){
        return ServiceGenerator.API_IMAGE_URL + "w342" + backdropPath;
    }

    public String getBackdropThumbPath(){
        return ServiceGenerator.API_IMAGE_URL + "w154" + backdropPath;
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
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Log.i("ASD", "getReleaseDate: " + releaseDate);
        return dateFormat.format(releaseDate);
    }

    @Override
    public String toString() {
        String s = "";
        s += "voteCount: " + voteCount + "\n";
        s += "id: " + id + "\n";
        s += "video: " + video + "\n";
        s += "voteAverage: " + voteAverage + "\n";
        s += "title: " + title + "\n";
        s += "popularity: " + popularity + "\n";
        s += "posterPath: " + posterPath + "\n";
        s += "originalLanguage: " + originalLanguage + "\n";
        s += "originalTitle: " + originalTitle + "\n";
        s += "genreIds: " + genreIds + "\n";
        s += "backdropPath: " + backdropPath + "\n";
        s += "adult: " + adult + "\n";
        s += "overview: " + overview + "\n";
        s += "releaseDate: " + releaseDate + "\n";
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

    public String getGenres(){
        String s = "";
        for(Genre genre : genres){
            s += genre.name + ", ";
        }
        return s.substring(0, s.length() - 2);
    }
}
