package com.forcavenda.Dao;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.forcavenda.Entidades.Pedido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

/**
 * Created by Leo on 09/02/2017.
 */

public class PedidoDao {
    private Context context;
    private Pedido pedido_insupd;

    public PedidoDao(Context context, Pedido pedido) {
        this.context = context;
        pedido_insupd = pedido;
    }

    public DatabaseReference IncluirNoRegistro(DatabaseReference ref, String chave, Map<String, Object> pedido) {
        Map<String, Object> objDao = new HashMap<>();
        objDao.put("pedido/" + chave, pedido);
        ref.updateChildren(objDao);
        DatabaseReference refNovoPedido = ref.child("pedido").child(chave);
        return refNovoPedido;
    }

    public DatabaseReference Incluir(DatabaseReference ref, Long id, Map<String, Object> pedido) {
        Map<String, Object> objDao = new HashMap<>();
        objDao.put("pedido/" + id, pedido);
        ref.updateChildren(objDao);
        DatabaseReference refNovoPedido = ref.child("pedido").child(String.valueOf(id));
        return refNovoPedido;
    }

    public Long novoIdentificador = Long.valueOf(0);

    public void IncluirIdPedido() {

        //Recupera a instancia do Banco de dados da aplicação
        final FirebaseDatabase banco = FirebaseDatabase.getInstance();

        DatabaseReference refContadorPedido = banco.getReference("pedido").child("contadorPedido");

        refContadorPedido.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                if (currentData.getValue() != null) {
                    novoIdentificador = (Long) currentData.getValue() + 1;
                    banco.getReference("pedido").child(pedido_insupd.getChave()).child("idPedido").setValue(novoIdentificador);
                    currentData.setValue(novoIdentificador);
                } else {
                    currentData.setValue(1);
                    novoIdentificador = (Long) currentData.getValue();
                }

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (b) {
                    Toast.makeText(context, "Pedido incluído " + String.valueOf(novoIdentificador) + " com sucesso.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Erro ao incluir pedido: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}