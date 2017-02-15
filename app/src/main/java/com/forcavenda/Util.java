package com.forcavenda;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Leo on 14/02/2017.
 */

public class Util {

    public static boolean estaConectadoInternet(Context context) {
        Boolean conectado = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }


}
