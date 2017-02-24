package com.forcavenda.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.forcavenda.R;
import com.forcavenda.Util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Leo on 23/02/2017.
 */

public class TrocaSenhaFragment extends Fragment {

    TextInputLayout input_senha_nova;
    TextInputLayout input_repita_senha_nova;
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

        input_senha_nova = (TextInputLayout) view.findViewById(R.id.input_nova_senha);
        input_repita_senha_nova = (TextInputLayout) view.findViewById(R.id.input_repita_nova_senha);

        txt_senha = (EditText) view.findViewById(R.id.txt_nova_senha);
        txt_repita_senha = (EditText) view.findViewById(R.id.txt_repita_nova_senha);
        Button btn_salvar = (Button) view.findViewById(R.id.btn_salvar);

        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Util.estaConectadoInternet(getActivity().getApplicationContext())) {
                    if (validaDados()) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        user.updatePassword(txt_senha.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Senha alterada com sucesso.", Snackbar.LENGTH_SHORT).show();

                                } else {
                                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Erro ao trocar senha: " + task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();

                                }

                            }
                        });

                    }
                } else {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Verifique sua conexão com a internet.", Snackbar.LENGTH_SHORT).show();

                }
            }
        });

        return view;
    }

    private boolean validaDados() {
        boolean validado = false;
        if (txt_senha.getText().length() == 0) {
            validado = false;
            input_senha_nova.setError("Informe a nova senha.");
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