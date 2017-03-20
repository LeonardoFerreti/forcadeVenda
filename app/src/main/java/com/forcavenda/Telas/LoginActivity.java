package com.forcavenda.Telas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.forcavenda.R;
import com.forcavenda.Telas.Cadastros.CadastroUsuarioActivity;
import com.forcavenda.Util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    //Autenticação do usuário
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //Componentes da tela
    private TextInputLayout input_email;
    private TextInputLayout input_senha;
    private TextView txt_Email;
    private TextView txt_Senha;
    private Button btnlogin;
    private Button btnCadastrar;
    private Button btnEsqueci;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        input_email = (TextInputLayout) findViewById(R.id.input_email);
        input_senha = (TextInputLayout) findViewById(R.id.input_senha);
        txt_Email = (TextView) findViewById(R.id.txt_usuario);
        txt_Senha = (TextView) findViewById(R.id.txt_senha);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnlogin = (Button) findViewById(R.id.btn_login);
        btnCadastrar = (Button) findViewById(R.id.btn_cadastrar);
        btnEsqueci = (Button) findViewById(R.id.btn_esqueci);

        //clique do botao esqueci senha
        btnEsqueci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetSenhaUsuarioActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Clique do botao cadastrar
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //clique do botao de login
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_email.setError("");
                input_senha.setError("");
                if (Util.estaConectadoInternet(getApplicationContext())) {
                    if (validaDados()) {
                        progressBar.setVisibility(View.VISIBLE); //Mostra a progressBar

                        //autenticando usuário
                        auth.signInWithEmailAndPassword(txt_Email.getText().toString().trim(), txt_Senha.getText().toString().trim())
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar.setVisibility(View.VISIBLE);
                                        if (!task.isSuccessful()) {
                                            progressBar.setVisibility(View.GONE);
                                            if (task.getException() instanceof FirebaseAuthException) {
                                                FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                                switch (e.getErrorCode()) {
                                                    case "ERROR_USER_NOT_FOUND":
                                                        input_email.setError("Usuário não encontrado.");
                                                        break;
                                                    case "ERROR_WRONG_PASSWORD":
                                                        input_senha.setError("Senha inválida.");
                                                        break;
                                                    case "WEAK_PASSWORD":
                                                        input_senha.setError("Sua senha deve conter no mínimo 6 dígitos.");
                                                    default:
                                                        Toast.makeText(LoginActivity.this, getString(R.string.erro_login) + e.getErrorCode(), Toast.LENGTH_LONG).show();
                                                }
                                            }

                                        } else {
                                            progressBar.setVisibility(View.GONE); //oculta a progressBar
                                            //Navega para a tela principal
                                            Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Responde a alterações no estado de início de sessão do usuário
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    //Usuário saindo do sistema
                    Log.d("Log", "onAuthStateChanged:saindo");
                }
            }
        };
    }

    private boolean validaDados() {
        boolean validado = true;
        if (txt_Email.getText().length() == 0) {
            validado = false;
            input_email.setError("Informe o e-mail.");
        } else {
            input_email.setError("");
        }

        if (txt_Senha.getText().length() == 0) {
            validado = false;
            input_senha.setError("Informe a senha.");
        } else {
            input_senha.setError("");
        }

        return validado;
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
