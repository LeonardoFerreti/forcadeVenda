package com.forcavenda.Dao;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.forcavenda.Entidades.Pedido;
import com.forcavenda.Enums.Status;
import com.forcavenda.Util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.HashMap;
import java.util.Map;

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

    public void alteraStatus(Status status) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("pedido").child(pedido_insupd.getChave()).child("status").setValue(status.toString());

        Toast.makeText(context, "Pedido " + String.valueOf(pedido_insupd.getIdPedido()) + " alterado com sucesso.", Toast.LENGTH_SHORT).show();

    }

    public Long novoIdentificador = Long.valueOf(0);

    public void IncluirIdPedido(final Context context) {

        final ProgressDialog progressDialog = Util.CriaProgressDialog(context);
        progressDialog.show();

        //Recupera a instancia do Banco de dados da aplicação
        final FirebaseDatabase banco = FirebaseDatabase.getInstance();
        //Recupera a referencia do contador do numero de pedidos
        DatabaseReference refContadorPedido = banco.getReference("contadores").child("contadorPedido");
        //Começa uma transação dentro do nó de contador do pedido.
        refContadorPedido.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                //Se não houver registros atualmente no nó, então o primeiro registro será o 1.
                if (currentData.getValue() == null) {
                    novoIdentificador = Long.valueOf(1);
                } else {
                    //caso haja registro, consulta e adiciona 1 ao contador
                    novoIdentificador = (Long) currentData.getValue() + 1;
                }
                //Atualiza o valor do contador no nó
                currentData.setValue(novoIdentificador);
                //recupera a referencia do pedido criado e atualiza seu identificador
                banco.getReference("pedido").child(pedido_insupd.getChave()).child("idPedido").setValue(novoIdentificador);

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (b) {
                    Toast.makeText(context, "Pedido incluído " + String.valueOf(novoIdentificador) + " com sucesso.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Erro ao incluir pedido: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });
    }
}