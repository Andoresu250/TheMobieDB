package com.andoresu.themoviedb.utils;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannedString;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;


import com.andoresu.themoviedb.R;
import com.andoresu.themoviedb.client.ErrorResponse;

import static com.andoresu.themoviedb.utils.Constants.REQUEST_PERMISSIONS;
import static com.andoresu.themoviedb.utils.MyUtils.checkPermissions;
import static com.andoresu.themoviedb.utils.MyUtils.removeTrailingLineFeed;
import static com.andoresu.themoviedb.utils.MyUtils.showErrorDialog;


@SuppressLint({"Registered", "LogNotTimber"})
public class BaseActivity extends AppCompatActivity implements BaseView{


    private String TAG = "THEMOVIEDB_" + BaseActivity.class.getSimpleName();

    private Bundle mainBundle;

    public int MENU_GROUP_ID = 0;

    public final int ITEM_MENU_LOGOUT_ID = 1;

    public final int ITEM_MENU_DOWNLOAD_MAP = 2;

    private boolean bound = false;

    private Class<?> childClass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

//        if (isNightMode()){
//            setTheme(R.style.NightAppTheme);
//        }else{
//            setTheme(R.style.AppTheme);
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestActivityPermissions();
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void restart(Class<?> cls, Bundle params){
        Intent intent = new Intent(this, cls);
        if(params != null){
            intent.putExtras(params);
        }
        startActivity(intent);
        finish();
    }

    public void setMainBundle(Bundle bundle){
        this.mainBundle = bundle;
    }


    public CharSequence getText(int id, Object... args) {
        for(int i = 0; i < args.length; ++i)
            args[i] = args[i] instanceof String? TextUtils.htmlEncode((String)args[i]) : args[i];
        return removeTrailingLineFeed(Html.fromHtml(String.format(Html.toHtml(new SpannedString(getText(id))), args)));
    }

    public void requestActivityPermissions(){
        boolean havePermissions = checkPermissions(this);
        if (!havePermissions) {
            MyUtils.requestPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSIONS){
            boolean havePermissions = checkPermissions(this);
            if(!havePermissions){
                showErrorDialog(this, "Necesitas todos los permisos para poder usar la aplicacion",
                        (dialogInterface, i) -> requestActivityPermissions(),
                        "Try Again",
                        (dialogInterface, i) -> BaseActivity.this.finish(),
                        "Close App");
            }
        }
    }

    @Override
    public void showProgressIndicator(boolean active) {

    }

    @Override
    public void showGlobalError(ErrorResponse errorResponse) {

    }

    @Override
    public void showMessage(String msg) {

    }

    public interface ServiceConnectionListener{

        void onServiceConnected();

        void onServiceDisconnected();
    }

}