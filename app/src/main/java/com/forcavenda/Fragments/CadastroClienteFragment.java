package com.forcavenda.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.forcavenda.Entidades.Cliente;
import com.forcavenda.Entidades.Endereco;
import com.forcavenda.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class CadastroClienteFragment extends Fragment {
    private static final String PARAM_CLIENTE = "cliente";
    public Cliente cliente;
    private OkHttpClient httpClient = new OkHttpClient();

    TextInputLayout input_nome;

    EditText txt_nome;
    EditText txt_senha;
    EditText txt_repita_senha;
    EditText txt_email;

    EditText txt_rua;
    EditText txt_numero;
    EditText txt_complemento;
    EditText txt_referencia;
    EditText txt_cep;

    EditText txt_numero_telefone;


    ProgressBar progressBar;


    public CadastroClienteFragment() {
        // Construtor padrão
    }

    public static CadastroClienteFragment newInstance(Cliente cliente) {
        CadastroClienteFragment fragment = new CadastroClienteFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAM_CLIENTE, cliente);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cliente = (Cliente) getArguments().getSerializable(PARAM_CLIENTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar o layout desse fragmento
        View view = inflater.inflate(R.layout.fragment_cadastro_cliente, container, false);

        //Dados do cliente
        input_nome = (TextInputLayout) view.findViewById(R.id.input_nome);
        txt_nome = (EditText) view.findViewById(R.id.txt_nome);
        txt_email = (EditText) view.findViewById(R.id.txt_email);

        //Dados do endereço do cliente
        txt_rua = (EditText) view.findViewById(R.id.txt_rua);
        txt_numero = (EditText) view.findViewById(R.id.txt_numero_endereco);
        txt_complemento = (EditText) view.findViewById(R.id.txt_complemento);
        txt_referencia = (EditText) view.findViewById(R.id.txt_referencia);
        txt_cep = (EditText) view.findViewById(R.id.txt_cep);

        txt_cep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cep = String.valueOf(txt_cep.getText().toString().trim());

                if (cep.length() == 8) {
                    cep = cep.substring(0, 5) + "-" + cep.substring(5, 8);
                    txt_cep.setText(cep);
                }

                if (cep.length() == 9) {
                    progressBar.setVisibility(View.VISIBLE);
                    cep = cep.replace("-", "");

                    try {
                        buscaCEP("https://viacep.com.br/ws/" + cep + "/json/");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        //Dados do telefone
        txt_numero_telefone = (EditText) view.findViewById(R.id.txt_telefone);

        //Progressbar
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        Button btn_salvar = (Button) view.findViewById(R.id.btn_salvar);

        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                UserProfileChangeRequest updatePerfil = new UserProfileChangeRequest.Builder()
                        .setDisplayName(txt_nome.getText().toString())
                        .build();

                user.updateProfile(updatePerfil).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference ref = FirebaseDatabase.getInstance()
                                    .getReference("cliente")
                                    .child(cliente.getId());
                            ref.updateChildren(Cliente.MapCliente(cliente));
                            Toast.makeText(getActivity().getApplicationContext(), "Cliente alterado com sucesso.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Erro ao alterar nome: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        if (cliente != null) {
            //Dados do cliente
            txt_nome.setText(cliente.getNome().toString());
            txt_email.setText(cliente.getEmail().toString());
            //Dados do endereço do cliente
            txt_rua.setText(cliente.getEndereco().getLogradouro().toString());
            txt_numero.setText(cliente.getEndereco().getNumero().toString());
            txt_complemento.setText(cliente.getEndereco().getComplemento().toString());
            txt_referencia.setText(cliente.getEndereco().getReferencia().toString());
            txt_cep.setText(cliente.getEndereco().getCep().toString());
            //Dados do telefone
            txt_numero_telefone.setText(cliente.getTelefone().toString());

        }

        return view;
    }


    void buscaCEP(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        httpClient.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onResponse(final Response response) throws IOException {
                        final String res = response.body().string();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(res);
                                    txt_rua.setText(jsonObject.getString("logradouro"));
                                    txt_complemento.setText(jsonObject.getString("complemento"));
                                    int pos = txt_rua.getText().length();
                                    txt_rua.requestFocus();
                                    txt_rua.setSelection(pos);
                                    progressBar.setVisibility(View.GONE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.i("Erro: ", "Erro ao retornar o array em JSON: " + e.getMessage());
                                }
                            }
                        });
                    }
                });
    }

    public Boolean validaCampos() {
        Boolean validado = true;

        if (txt_nome.getText().length() == 0) {
            validado = false;
            input_nome.setError(getString(R.string.nome_cliente_vazio));
        } else {
            input_nome.setError("");
        }

        if (validado) {
            Endereco endereco = new Endereco(txt_rua.getText().toString().trim(), txt_numero.getText().toString().trim(),
                    txt_complemento.getText().toString().trim(), txt_cep.getText().toString().trim(),
                    txt_referencia.getText().toString().trim());

            String chaveCliente = "";
            if (cliente != null) {
                chaveCliente = cliente.getId().toString();
            }
            cliente = new Cliente(chaveCliente, txt_nome.getText().toString().trim(),
                    txt_email.getText().toString().trim(), "", false, endereco, txt_numero_telefone.getText().toString());
        }
        return validado;
    }

}
