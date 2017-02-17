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

import com.forcavenda.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetSenhaUsuarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_senha_usuario);

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final EditText txt_email = (EditText) findViewById(R.id.txt_email);
        Button btn_recuperar = (Button) findViewById(R.id.btn_recuperar);

        btn_recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txt_email.getText().toString();
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Snackbar.make(findViewById(android.R.id.content),
                                            "E-mail enviado com sucesso.",
                                            Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });



    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ResetSenhaUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, 0);
        return true;
    }
}
