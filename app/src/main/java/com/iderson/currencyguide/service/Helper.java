package com.iderson.currencyguide.service;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.iderson.currencyguide.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Helper {

    private static boolean DISPLAY_DEBUG = true;

    public static void noConnection(final Context context) {

        AlertDialog.Builder ab = null;
        ab = new AlertDialog.Builder(context);

        ab.setMessage(context.getResources().getString(R.string.dialog_internet_description));
        ab.setPositiveButton(context.getResources().getString(R.string.ok), null);
        ab.setTitle(context.getResources().getString(R.string.dialog_internet_title));


        ab.show();
    }


    public static boolean isOnline(Context c, boolean calledFromFragment, boolean showDialog) {
        ConnectivityManager cm = (ConnectivityManager)
                c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni != null && ni.isConnected())
            return true;
        else if (showDialog) {
            noConnection(c);
        }
        return false;
    }

    public static void admobLoader(Context c, Resources resources, View AdmobView) {
        String adId = resources.getString(R.string.ad_id);
        if (adId != null && !adId.equals("")) {
            AdView adView = (AdView) AdmobView;
            adView.setVisibility(View.VISIBLE);

            // Look up the AdView as a resource and load a request.
            Builder adRequestBuilder = new AdRequest.Builder();
            adView.loadAd(adRequestBuilder.build());
        }
    }

    @SuppressLint("NewApi")
    public static void revealView(View toBeRevealed, View frame) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                // get the center for the clipping circle
                int cx = (frame.getLeft() + frame.getRight()) / 2;
                int cy = (frame.getTop() + frame.getBottom()) / 2;

                // get the final radius for the clipping circle
                int finalRadius = Math.max(frame.getWidth(), frame.getHeight());

                // create the animator for this view (the start radius is zero)
                Animator anim = ViewAnimationUtils.createCircularReveal(
                        toBeRevealed, cx, cy, 0, finalRadius);

                // make the view visible and start the animation
                toBeRevealed.setVisibility(View.VISIBLE);
                anim.start();
            } else {
                toBeRevealed.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    public static void setStatusBarColor(Activity mActivity, int color) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                mActivity.getWindow().setStatusBarColor(color);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Makes high numbers readable (e.g. 5000 -> 5K)
    public static String formatValue(double value) {
        if (value > 0) {
            int power;
            String suffix = " kmbt";
            String formattedNumber = "";

            NumberFormat formatter = new DecimalFormat("#,###.#");
            power = (int) StrictMath.log10(value);
            value = value / (Math.pow(10, (power / 3) * 3));
            formattedNumber = formatter.format(value);
            formattedNumber = formattedNumber + suffix.charAt(power / 3);
            return formattedNumber.length() > 4 ? formattedNumber.replaceAll("\\.[0-9]+", "") : formattedNumber;
        } else {
            return "0";
        }
    }

}
