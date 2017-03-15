package com.forcavenda.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.forcavenda.R;
import com.forcavenda.Util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Leo on 23/02/2017.
 */

public class TrocaSenhaFragment extends Fragment {

    ProgressBar progressBar;
    TextInputLayout input_senha_antiga;
    TextInputLayout input_senha_nova;
    TextInputLayout input_repita_senha_nova;
    EditText txt_senha_antiga;
    EditText txt_senha;
    EditText txt_repita_senha;

    public TrocaSenhaFragment() {

    }

    public static TrocaSenhaFragment newInstance() {
        TrocaSenhaFragment fragment = new TrocaSenhaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_troca_senha, container, false);

        input_senha_antiga = (TextInputLayout) view.findViewById(R.id.input_senha_antiga);
        input_senha_nova = (TextInputLayout) view.findViewById(R.id.input_nova_senha);
        input_repita_senha_nova = (TextInputLayout) view.findViewById(R.id.input_repita_nova_senha);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        txt_senha_antiga = (EditText) view.findViewById(R.id.txt_senha_antiga);
        txt_senha = (EditText) view.findViewById(R.id.txt_nova_senha);
        txt_repita_senha = (EditText) view.findViewById(R.id.txt_repita_nova_senha);
        final Button btn_salvar = (Button) view.findViewById(R.id.btn_salvar);

        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Util.estaConectadoInternet(getActivity().getApplicationContext())) {
                    if (validaDados()) {
                        progressBar.setVisibility(View.VISIBLE);
                        btn_salvar.setEnabled(false);

                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        AuthCredential credential = EmailAuthProvider
                                .getCredential(user.getEmail(), txt_senha_antiga.getText().toString());
                        //tenta reautenticar o usuário de acordo com a informacao passada
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //Se obtido o sucesso, tenta atualizar a senha.
                                            user.updatePassword(txt_senha.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {
                                                        txt_senha_antiga.setText("");
                                                        txt_senha.setText("");
                                                        txt_repita_senha.setText("");
                                                        Toast.makeText(getActivity().getApplicationContext(), "Senha alterada com sucesso.", Toast.LENGTH_SHORT).show();
                                                        btn_salvar.setEnabled(true);
                                                        progressBar.setVisibility(View.GONE);
                                                    } else {
                                                        Toast.makeText(getActivity().getApplicationContext(), "Erro ao trocar senha: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        Log.i("erro", task.getException().getMessage());
                                                        btn_salvar.setEnabled(true);
                                                        progressBar.setVisibility(View.GONE);
                                                    }
                                                }
                                            });

                                        } else {
                                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                                 input_senha_antiga.setError("A senha informada está errada.");
                                            } else {
                                                Toast.makeText(getActivity().getApplicationContext(), "Erro ao trocar senha: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                Log.i("erro", task.getException().getMessage());
                                            }
                                            btn_salvar.setEnabled(true);
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();

                }
            }
        });

        return view;
    }

    private boolean validaDados() {
        boolean validado = true;

        if (txt_senha_antiga.getText().length() == 0) {
            validado = false;
            input_senha_antiga.setError("Informe sua senha atual.");
        } else {
            input_senha_antiga.setError("");
        }

        if (txt_senha.getText().length() == 0) {
            validado = false;
            input_senha_nova.setError("Informe a nova senha.");
        } else {
            input_senha_nova.setError("");
        }

        if (txt_senha.getText().length() < 6) {
            validado = false;
            input_senha_nova.setError("Sua senha deve ter no mínimo 6 caracteres.");
        } else {
            input_senha_nova.setError("");
        }

        if (txt_repita_senha.getText().length() == 0) {
            validado = false;
            input_repita_senha_nova.setError("Confirme a nova senha.");
        } else {
            input_repita_senha_nova.setError("");
        }

        if (validado && (!txt_senha.getText().toString().equals(txt_repita_senha.getText().toString()))) {
            validado = false;
            input_repita_senha_nova.setError("Senhas não coincidem.");
        } else {
            input_repita_senha_nova.setError("");
        }
        return validado;
    }

}