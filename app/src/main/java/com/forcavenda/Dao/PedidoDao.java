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
    //Contexto da inclusão do pedido
    private Context context;
    //pedido a ser incluido
    private Pedido pedido_insupd;
    //Identificador do pedido a ser incluido
    private Long novoIdentificador = 0L;

    //Construtor com os parametros da classe
    public PedidoDao(Context context, Pedido pedido) {
        this.context = context;
        pedido_insupd = pedido;
    }

    //Rotina que inclui um pedido no registro do firebase
    public DatabaseReference IncluirNoRegistro(DatabaseReference ref, String chave, Map<String, Object> pedido) {
        Map<String, Object> objDao = new HashMap<>();
        objDao.put("pedido/" + chave, pedido);
        ref.updateChildren(objDao);
        return ref.child("pedido").child(chave);
    }

    //rotina que altera um status de um pedido
    public void alteraStatus(Status status) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("pedido").child(pedido_insupd.getChave()).child("status").setValue(status.toString());
        Toast.makeText(context, "Pedido " + String.valueOf(pedido_insupd.getIdPedido()) + " alterado com sucesso.", Toast.LENGTH_SHORT).show();
    }


    //Rotina que inclui o identificador do pedido
    public void IncluirIdPedido(final Context context) {
        //Chama um progressDialog enquanto estiver rodando a rotina
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
                    novoIdentificador = 1L;
                } else {
                    //caso haja registro, consulta e adiciona 1 ao contador
                    novoIdentificador = (Long) currentData.getValue() + 1;
                }
                //Atualiza o valor do contador no nó
                currentData.setValue(novoIdentificador);
                //recupera a referencia do pedido criado e atualiza seu identificador
                banco.getReference("pedido").child(pedido_insupd.getChave()).child("idPedido").setValue(novoIdentificador);
                //retorna o sucesso da operação
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