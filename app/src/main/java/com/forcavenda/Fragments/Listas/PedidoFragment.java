package com.forcavenda.Fragments.Listas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forcavenda.Entidades.Pedido;
import com.forcavenda.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 03/03/2017.
 */
public class PedidoFragment extends Fragment {
    List<Pedido> listaProdutos = new ArrayList<Pedido>();

    public PedidoFragment() {
    }

    public static PedidoFragment newInstance() {
        PedidoFragment fragment = new PedidoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lista, container, false);


        return view;
    }
}
