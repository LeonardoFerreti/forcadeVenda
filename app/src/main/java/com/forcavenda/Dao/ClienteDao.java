package com.forcavenda.Dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 09/02/2017.
 */

public class ClienteDao {

    public void associaClienteUsuario(String chave,final Map<String, Object> cliente ){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> objDao = new HashMap<>();
        objDao.put("cliente/" + chave, cliente);
        ref.updateChildren(objDao);
    }


    public void IncluirAlterar(final Context context, String chave, final Map<String, Object> cliente, final String texto) {
        //recupera a raiz do nó do banco de dados
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        //Declara uma variavel HasMap, que vai ser utilizada para atualizar o banco do firebase.
        final Map<String, Object> objDao = new HashMap<>();
        //Adiciona ao objDao o caminho do cliente a ser incluido/alterado, juntamente com seu objeto mapeado
        objDao.put("cliente/" + chave, cliente);

        //Adiciona uma referencia ao nó de relacionamento cliente/pedido, adicionando ao objDao
        // todos os caminhos dos pedidos que possuem o cliente que está sendo alterado.
        final DatabaseReference refClientePedido = ref.child("clientePedido").child(chave);
        //Adiciona um evento de escuta para cada item encontrado do relacionamento
        refClientePedido.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //adiciona o caminho do pedido que utiliza o cliente informado
                objDao.put("pedido/" + dataSnapshot.getKey().toString() + "/cliente/", cliente);
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
       //Acionado quando todas as referencias do cliente foram adicionadas ao ObjDao, fazer o update ...
        refClientePedido.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ao final das consultas, atualiza todos os caminhos informados com o objeto cliente alterado.
                ref.updateChildren(objDao, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(context, texto, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Erro ao atualizar o cliente: " + databaseError.getMessage().toString(), Toast.LENGTH_LONG).show();
                            Log.i("Erro", databaseError.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}
