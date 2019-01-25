package com.andoresu.themoviedb.core.authorization.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("avatar")
    @Expose
    public Avatar avatar;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("iso_639_1")
    @Expose
    public String iso6391;
    @SerializedName("iso_3166_1")
    @Expose
    public String iso31661;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("include_adult")
    @Expose
    public Boolean includeAdult;
    @SerializedName("username")
    @Expose
    public String username;
    public String requestToken;
    public String sessionId;

    public String getImage(){
        if(avatar != null && avatar.gravatar != null && avatar.gravatar.hash != null){
            return "https://www.gravatar.com/avatar/" + avatar.gravatar.hash + "?s=200";
        }
        return null;
    }

}