package com.andoresu.themoviedb.core.authorization.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Session implements Serializable {

    @SerializedName("success")
    @Expose
    public Boolean success;
    @SerializedName("session_id")
    @Expose
    public String sessionId;

    public Session(String sessionId){
        this.sessionId = sessionId;
    }

}