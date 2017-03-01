package com.forcavenda.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.forcavenda.Dao.ClienteDao;
import com.forcavenda.Dao.EnderecoDao;
import com.forcavenda.Entidades.Cliente;
import com.forcavenda.Entidades.Endereco;
import com.forcavenda.R;
import com.forcavenda.Telas.Nav_PrincipalActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CadastroClienteFragment extends DialogFragment {
    private static final String PARAM_CLIENTE = "cliente";
    public Cliente cliente;
    private DatabaseReference ref; //Instancia uma variavel para recuperar a referencia do nó
    private OkHttpClient httpClient = new OkHttpClient();

    TextInputLayout input_nome;
    TextInputLayout input_email;
    TextInputLayout input_rua;
    TextInputLayout input_referencia;
    TextInputLayout input_telefone;

    EditText txt_nome;
    EditText txt_email;
    EditText txt_rua;
    EditText txt_numero;
    EditText txt_complemento;
    EditText txt_referencia;
    EditText txt_cep;
    EditText txt_telefone;

    ProgressBar progressBarCadastro;
    ProgressBar progressBarCEP;
    ProgressDialog progressDialog;

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
                                    txt_complemento.setText(jsonObject.getString("complemento"));
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

    @Override
    public void onResume() {
        // Obter parâmetros de layout existentes para a janela
        ViewGroup.LayoutParams parametros = getDialog().getWindow().getAttributes();
        // Atribuir propriedades da janela para preencher a Dialog
        parametros.width = WindowManager.LayoutParams.MATCH_PARENT;
        parametros.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) parametros);
        // Chamar o onResume da classe pai após o redimensionamento
        super.onResume();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.salvar, null);

        LayoutInflater i = getActivity().getLayoutInflater();
        View view = i.inflate(R.layout.fragment_cadastro_cliente, null);

        //Recupera a instancia do Banco de dados da aplicação
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //recupera a raiz do nó do banco de dados
        ref = database.getReference();

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
        });

        //Dados do telefone
        txt_telefone = (EditText) view.findViewById(R.id.txt_telefone);

        //Progressbar
        progressBarCEP = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBarCadastro = (ProgressBar) view.findViewById(R.id.progressBar2);

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
            txt_telefone.setText(cliente.getTelefone().toString());

        }
        builder.setView(view);

        View titulo = i.inflate(R.layout.layout_titulo_fragment, null);
        TextView txt_titulo = (TextView) titulo.findViewById(R.id.txt_1);
        txt_titulo.setText(R.string.cadastrar_cliente);
        builder.setCustomTitle(titulo);

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button btn_salvar = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btn_salvar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBarCadastro.setVisibility(View.VISIBLE);
                        if (com.forcavenda.Util.Util.estaConectadoInternet(getActivity().getApplicationContext())) {
                            if (validaCampos()) {
                                Endereco endereco = new Endereco(txt_rua.getText().toString().trim(), txt_numero.getText().toString().trim(), txt_complemento.getText().toString().trim(),
                                        txt_cep.getText().toString(), txt_referencia.getText().toString().trim());
                                Cliente cliente = new Cliente("", txt_nome.getText().toString().trim(), txt_email.getText().toString().trim(), "", false, endereco, txt_telefone.getText().toString());
                                InsereNovoCliente(cliente);
                                progressBarCadastro.setVisibility(View.GONE);
                                Toast.makeText(getActivity().getApplicationContext(), R.string.cliente_cadastrado_sucesso, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), R.string.verificar_dados_informados, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), R.string.verificar_conexao, Toast.LENGTH_SHORT).show();
                        }
                        progressBarCadastro.setVisibility(View.GONE);


                    }
                });
            }
        });


        return alertDialog;
    }

    //Rotina responsavel por incluir um cliente
    public void InsereNovoCliente(Cliente cliente_ins) {

        String chave = null;

        //captura o identificador do cliente
        if (cliente == null) {
            chave = ref.child("cliente").push().getKey();
        } else {
            chave = cliente.getId();
        }

        //Mapeia o objeto cliente com os parametros identificador, nome e email
        Cliente cliente = new Cliente(chave, cliente_ins.getNome(), cliente_ins.getEmail(), "", false, "");

        //Chama a classe de CRUD de cliente, fazendo referencia ao nó raiz do cadastro de cliente
        ClienteDao clienteDao = new ClienteDao();
        DatabaseReference refNovoCliente = clienteDao.IncluirAlterar(ref, chave, Cliente.MapCliente(cliente));

        //Chama a classe de CRUD de endereçc, fazendo referencia ao nó do cadastro de cliente
        EnderecoDao enderecoDao = new EnderecoDao();
        enderecoDao.Incluir(refNovoCliente, Endereco.MapEndereco(cliente_ins.getEndereco()));

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

//        if (validado) {
//            Endereco endereco = new Endereco(txt_rua.getText().toString().trim(), txt_numero.getText().toString().trim(),
//                    txt_complemento.getText().toString().trim(), txt_cep.getText().toString().trim(),
//                    txt_referencia.getText().toString().trim());
//
//            String chaveCliente = "";
//            if (cliente != null) {
//                chaveCliente = cliente.getId().toString();
//            } else {
//                chaveCliente = ref.child("cliente").push().getKey();
//            }
//            cliente = new Cliente(chaveCliente, txt_nome.getText().toString().trim(),
//                    txt_email.getText().toString().trim(), "", false, endereco, txt_numero_telefone.getText().toString());
//        }
        return validado;
    }

    private class SalvaCliente extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;
        private Cliente cliente;
        String msg;

        public SalvaCliente(Nav_PrincipalActivity activity, Cliente cliente) {
            this.cliente = cliente;
            progressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Salvando cliente...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            Handler handler = new Handler(getActivity().getApplicationContext().getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (com.forcavenda.Util.Util.estaConectadoInternet(getActivity().getApplicationContext())) {
                    if (validaCampos()) {
                        InsereNovoCliente(cliente);
                        msg = String.valueOf(R.string.cliente_cadastrado_sucesso);
                    } else {
                        msg = String.valueOf(R.string.verificar_dados_informados);
                    }
                } else {
                    msg = String.valueOf(R.string.verificar_conexao);
                }
            } catch (Exception e) {
                msg = "Erro ao cadastrar cliente: " + e.getMessage();
            }
            return null;
        }
    }
}
