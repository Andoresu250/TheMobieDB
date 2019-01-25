package com.andoresu.themoviedb.utils;


import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.SpannedString;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.andoresu.themoviedb.R;

import butterknife.Unbinder;

import static com.andoresu.themoviedb.utils.MyUtils.isNightMode;
import static com.andoresu.themoviedb.utils.MyUtils.removeTrailingLineFeed;

@SuppressLint("LogNotTimber")
public class BaseDialogFragment extends DialogFragment {

    private static String TAG = "THEMOVIEDB_" + BaseDialogFragment.class.getSimpleName();

    private Integer title;

    private Unbinder unbinder;

    private View view;

    public Button positiveBtn;

    public Button negativeBtn;

    public Unbinder getUnbinder() {
        return unbinder;
    }

//    public String tag;

    public void setUnbinder(Unbinder unbinder) {
        this.unbinder = unbinder;
    }

    public Integer getTitle() {
        return title;
    }

    public void setTitle(Integer title) {
        this.title = title;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        positiveBtn = ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);
        negativeBtn = ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE);
        if(isNightMode()){
            getDialog().getWindow().setBackgroundDrawableResource(R.color.colorCardBackgroundNight);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void show(FragmentTransaction fragmentTransaction){
        try{
            Log.i(TAG, "show: tag: " + this.getClass().getSimpleName());
            show(fragmentTransaction, "THEMOVIEDB_" + this.getClass().getSimpleName());
//            show(fragmentTransaction, tag == null ? this.getClass().getSimpleName() : tag);
        }catch (Exception e){e.printStackTrace();}
    }


    public static Fragment getInstancedFragment(FragmentManager fragmentManager, String tag){
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if(fragment == null){
            Log.e(TAG, "getInstancedFragment: fragment with tag: " + tag + " not found");
        }
        return fragment;
    }

    public void removeIfOpened(FragmentManager fragmentManager, FragmentTransaction fragmentTransaction){
        Fragment prev = getInstancedFragment(fragmentManager, this.getClass().getName());
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
    }

    public void showButtons(){
        if(positiveBtn != null){
            positiveBtn.setVisibility(View.VISIBLE);
        }
        if(negativeBtn != null){
            negativeBtn.setVisibility(View.VISIBLE);
        }
    }

    public void hideButtons(){
        if(positiveBtn != null){
            positiveBtn.setVisibility(View.GONE);
        }
        if(negativeBtn != null){
            negativeBtn.setVisibility(View.GONE);
        }
    }

    public void setDialogSize(Double widthPercentage, Double heightPercentage){

        Window window = getDialog().getWindow();
        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        int width = widthPercentage != null ? (int) (size.x * widthPercentage) : WindowManager.LayoutParams.WRAP_CONTENT;
        int height = heightPercentage != null ? (int) (size.y * heightPercentage) : WindowManager.LayoutParams.WRAP_CONTENT;

        window.setLayout(width, height);
        window.setGravity(Gravity.CENTER);

    }

    public AlertDialog.Builder getBuilder(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if(title == null){
            return builder;
        }

        builder.setTitle(title);


        return builder;
    }

    public CharSequence getText(int id, Object... args) {
        for(int i = 0; i < args.length; ++i)
            args[i] = args[i] instanceof String? TextUtils.htmlEncode((String)args[i]) : args[i];
        return removeTrailingLineFeed(Html.fromHtml(String.format(Html.toHtml(new SpannedString(getText(id))), args)));
    }
}
