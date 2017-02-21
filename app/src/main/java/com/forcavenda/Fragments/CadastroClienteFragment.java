package com.forcavenda.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.forcavenda.Entidades.Cliente;
import com.forcavenda.Entidades.Endereco;
import com.forcavenda.Entidades.Telefone;
import com.forcavenda.R;
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
    private OkHttpClient client = new OkHttpClient();

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
    EditText txt_ramal;

    ProgressBar progressBar;


    public CadastroClienteFragment() {
        // Construtor padrão
    }

    public static CadastroClienteFragment newInstance(String param1) {
        CadastroClienteFragment fragment = new CadastroClienteFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAM_CLIENTE, param1);

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
        txt_senha = (EditText) view.findViewById(R.id.txt_senha);
        txt_repita_senha = (EditText) view.findViewById(R.id.txt_repita_senha);

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
        txt_ramal = (EditText) view.findViewById(R.id.txt_ramal);

        //Progressbar
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

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
            txt_numero_telefone.setText(cliente.getTelefone().getNumero().toString());
            txt_ramal.setText(cliente.getTelefone().getRamal().toString());
        }

        return view;
    }


    void buscaCEP(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request)
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
            Telefone telefone = new Telefone(txt_numero_telefone.getText().toString().trim(), txt_ramal.getText().toString().trim());
            Endereco endereco = new Endereco(txt_rua.getText().toString().trim(), txt_numero.getText().toString().trim(),
                    txt_complemento.getText().toString().trim(), txt_cep.getText().toString().trim(),
                    txt_referencia.getText().toString().trim());

            String chaveCliente = "";
            if (cliente!= null){
                chaveCliente = cliente.getId().toString();
            }
            cliente = new Cliente(chaveCliente , txt_nome.getText().toString().trim(),
                    txt_email.getText().toString().trim(),"",false, endereco, telefone);
        }
        return validado;
    }

}
