package com.forcavenda.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.forcavenda.Dao.ClienteDao;
import com.forcavenda.Dao.EnderecoDao;
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
import com.squareup.okhttp.internal.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Leo on 23/02/2017.
 */

public class PerfilFragment extends Fragment {
    private DatabaseReference ref; //Instancia uma variavel para recuperar a referencia do nó
    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private static final String ARG_CLIENTE = "cliente";

    Cliente cliente;
    private OkHttpClient httpClient = new OkHttpClient();

    TextInputLayout input_nome;
    EditText txt_nome;
    EditText txt_email;

    EditText txt_rua;
    EditText txt_numero;
    EditText txt_complemento;
    EditText txt_referencia;
    EditText txt_cep;

    EditText txt_numero_telefone;
    ProgressBar progressBar;
    ProgressBar progressBar2;

    Boolean consultaCep = true;

    public PerfilFragment() {
    }

    public static PerfilFragment newInstance(Cliente cliente) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLIENTE, cliente);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cliente = (Cliente) getArguments().getSerializable(ARG_CLIENTE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_cliente, container, false);

        progressBar2 = (ProgressBar) view.findViewById(R.id.progressBar2);

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

                if (consultaCep){
                    if (cep.length() == 9 ) {
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

            }
        });

        //Dados do telefone
        txt_numero_telefone = (EditText) view.findViewById(R.id.txt_telefone);
        //Progressbar
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);


        if (cliente != null) {
            //Dados do cliente
            txt_nome.setText(cliente.getNome().toString());
            txt_email.setText(cliente.getEmail().toString());
            txt_email.setEnabled(false);

            if (cliente.getEndereco() != null) {
                //Dados do endereço do cliente
                consultaCep =false;
                txt_cep.setText(cliente.getEndereco().getCep().toString().replace("-", ""));
                txt_rua.setText(cliente.getEndereco().getLogradouro().toString());
                txt_numero.setText(cliente.getEndereco().getNumero().toString());
                txt_complemento.setText(cliente.getEndereco().getComplemento().toString());
                txt_referencia.setText(cliente.getEndereco().getReferencia().toString());
                consultaCep =true;
            }

            //Dados do telefone
            txt_numero_telefone.setText(cliente.getTelefone().toString());

        }

        Button btn_salvar = (Button) view.findViewById(R.id.btn_salvar);

        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar2.setVisibility(View.VISIBLE);
                if (com.forcavenda.Util.Util.estaConectadoInternet(getActivity().getApplicationContext())) {
                    if (validaCampos()) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest updatePerfil = new UserProfileChangeRequest.Builder()
                                .setDisplayName(txt_nome.getText().toString())
                                .build();

                        user.updateProfile(updatePerfil).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    insereNovoCliente(cliente);
                                    Toast.makeText(getActivity().getApplicationContext(), "Cliente alterado com sucesso.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), "Erro ao alterar nome: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Verifique os dados informados.", Snackbar.LENGTH_SHORT).show();
                    }

                } else {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Verifique sua conexão com a internet.", Snackbar.LENGTH_SHORT).show();
                }
                progressBar2.setVisibility(View.GONE);
            }
        });


        return view;
    }

    //Rotina responsavel por incluir um cliente
    private void insereNovoCliente(Cliente cliente_ins) {

        //Recupera a instancia do Banco de dados da aplicação
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //recupera a raiz do nó do banco de dados
        ref = database.getReference();

        //captura o identificador do cliente
        String chave = cliente_ins.getId(); // ref.child("cliente").push().getKey();

        //Mapeia o objeto cliente com os parametros identificador, nome e email
        Cliente cliente = new Cliente(chave, cliente_ins.getNome(), cliente_ins.getEmail(),
                cliente_ins.getId_usuario(), cliente_ins.getAdmin(),
                txt_numero_telefone.getText().toString());

        //Chama a classe de CRUD de cliente, fazendo referencia ao nó raiz do cadastro de cliente
        ClienteDao clienteDao = new ClienteDao();
        DatabaseReference refNovoCliente = clienteDao.IncluirAlterar(ref, chave, Cliente.MapCliente(cliente));

        //Chama a classe de CRUD de endereçc, fazendo referencia ao nó do cadastro de cliente
        EnderecoDao enderecoDao = new EnderecoDao();
        enderecoDao.Incluir(refNovoCliente, Endereco.MapEndereco(cliente_ins.getEndereco()));
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
                                    //txt_complemento.setText(jsonObject.getString("complemento"));
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
                    txt_email.getText().toString().trim(), FirebaseAuth.getInstance().getCurrentUser().getUid(), cliente.getAdmin(), endereco, txt_numero_telefone.getText().toString());
        }
        return validado;
    }

}
