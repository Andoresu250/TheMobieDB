package com.andoresu.themoviedb.client;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class GsonBuilderUtils {

    public static Gson SERIALIZED_GSON = getGenericGson(true);

    public static Gson getGenericGson(boolean lowerCase){
        return getGenericBuilder(lowerCase).create();
    }

    public static Gson getGenericGson(){
        return getGenericBuilder().create();
    }

    private static GsonBuilder getGenericBuilder(boolean lowerCase){
//        GsonBuilder builder = Utils.gsonBuilderWithDate();
        GsonBuilder builder = new GsonBuilder()
                .setDateFormat("yyyy-mm-dd")
                .setExclusionStrategies(new AnnotationExclusionStrategy());
        if(lowerCase){
            builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        }
        return builder;
    }

    private static GsonBuilder getGenericBuilder(){
        return getGenericBuilder(false);
    }

}
