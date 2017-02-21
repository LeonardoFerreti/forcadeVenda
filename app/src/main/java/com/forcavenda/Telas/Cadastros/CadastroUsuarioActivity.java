package com.forcavenda.Telas.Cadastros;

import android.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.forcavenda.Dao.ClienteDao;
import com.forcavenda.Dao.EnderecoDao;
import com.forcavenda.Dao.TelefoneDao;
import com.forcavenda.Dao.UsuarioDao;
import com.forcavenda.Entidades.Cliente;
import com.forcavenda.Entidades.Endereco;
import com.forcavenda.Entidades.Telefone;
import com.forcavenda.Entidades.Usuario;
import com.forcavenda.Fragments.CadastroClienteFragment;
import com.forcavenda.R;
import com.forcavenda.Telas.LoginActivity;
import com.forcavenda.Util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CadastroUsuarioActivity extends FragmentActivity {
    private Button btnCadastrar;
    EditText txt_senha;
    LinearLayout fragmentContainer;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        fragmentContainer = (LinearLayout) findViewById(R.id.container);

        final CadastroClienteFragment fragmentCliente = CadastroClienteFragment.newInstance(null);
        final FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragmentCliente);
        fragmentTransaction.commit();

        auth = FirebaseAuth.getInstance();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnCadastrar = (Button) findViewById(R.id.btn_cadastro);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Util.estaConectadoInternet(getApplicationContext())) {
                    if (fragmentCliente.validaCampos()) {
                        progressBar.setVisibility(View.VISIBLE);

                        final Cliente cliente = fragmentCliente.cliente;
                        txt_senha = (EditText) fragmentCliente.getView().findViewById(R.id.txt_senha);

                        auth.createUserWithEmailAndPassword(cliente.getEmail(), txt_senha.getText().toString())
                                .addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(CadastroUsuarioActivity.this, "Autenticação falhou:" + task.getException(),
                                                    Toast.LENGTH_SHORT).show();
                                        } else {

                                            String chaveUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                            DatabaseReference refCliente = ref.child("cliente");

                                            //captura o identificador do cliente
                                            String chaveCliente = ref.child("cliente").push().getKey();

                                            //Mapeia o objeto cliente com os parametros identificador, nome e email
                                            Cliente cliente = new Cliente(chaveCliente, fragmentCliente.cliente.getNome(), fragmentCliente.cliente.getEmail(), chaveUsuario);

                                            //Chama a classe de CRUD de cliente, fazendo referencia ao nó raiz do cadastro de cliente
                                            ClienteDao clienteDao = new ClienteDao();
                                            DatabaseReference refNovoCliente = clienteDao.Incluir(ref, chaveCliente, Cliente.MapCliente(cliente));

                                            //Chama a classe de CRUD de endereçc, fazendo referencia ao nó do cadastro de cliente
                                            EnderecoDao enderecoDao = new EnderecoDao();
                                            enderecoDao.Incluir(refNovoCliente, Endereco.MapEndereco(fragmentCliente.cliente.getEndereco()));

                                            //Chama a classe de CRUD de telefone, fazendo referencia ao nó do cadastro de cliente
                                            TelefoneDao telefoneDao = new TelefoneDao();
                                            telefoneDao.Incluir(refNovoCliente, Telefone.MapTelefone(fragmentCliente.cliente.getTelefone()));

                                            String chave_usuario = ref.child("usuario").push().getKey();

                                            //Chama a classe de CRUD de usuário, fazendo referencia ao nó do banco de dados
                                            Usuario usuario = new Usuario(chaveUsuario,chaveCliente, cliente.getEmail(),false);
                                            UsuarioDao usuarioDao = new UsuarioDao();
                                            usuarioDao.IncluirAlterar(ref,chave_usuario,Usuario.MapUsuario(usuario));

                                            FirebaseAuth.getInstance().signOut();

                                            Toast.makeText(CadastroUsuarioActivity.this, "Usuário criado:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);

                                            startActivity(new Intent(CadastroUsuarioActivity.this, LoginActivity.class));
                                            finish();
                                        }
                                    }
                                });
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Verifique os dados informados.", Snackbar.LENGTH_SHORT).show();
                    }

                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Verifique sua conexão com a internet.", Snackbar.LENGTH_SHORT).show();
                }

            }
        });


    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, 0);
        return true;
    }
}
