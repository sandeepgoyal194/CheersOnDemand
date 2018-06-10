package com.cheersondemand.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cheersondemand.R;

import java.util.UUID;

/**
 * Created by GAURAV on 5/23/2018.
 */

public class Util {
    Snackbar snackbar;

    public static boolean isNetworkConnectivity(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public void setSnackbarMessage(Activity activity, String status, View view, boolean isError) {


        snackbar = Snackbar
                .make(view, status, Snackbar.LENGTH_LONG)
                .setAction("X", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
        // Changing message text color
        snackbar.setActionTextColor(ContextCompat.getColor(activity, R.color.cross_color));
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        if(isError) {
            textView.setTextColor(ContextCompat.getColor(activity, R.color.error_color));
        }
        else {
            textView.setTextColor(ContextCompat.getColor(activity, R.color.cross_color));

        }
        sbView.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));

        snackbar.show();


    }
    public static boolean isValidMail(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public static boolean isValidPhone(CharSequence phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(phone).matches();
        }
    }
    private static String uniqueID = null;
    public synchronized static String id(Context context) {
        if (uniqueID == null) {

            uniqueID = SharedPreference.getInstance(context).getString(C.PREF_UNIQUE_ID);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreference.getInstance(context).setString(C.PREF_UNIQUE_ID, uniqueID);
            }
        }
        return uniqueID;
    }
}
