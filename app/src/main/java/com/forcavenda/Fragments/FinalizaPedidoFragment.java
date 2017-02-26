package com.forcavenda.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.forcavenda.Adapters.SpinnerFormaPgtoAdapter;
import com.forcavenda.Entidades.FormaPgto;
import com.forcavenda.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 26/02/2017.
 */

public class FinalizaPedidoFragment extends Fragment {
    List<FormaPgto> listaFormaPgto = new ArrayList<FormaPgto>();
    Spinner cboFormaPgto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finaliza_pedido, container, false);

        cboFormaPgto = (Spinner) view.findViewById(R.id.cboFormapgto);

        final FirebaseDatabase banco = FirebaseDatabase.getInstance();
        DatabaseReference tabFormaPgto = banco.getReference("formaPgto");
        Query resultadoFormaPgto = tabFormaPgto.orderByChild("nome");
        final SpinnerFormaPgtoAdapter adapterFormaPgto = new SpinnerFormaPgtoAdapter(getActivity().getApplicationContext(), listaFormaPgto);
        cboFormaPgto.setAdapter(adapterFormaPgto);
        
        resultadoFormaPgto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        resultadoFormaPgto.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                FormaPgto formaPgto = snapshot.getValue(FormaPgto.class);
                listaFormaPgto.add(formaPgto);
                adapterFormaPgto.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        return view;
    }
}
