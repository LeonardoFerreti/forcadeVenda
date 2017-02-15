package com.forcavenda.Telas.Cadastros;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.forcavenda.R;
import com.forcavenda.Telas.LoginActivity;
import com.forcavenda.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtSenha;
    private Button btnCadastrar;

    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        auth = FirebaseAuth.getInstance();

        txtEmail = (EditText) findViewById(R.id.txt_usuario);
        txtSenha = (EditText) findViewById(R.id.txt_senha);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnCadastrar = (Button) findViewById(R.id.btn_cadastro);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Util.estaConectadoInternet(getApplicationContext())) {
                    progressBar.setVisibility(View.VISIBLE);

                    auth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtSenha.getText().toString())
                            .addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(CadastroUsuarioActivity.this, "Usuário criado:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(CadastroUsuarioActivity.this, "Autenticação falhou:" + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(new Intent(CadastroUsuarioActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                }
                            });

                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Verifique sua conexão com a internet.", Snackbar.LENGTH_SHORT).show();
                }

            }
        });


    }
}
