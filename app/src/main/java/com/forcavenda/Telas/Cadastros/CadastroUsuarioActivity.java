package com.forcavenda.Telas.Cadastros;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ProgressBar;
import android.widget.Toast;

import com.forcavenda.R;
import com.forcavenda.Telas.LoginActivity;
import com.forcavenda.Util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;


public class CadastroUsuarioActivity extends AppCompatActivity {
    private Button btnCadastrar;
    TextInputLayout input_email;
    TextInputLayout input_senha;
    TextInputLayout input_repita_senha;
    EditText txt_email;
    EditText txt_senha;
    EditText txt_repita_senha;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        auth = FirebaseAuth.getInstance();

        input_email = (TextInputLayout) findViewById(R.id.input_email);
        input_senha = (TextInputLayout) findViewById(R.id.input_senha);
        input_repita_senha = (TextInputLayout) findViewById(R.id.input_repita_senha);

        txt_email = (EditText) findViewById(R.id.txt_email);
        txt_senha = (EditText) findViewById(R.id.txt_senha);
        txt_repita_senha = (EditText) findViewById(R.id.txt_repita_senha);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnCadastrar = (Button) findViewById(R.id.btn_cadastro);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Util.estaConectadoInternet(getApplicationContext())) {
                    if (validaCampos()) {
                        progressBar.setVisibility(View.VISIBLE);

                        auth.createUserWithEmailAndPassword(txt_email.getText().toString().trim(), txt_senha.getText().toString())
                                .addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {

                                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                                Snackbar.make(findViewById(android.R.id.content),R.string.email_ja_sendo_usado,  Snackbar.LENGTH_SHORT).show();
                                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                                Snackbar.make(findViewById(android.R.id.content), R.string.email_invalido , Snackbar.LENGTH_SHORT).show();
                                            } else if (task.getException().getMessage().toString().contains("WEAK_PASSWORD")) {
                                                Snackbar.make(findViewById(android.R.id.content), R.string.senha_insegura, Snackbar.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(CadastroUsuarioActivity.this, R.string.cadastro_usuario_falha + task.getException().getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                            progressBar.setVisibility(View.GONE);

                                        } else {
                                            FirebaseAuth.getInstance().signOut();
                                            Toast.makeText(CadastroUsuarioActivity.this, "Usuário criado com sucesso.", Toast.LENGTH_SHORT).show();
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

    private boolean validaCampos() {
        Boolean validado = true;
        if (txt_email.getText().length() == 0) {
            validado = false;
            input_email.setError("Informe o endereço de e-mail.");
        } else {
            input_email.setError("");
        }

        if (txt_senha.getText().length() == 0) {
            validado = false;
            input_senha.setError("Informe a senha.");
        } else {
            input_senha.setError("");
        }

        if (txt_repita_senha.getText().length() == 0) {
            validado = false;
            input_repita_senha.setError("Confirme a senha.");
        } else {
            input_repita_senha.setError("");
        }

        if (validado && (!txt_senha.getText().toString().equals(txt_repita_senha.getText().toString()))) {
            validado = false;
            input_repita_senha.setError("Senhas não coincidem.");
        } else {
            input_repita_senha.setError("");
        }
        return validado;
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
