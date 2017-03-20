package com.forcavenda.Telas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.forcavenda.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class ResetSenhaUsuarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_senha_usuario);

       //Captura a instancia do usuario atual
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final EditText txt_email = (EditText) findViewById(R.id.txt_email);
        Button btn_recuperar = (Button) findViewById(R.id.btn_recuperar);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btn_recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txt_email.getText().toString();
                if (email.length() > 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(),
                                                "E-mail enviado com sucesso.",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (task.getException().getMessage().contains("INVALID_EMAIL")) {
                                            Toast.makeText(getApplicationContext(), R.string.email_invalido, Toast.LENGTH_SHORT).show();
                                        } else if (task.getException().getMessage().contains("no user record corresponding to this identifier")) {
                                            Toast.makeText(getApplicationContext(), R.string.conta_inexistente, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), R.string.erro_envio_email + task.getException().getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Digite um e-mail.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        //Volta a tela de login
        Intent intent = new Intent(ResetSenhaUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //Volta a tela de login
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, 0);
        return true;
    }
}
