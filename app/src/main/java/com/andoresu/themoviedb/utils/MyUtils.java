package com.andoresu.themoviedb.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.text.Html;
import android.text.SpannedString;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.andoresu.themoviedb.R;
import com.andoresu.themoviedb.client.ErrorResponse;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@SuppressLint("LogNotTimber")
public class MyUtils {

    private static final String TAG = "THEMOVIEDB_" + MyUtils.class.getSimpleName();

    public static CharSequence getText(Context context, int id, Object... args) {
        for(int i = 0; i < args.length; ++i)
            args[i] = args[i] instanceof String? TextUtils.htmlEncode((String)args[i]) : args[i];
        return removeTrailingLineFeed(Html.fromHtml(String.format(Html.toHtml(new SpannedString(context.getText(id))), args)));
    }

    public static boolean checkNullEmpty(String s) {
        return null == s || s.trim().isEmpty();
    }

    public static boolean checkNumeric(String s) {
        Log.i(TAG, "checkNumeric: " + s + " isNumeric? " + s.matches("\\d"));
        return s.matches("\\d+");
    }

    public static boolean checkLength(String s, int length) {
        return s.length() == length;
    }

    public static boolean checkOnlyLetters(String s, boolean onlyUpperCase) {
        String regex = onlyUpperCase ? "[A-Z]+" : "[a-zA-Z]+";
        return s.matches(regex);
    }

    public static String saveImage(Context context, Bitmap image, String fileName) {
        try{
            fileName = fileName.replaceAll("\\/","");
            Log.i(TAG, "saveImage: filename: " + fileName);
            File file = new File(context.getFilesDir() + File.separator + "movie_" + fileName);
            FileOutputStream fos = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String saveImage(Bitmap image, String fileName) {
        fileName = fileName.replaceAll("\\\\", "");
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File file = new File(path, fileName);
        try{
            if(path.mkdirs()){
                OutputStream fOut = new FileOutputStream(file);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            }
            return file.getAbsolutePath();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
//        String savedImagePath = null;
//        fileName = fileName.replaceAll("\\\\", "");
//        String imageFileName = "JPEG_" + fileName;
//        File storageDir = new File(
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//                        + "/Comicoid");
//        boolean success = true;
//        if (!storageDir.exists()) {
//            success = storageDir.mkdirs();
//        }
//        if (success) {
//            File imageFile = new File(storageDir, imageFileName);
//            savedImagePath = imageFile.getAbsolutePath();
//            try {
//                OutputStream fOut = new FileOutputStream(imageFile);
//                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//                fOut.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return savedImagePath;
    }

//    private static void galleryAddPic(String imagePath) {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(imagePath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        context.sendBroadcast(mediaScanIntent);
//    }


    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    public static void goToWebsite(Context context, String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    @SuppressLint("CheckResult")
    public static RequestOptions glideRequestOptions(Context context){

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loading_image);
        requestOptions.error(R.drawable.imagen_disponible);

        return requestOptions;
    }

    @SuppressLint("CheckResult")
    public static RequestOptions glideRequestOptions(boolean circle){

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loading_image);
        requestOptions.error(R.drawable.imagen_disponible);
        if(circle){
            requestOptions.circleCrop()
            .autoClone();
        }
        return requestOptions;
    }

    public static void showDialog(@NonNull Context context, @NonNull String title, @NonNull String message, Drawable icon, @ColorRes Integer iconColor) {
        showDialog(context, title, message, icon, iconColor, null, null);
    }

    public static void showDialog(@NonNull Context context, @NonNull String title, @NonNull String message, Drawable icon, @ColorRes Integer iconColor,
                                  DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        String positiveText = context.getString(R.string.error_dialog_ok);
        String negativeText = context.getString(R.string.cancel);
        showDialog(context, title, message, icon, iconColor, positiveListener, positiveText, negativeListener, negativeText);
    }

    public static void showDialog(@NonNull Context context, @NonNull String title, @NonNull String message, Drawable icon, @ColorRes Integer iconColor,
                                  DialogInterface.OnClickListener positiveListener, @StringRes Integer positiveStringRes, DialogInterface.OnClickListener negativeListener, @StringRes Integer negativeStringRes) {
        String positiveText = context.getString(positiveStringRes == null ? R.string.error_dialog_ok : positiveStringRes);
        String negativeText = context.getString(negativeStringRes == null ? R.string.cancel : negativeStringRes);
        showDialog(context, title, message, icon, iconColor, positiveListener, positiveText, negativeListener, negativeText);
    }

    public static void showDialog(@NonNull Context context, @NonNull String title, @NonNull String message, Drawable icon, @ColorRes Integer iconColor,
                                  DialogInterface.OnClickListener positiveListener, String positiveText, DialogInterface.OnClickListener negativeListener, String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message);

        DialogInterface.OnClickListener defaultListener = (dialogInterface, i) -> dialogInterface.dismiss();
        String defaultPositiveText = context.getString(R.string.error_dialog_ok);
        String defaultNegativeText = context.getString(R.string.cancel);

        if (positiveListener == null) {
            positiveListener = defaultListener;
        }
        if (positiveText == null) {
            positiveText = defaultPositiveText;
        }
        if (negativeListener == null) {
            negativeListener = defaultListener;
        }
        if (negativeText == null) {
            negativeText = defaultNegativeText;
        }
        builder.setPositiveButton(positiveText, positiveListener);
        builder.setNegativeButton(negativeText, negativeListener);

        if (icon != null) {
            if (iconColor != null) {
                icon = DrawableCompat.wrap(icon);
                DrawableCompat.setTint(icon, context.getResources().getColor(iconColor));
            }
            builder.setIcon(icon);
        }

        builder.show();
    }

    public static void showDialog(@NonNull Context context, @NonNull String title, @NonNull String message, Integer icon, @ColorRes Integer iconColor,
                                  DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        Drawable i = context.getDrawable(icon);
        showDialog(context, title, message, i, iconColor, positiveListener, negativeListener);
    }

    public static void showDialog(@NonNull Context context, @StringRes int title, @StringRes int message, @DrawableRes Integer icon, @ColorRes Integer iconColor) {
        String t = context.getString(title);
        String m = context.getString(message);
        Drawable i = context.getDrawable(icon);
        showDialog(context, t, m, i, iconColor);
    }

    public static void showDialog(@NonNull Context context, @StringRes int title, @NonNull String message, @DrawableRes Integer icon, @ColorRes Integer iconColor) {
        String t = context.getString(title);
        Drawable i = context.getDrawable(icon);
        showDialog(context, t, message, i, iconColor);
    }

    public static void showDialog(@NonNull Context context, @StringRes int title, @NonNull String message, @DrawableRes Integer icon, @ColorRes Integer iconColor,
                                  DialogInterface.OnClickListener positiveListener) {
        String t = context.getString(title);
        Drawable i = context.getDrawable(icon);
        showDialog(context, t, message, i, iconColor, positiveListener, null);
    }

    public static void showDialog(@NonNull Context context, @StringRes int title, @NonNull String message, @DrawableRes Integer icon, @ColorRes Integer iconColor,
                                  DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        String t = context.getString(title);
        Drawable i = context.getDrawable(icon);
        showDialog(context, t, message, i, iconColor, positiveListener, negativeListener);
    }

    public static void showDialog(@NonNull Context context, @StringRes int title, @StringRes int message, @DrawableRes Integer icon) {
        String t = context.getString(title);
        String m = context.getString(message);
        Drawable i = context.getDrawable(icon);
        showDialog(context, t, m, i, null);
    }

    public static void showDialog(@NonNull Context context, @StringRes int title, @NonNull String message, @DrawableRes Integer icon) {
        String t = context.getString(title);
        Drawable i = context.getDrawable(icon);
        showDialog(context, t, message, i, null);
    }

    public static void showErrorDialog(@NonNull Context context, String error) {
        showDialog(context, R.string.error_dialog_title, error, R.drawable.ic_error, R.color.colorAlertError);
    }

    public static void showErrorDialog(@NonNull Context context, String[] errors) {
        StringBuilder error = new StringBuilder();
        for (String e : errors) {
            error.append(e).append("\n");
        }
        showDialog(context, R.string.error_dialog_title, error.toString(), R.drawable.ic_error, R.color.colorAlertError);
    }

    public static void showErrorDialog(@NonNull Context context, String error, DialogInterface.OnClickListener positiveListener) {
        showDialog(context, R.string.error_dialog_title, error, R.drawable.ic_error, R.color.colorAlertError, positiveListener);
    }

    public static void showErrorDialog(@NonNull Context context, String title, String error, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        showDialog(context, title, error, R.drawable.ic_error, R.color.colorAlertError, positiveListener, negativeListener);
    }

    public static void showErrorDialog(@NonNull Context context, String title, String error, DialogInterface.OnClickListener positiveListener) {
        showDialog(context, title, error, R.drawable.ic_error, R.color.colorAlertError, positiveListener, null);
    }

    public static void showErrorDialog(@NonNull Context context, String error, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        showDialog(context, R.string.error_dialog_title, error, R.drawable.ic_error, R.color.colorAlertError, positiveListener, negativeListener);
    }

    public static void showErrorDialog(@NonNull Context context, String error, DialogInterface.OnClickListener positiveListener, String positiveText, DialogInterface.OnClickListener negativeListener, String negativeText) {
        String t = context.getString(R.string.error_dialog_title);
        Drawable i = context.getDrawable(R.drawable.ic_error);
        showDialog(context, t, error, i, R.color.colorAlertError, positiveListener, positiveText, negativeListener, negativeText);
    }

    public static void showErrorDialog(@NonNull Context context, ErrorResponse errorResponse) {
        showErrorDialog(context, errorResponse.toString());
    }

    public static void showSuccessDialog(@NonNull Context context, String title, String msg) {
        showDialog(context, title, msg, context.getDrawable(R.drawable.ic_check), R.color.colorAccent);
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    public static String fillSpaces(char fillChar, int length, String s) {
        if (s.length() >= length) {
            return s;
        } else {
            int spaces = length - s.length();
            return s + new String(new char[spaces]).replace('\0', fillChar);
        }
    }

    public static void closeKeyboard(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static ButterKnife.Action<View> REMOVE_ERRORS = (view, index) -> {
        if (view instanceof EditText) {
            ((EditText) view).setError(null);
        } else if (view instanceof TextInputLayout) {
            ((TextInputLayout) view).setError(null);
        }

    };

    public static ButterKnife.Action<View> TOGGLE_VIEW_ENABLE_STATE = (view, index) -> {
        if(view.isEnabled()){
            view.setEnabled(false);
        } else {
            view .setEnabled(true);
        }
    };

    public static ButterKnife.Action<View> ENABLE_VIEW = (view, index) -> {
        view .setEnabled(true);
    };

    public static ButterKnife.Action<View> DISABLE_VIEW = (view, index) -> {
        view.setEnabled(false);
    };

    public static ButterKnife.Action<View> HIDE_VIEW = (view, index) -> {
        view.setVisibility(View.GONE);
    };
    public static ButterKnife.Action<View> SHOW_VIEW = (view, index) -> {
        view.setVisibility(View.VISIBLE);
    };

    public static boolean isNightMode(){
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
    }

    public static String getMetadata(Context context, String name) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                return appInfo.metaData.getString(name);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String formatTime(Integer secs) {
        if(secs == null){
            return "00:00:00";
        }
        return String.format("%02d:%02d:%02d", (secs % 86400) / 3600, (secs % 3600) / 60, secs % 60);
    }

    public static String TIME_REGEX = "^\\d\\d:\\d\\d";


    public static boolean checkTimeInterval(String time, String startTime, String endTime){

        Log.i(TAG, "checkTimeInterval: time " + time);
        Log.i(TAG, "checkTimeInterval: startTime " + startTime);
        Log.i(TAG, "checkTimeInterval: endTime " + endTime);

        if(!time.matches(TIME_REGEX)){
            Log.e(TAG, "checkTimeInterval: time its not a time");
            return false;
        }
        if(!startTime.matches(TIME_REGEX)){
            Log.e(TAG, "checkTimeInterval: startTime its not a time");
            return false;
        }
        if(!endTime.matches(TIME_REGEX)){
            Log.e(TAG, "checkTimeInterval: endTime its not a time");
            return false;
        }

        int timeHour = Integer.parseInt(time.split(":")[0]);
        int timeMinutes = Integer.parseInt(time.split(":")[1]);

        int startTimeHour = Integer.parseInt(startTime.split(":")[0]);
        int startTimeMinutes = Integer.parseInt(startTime.split(":")[1]);

        int endTimeHour = Integer.parseInt(endTime.split(":")[0]);
        int endTimeMinutes = Integer.parseInt(endTime.split(":")[1]);

        Log.i(TAG, "checkTimeInterval: timeHour  " + timeHour );
        Log.i(TAG, "checkTimeInterval: timeMinutes  " + timeMinutes );

        Log.i(TAG, "checkTimeInterval: startTimeHour  " + startTimeHour );
        Log.i(TAG, "checkTimeInterval: startTimeMinutes  " + startTimeMinutes );

        Log.i(TAG, "checkTimeInterval: endTimeHour  " + endTimeHour );
        Log.i(TAG, "checkTimeInterval: endTimeMinutes  " + endTimeMinutes );

        if(compareTimes(startTime, endTime) > 0){
            if(timeHour == startTimeHour){
                return timeMinutes >= startTimeMinutes;
            }else if(timeHour == endTimeHour){
                return timeMinutes <= endTimeMinutes;
            }else{
                return (timeHour >= startTimeHour && timeHour <= 23) || (timeHour >= 0 && timeHour <= endTimeHour);
            }
        }else{
            if(timeHour == startTimeHour){
                return timeMinutes >= startTimeMinutes;
            }else if(timeHour == endTimeHour){
                return timeMinutes <= endTimeMinutes;
            }else{
                return timeHour >= startTimeHour && timeHour <= endTimeHour;
            }
        }

    }

    public static boolean checkTimeInterval(Calendar calendar, String startTime, String endTime){

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String hourString = (hour < 10 ? "0" : "") + hour;
        String minuteString = (minute < 10 ? "0" : "") + minute;
        String time = hourString + ":" + minuteString;
        return checkTimeInterval(time, startTime, endTime);

    }

    public static int compareTimes(String time1, String time2){

        int timeHour1 = Integer.parseInt(time1.split(":")[0]);
        int timeMinutes1 = Integer.parseInt(time1.split(":")[1]);

        int timeHour2 = Integer.parseInt(time2.split(":")[0]);
        int timeMinutes2 = Integer.parseInt(time2.split(":")[1]);

        if(timeHour1 == timeHour2){
            if(timeMinutes1 == timeMinutes2){
                return 0;
            }else if(timeMinutes1 < timeMinutes2){
                return -1;
            }else{
                return 1;
            }
        }else if(timeHour1 < timeHour2){
            return -1;
        }else {
            return 1;
        }

    }

    @SuppressLint("DefaultLocale")
    public static String toMoney(double price){
        return toMoney(price, "$");
    }

    @SuppressLint("DefaultLocale")
    public static String toMoney(double price, String currency){
        return (currency + " " + String.format("%,.2f", price)).replaceAll(",00", "");
    }

    public static <T> T getFirst(List<T> list){
        try {
            return list.get(0);
        }catch (Exception e){
            return null;
        }
    }

    public static Float parseFloat(String s){
        try{
            return Float.parseFloat(s);
        }catch (Exception e){ return null; }
    }

    public static Integer parseInt(String s){
        try{
            return Integer.parseInt(s);
        }catch (Exception e){ return null; }
    }

    public static Intent phoneIntent(String number){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        return intent;
    }

    public static Intent galleryIntent(){
        return new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    public static Bitmap getBitmapFromCameraData(Intent data, Context context){
        Uri selectedImage = data.getData();
        String picturePath = getPathFromURI(selectedImage, context);
        if (picturePath != null) {
            return BitmapFactory.decodeFile(picturePath);
        }
        return null;
    }

    public static String getPathFromURI(Uri contentUri, Context context) {
        Log.i(TAG, "getPathFromURI: 1");
        if(context == null || contentUri == null){
            return null;
        }
        Log.i(TAG, "getPathFromURI: 2");
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, filePathColumn, null, null, null);
        Log.i(TAG, "getPathFromURI: 2");
        if(cursor != null){
            String res = "";
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                 res = cursor.getString(column_index);
                Log.i(TAG, "getPathFromURI: 3");
            }
            cursor.close();
            Log.i(TAG, "getPathFromURI: path " + res);
            Log.i(TAG, "getPathFromURI: 4");
            return res;
        }
        Log.i(TAG, "getPathFromURI: 5");
        return null;
    }

    public static MultipartBody.Part prepareFilePart(String partName, Uri fileUri, Context context) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = new File(getPathFromURI(fileUri, context));

        // create RequestBody instance from file
        RequestBody requestFile = requestBodyFile(fileUri, context);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public static RequestBody requestBodyFile(Uri fileUri, Context context){
        File file = new File(getPathFromURI(fileUri, context));
        return RequestBody.create(
                MediaType.parse(context.getContentResolver().getType(fileUri)),
                file
        );
    }


    public static Bitmap drawableToBitmap(int drawable, Resources res, int color){
        Bitmap bitmap = BitmapFactory.decodeResource(res, drawable);
        Bitmap resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth() - 1, bitmap.getHeight() - 1);

        ColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);

        Paint markerPaint = new Paint();
        markerPaint.setColorFilter(filter);

        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, markerPaint);

        return resultBitmap;

    }



    public static Bitmap drawableToBitmap(int drawable, Resources res){
        return drawableToBitmap(drawable, res, res.getColor(R.color.colorAccent));
    }

    public static CharSequence removeTrailingLineFeed(@NonNull CharSequence text) {
        while (text.charAt(text.length() - 1) == '\n') {
            text = text.subSequence(0, text.length() - 1);
        }
        return text;
    }

    public static boolean checkPermissions(Context context){
        boolean res = true;
        for(String per : Constants.permissions){
            if (ContextCompat.checkSelfPermission(context, per) != PackageManager.PERMISSION_GRANTED){
                res = false;
                break;
            }
        }
        return res;
    }

    public static void requestPermissions(Activity activity){
        Log.i(TAG, "requestPermissions: checking");
        ActivityCompat.requestPermissions(activity, Constants.permissions, Constants.REQUEST_PERMISSIONS);
        Log.i(TAG, "requestPermissions: done");
    }

}
