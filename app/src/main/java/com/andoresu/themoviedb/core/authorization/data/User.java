package com.andoresu.themoviedb.core.authorization.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class User implements Serializable {

    @SerializedName("avatar")
    @Expose
    @Ignore
    public Avatar avatar;
    @SerializedName("id")
    @Expose
    @PrimaryKey
    public Integer id;
    @SerializedName("iso_639_1")
    @Expose
    @ColumnInfo
    public String iso6391;
    @SerializedName("iso_3166_1")
    @Expose
    @ColumnInfo
    public String iso31661;
    @SerializedName("name")
    @Expose
    @ColumnInfo
    public String name;
    @SerializedName("include_adult")
    @Expose
    @ColumnInfo
    public Boolean includeAdult;
    @SerializedName("username")
    @Expose
    @ColumnInfo
    public String username;
    @ColumnInfo
    public String requestToken;
    @ColumnInfo
    public String sessionId;

    public String getImage(){
        if(avatar != null && avatar.gravatar != null && avatar.gravatar.hash != null){
            return "https://www.gravatar.com/avatar/" + avatar.gravatar.hash + "?s=200";
        }
        return "http://chittagongit.com//images/default-profile-icon/default-profile-icon-24.jpg";
    }

}