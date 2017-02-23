package com.forcavenda.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.forcavenda.R;

/**
 * Created by Leo on 23/02/2017.
 */

public class PerfilFragment extends Fragment {


    public PerfilFragment() {
    }

    public static PerfilFragment newInstance() {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);


        ImageView imgPerfil = (ImageView) view.findViewById(R.id.img_Perfil);
        EditText txtNome = (EditText) view.findViewById(R.id.txt_nome);

       // imgPerfil.setImageURI();

        return  view;
    }
}
