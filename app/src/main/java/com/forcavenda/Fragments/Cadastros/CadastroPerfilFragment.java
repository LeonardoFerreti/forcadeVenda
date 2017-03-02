package com.forcavenda.Fragments.Cadastros;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
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

import com.forcavenda.Dao.ClienteDao;
import com.forcavenda.Dao.EnderecoDao;
import com.forcavenda.Entidades.Cliente;
import com.forcavenda.Entidades.Endereco;
import com.forcavenda.R;
import com.forcavenda.Telas.Nav_PrincipalActivity;
import com.forcavenda.Util.Util;
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

/**
 * Created by Leo on 23/02/2017.
 */

public class CadastroPerfilFragment extends Fragment {
    private DatabaseReference ref; //Instancia uma variavel para recuperar a referencia do nó
    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private static final String ARG_CLIENTE = "cliente";

    Cliente cliente;
    private OkHttpClient httpClient = new OkHttpClient();

    TextInputLayout input_nome;
    TextInputLayout input_email;
    TextInputLayout input_rua;
    TextInputLayout input_referencia;
    TextInputLayout input_telefone;
    EditText txt_telefone;

    EditText txt_nome;
    EditText txt_email;
    EditText txt_rua;
    EditText txt_numero;
    EditText txt_complemento;
    EditText txt_referencia;
    EditText txt_cep;

    ProgressBar progressBarCEP;
    ProgressBar progressBarCadastro;

    Boolean consultaCep = true;

    public CadastroPerfilFragment() {
    }

    public static CadastroPerfilFragment newInstance(Cliente cliente) {
        CadastroPerfilFragment fragment = new CadastroPerfilFragment();
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

        progressBarCadastro = (ProgressBar) view.findViewById(R.id.progressBar2);

        //Dados do cliente
        input_nome = (TextInputLayout) view.findViewById(R.id.input_nome);
        input_email = (TextInputLayout) view.findViewById(R.id.input_email);
        input_rua = (TextInputLayout) view.findViewById(R.id.input_rua);
        input_referencia = (TextInputLayout) view.findViewById(R.id.input_referencia);
        input_telefone = (TextInputLayout) view.findViewById(R.id.input_telefone);
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

                if (consultaCep) {
                    if (cep.length() == 9) {
                        progressBarCEP.setVisibility(View.VISIBLE);
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
        txt_telefone = (EditText) view.findViewById(R.id.txt_telefone);
        //Progressbar
        progressBarCEP = (ProgressBar) view.findViewById(R.id.progressBar);

        if (cliente != null) {
            //Dados do cliente
            txt_nome.setText(cliente.getNome().toString());
            txt_email.setText(cliente.getEmail().toString());
            txt_email.setEnabled(false);

            if (cliente.getEndereco() != null) {
                //Dados do endereço do cliente
                consultaCep = false;
                txt_cep.setText(cliente.getEndereco().getCep().toString().replace("-", ""));
                txt_rua.setText(cliente.getEndereco().getLogradouro().toString());
                txt_numero.setText(cliente.getEndereco().getNumero().toString());
                txt_complemento.setText(cliente.getEndereco().getComplemento().toString());
                txt_referencia.setText(cliente.getEndereco().getReferencia().toString());
                consultaCep = true;
            }

            //Dados do telefone
            txt_telefone.setText(cliente.getTelefone().toString());

        }

        Button btn_salvar = (Button) view.findViewById(R.id.btn_salvar);
        btn_salvar.setVisibility(View.VISIBLE);

        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = Util.CriaProgressDialog(getActivity());
                progressDialog.show();
                if (com.forcavenda.Util.Util.estaConectadoInternet(getActivity().getApplicationContext())) {
                    if (validaCampos()) {
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest updatePerfil = new UserProfileChangeRequest.Builder()
                                .setDisplayName(txt_nome.getText().toString())
                                .build();
                        user.updateProfile(updatePerfil)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //Recupera a instancia do Banco de dados da aplicação
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            //recupera a raiz do nó do banco de dados
                                            ref = database.getReference();
                                            //captura o identificador do Cliente
                                            String chave = (cliente == null) ? ref.child("cliente").push().getKey() : cliente.getId();
                                            //Mapeia o objeto Endereço
                                            Endereco endereco = new Endereco(txt_rua.getText().toString().trim(), txt_numero.getText().toString().trim(), txt_complemento.getText().toString().trim(),
                                                    txt_cep.getText().toString(), txt_referencia.getText().toString().trim());
                                            //Mapeia o objeto Cliente
                                            Boolean isAdmin = (cliente == null) ? false : cliente.getAdmin();
                                            Cliente novoCliente = new Cliente(chave, txt_nome.getText().toString().trim(), user.getEmail(), user.getUid(),
                                                    isAdmin, endereco, txt_telefone.getText().toString());
                                            //Chama a classe de CRUD de forma de pagamento, fazendo referencia ao nó raiz do Cliente
                                            ClienteDao clienteDao = new ClienteDao();
                                            DatabaseReference refNovoCliente = clienteDao.IncluirAlterar(ref, chave, novoCliente.MapCliente(novoCliente));
                                            //Chama a classe de CRUD de endereçc, fazendo referencia ao nó do cadastro de cliente
                                            EnderecoDao enderecoDao = new EnderecoDao();
                                            enderecoDao.Incluir(refNovoCliente, Endereco.MapEndereco(novoCliente.getEndereco()));
                                            Toast.makeText(getActivity().getApplicationContext(), R.string.perfil_alterado_sucesso, Toast.LENGTH_SHORT).show();
                                            cliente = novoCliente;
                                        }
                                    }
                                });
                    }
                }
                progressDialog.dismiss();
            }
        });
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
                                progressBarCEP.setVisibility(View.GONE);
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
                                    progressBarCEP.setVisibility(View.GONE);
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

        if (txt_email.getText().length() == 0) {
            validado = false;
            input_email.setError(getString(R.string.email_cliente_vazio));
        } else {
            input_email.setError("");
        }

        if (txt_rua.getText().length() == 0) {
            validado = false;
            input_rua.setError(getString(R.string.rua_cliente_vazio));
        } else {
            input_rua.setError("");
        }

        if (txt_referencia.getText().length() == 0) {
            validado = false;
            input_referencia.setError(getString(R.string.referencia_cliente_vazio));
        } else {
            input_referencia.setError("");
        }

        if (txt_telefone.getText().length() == 0) {
            validado = false;
            input_telefone.setError(getString(R.string.telefone_cliente_vazio));
        } else {
            input_telefone.setError("");
        }
        return validado;
    }
}
