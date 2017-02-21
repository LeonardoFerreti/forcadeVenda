package com.forcavenda.Telas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.forcavenda.Dao.ClienteDao;
import com.forcavenda.Dao.UsuarioDao;
import com.forcavenda.Entidades.Cliente;
import com.forcavenda.Entidades.Usuario;
import com.forcavenda.R;
import com.forcavenda.Telas.Cadastros.CadastroPedidoActivity;
import com.forcavenda.Telas.Listas.ListaClienteActivity;
import com.forcavenda.Telas.Listas.ListaFormaPgtoActivity;
import com.forcavenda.Telas.Listas.ListaProdutoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        final Query refUsuarioCliente = ref.child("cliente").orderByChild("email").equalTo(user.getEmail());

        refUsuarioCliente.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                //se nao foi encontrado um valor valido para o email, entao insere o cliente com os dados do usuario atual
                if (dataSnapshot.getValue() == null) {
                    //Gera um identificador para o cliente
                    String chave = ref.child("cliente").push().getKey();

                    //Chama a classe de CRUD de usuário, fazendo referencia ao nó do banco de dados
                    Cliente cliente_usuario_ins = new Cliente(chave, "", user.getEmail(), user.getUid(), false);

                    //Chama a classe de CRUD de cliente, fazendo referencia ao nó raiz do cadastro de cliente
                    ClienteDao clienteDao = new ClienteDao();
                    clienteDao.Incluir(ref, chave, Cliente.MapCliente(cliente_usuario_ins));

                } else {

                    Cliente cliente = null;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        cliente = snapshot.getValue(Cliente.class);
                    }

                    //verifico se o identificador desse cliente está vazio. Se estiver, tenho que vincular.(Esse caso foi o usuario
                    // admin que cadastrou, entao usuario completa seu cadastro.)
                    if (cliente.getId_usuario() == null || cliente.getId_usuario().equals("")) {
                        ref.child("cliente").child(cliente.getId()).child("id_usuario").setValue(user.getUid());
                    }

                    Toast.makeText(getApplicationContext(), "Email já cadastrado anteriormente, vinculando usuário...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button btn_cliente = (Button) findViewById(R.id.btn_cliente);
        btn_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this, ListaClienteActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btn_formapgto = (Button) findViewById(R.id.btn_formapgto);
        btn_formapgto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this, ListaFormaPgtoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btn_produto = (Button) findViewById(R.id.btn_produto);
        btn_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this, ListaProdutoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btn_pedido = (Button) findViewById(R.id.btn_pedido);
        btn_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this, CadastroPedidoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, PrincipalActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
    }
}
