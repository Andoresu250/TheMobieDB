package com.andoresu.themoviedb.core.authorization.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class RequestToken implements Serializable {

    @SerializedName("success")
    @Expose
    public Boolean success;
    @SerializedName("expires_at")
    @Expose
    public Date expiresAt;
    @SerializedName("request_token")
    @Expose
    public String requestToken;

    public String getToken(){
        return requestToken;
    }

}