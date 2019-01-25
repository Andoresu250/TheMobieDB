package com.andoresu.themoviedb.client;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;

import retrofit2.Response;

import static com.andoresu.themoviedb.client.GsonBuilderUtils.SERIALIZED_GSON;

@SuppressLint("LogNotTimber")
public class ErrorResponse implements Serializable{

    private final static String TAG = "THEMOVIEDB_" + ErrorResponse.class.getSimpleName();

    public Integer statusCode;
    public String statusMessage;


    public ErrorResponse(){}

    public static ErrorResponse getFailErrorResponse(){
        return new ErrorResponse("Error Desconocido ", 0);
    }

    public static ErrorResponse getBadJsonResponse(){
        return new ErrorResponse("Error al deserializar información ", 0);
    }

    public static ErrorResponse failConnection(){
        return new ErrorResponse("No hay una red disponible ", 0);
    }

    public static ErrorResponse timeOut(){
        return new ErrorResponse("Tiempo de espera agotado intente otra vez ", 0);
    }

    public static ErrorResponse runTimeException(){
        return new ErrorResponse("Error de ejecución ", 0);
    }

    public ErrorResponse(String error, int code){
        this.statusMessage = error + " Codigo error: " + code;
    }

    public ErrorResponse(String statusMessage, Integer statusCode) {
        this.statusMessage = statusMessage;
        this.statusCode = statusCode;
    }

    public static ErrorResponse response(String response){
        ErrorResponse errorResponse;
        Log.i(TAG, "response: " + response);
        try{
            errorResponse = SERIALIZED_GSON.fromJson(response, ErrorResponse.class);
        }catch (Exception e){
//            e.printStackTrace();
            errorResponse = new ErrorResponse("Ha ocurrido un error en el servidor.", 0);
        }
        return errorResponse;
    }

    
    public static ErrorResponse response(Response response){
        Log.i(TAG, "response: constructor init");
        if(response.code() >= 500){
            Log.i(TAG, "response: server error");
            return new ErrorResponse("Ha ocurrido un error en el servidor.", response.code());
        }
        try {
            Log.i(TAG, "response: try to get string from error body");
            String s = response.errorBody().string();
            Log.i(TAG, "response: successful get string from error body");
            return ErrorResponse.response(s);
        } catch (IOException e) {
            Log.i(TAG, "response: failed to get string from error body");
            e.printStackTrace();
            return new ErrorResponse("Ha ocurrido un error en el servidor.", response.code());
        }

    }

    private String getError(){
        return statusMessage.equals("") ? null : statusMessage;
    }

    @Override
    public String toString() {
        return getError();
    }
}
