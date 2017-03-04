package com.forcavenda.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.WindowManager;

import com.forcavenda.R;

/**
 * Created by Leo on 14/02/2017.
 */

public class Util {

    public static final String PREFERENCIA = "PREFERENCIA";
    public static final String chaveCliente = "chaveCliente";
    public static final String isAdmin = "isAdmin";

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

    public static ProgressDialog CriaProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Carregando dados...");
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {
            Log.i("Erro: ", e.getMessage().toString());
        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progress_dialog);
        return dialog;
    }

}
