package com.andoresu.themoviedb.utils;

import java.io.Serializable;

public abstract class BaseListResponse implements Serializable {

    public static final int PER_PAGE = 15;

    public Integer totalCount;

    public int getTotalPage(){

        double res = (double)totalCount /(double) PER_PAGE;
        return (int) Math.ceil(res);
    }

}
