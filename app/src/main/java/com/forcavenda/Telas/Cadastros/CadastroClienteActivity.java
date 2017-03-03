package com.forcavenda.Telas.Cadastros;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.forcavenda.Dao.ClienteDao;
import com.forcavenda.Dao.EnderecoDao;
import com.forcavenda.Dao.TelefoneDao;
import com.forcavenda.Entidades.Cliente;
import com.forcavenda.Entidades.Endereco;

import com.forcavenda.R;
import com.forcavenda.Telas.Listas.ListaClienteActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CadastroClienteActivity extends AppCompatActivity implements TextWatcher {
    private DatabaseReference ref; //Instancia uma variavel para recuperar a referencia do nó
    private OkHttpClient client = new OkHttpClient();

    EditText txt_rua;
    EditText txt_numero;
    EditText txt_complemento;
    EditText txt_referencia;
    EditText txt_cep;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cadastro_cliente);

        //Recupera a instancia do Banco de dados da aplicação
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //recupera a raiz do nó do banco de dados
        ref = database.getReference();

        //Dados do cliente
        final EditText txt_nome = (EditText) findViewById(R.id.txt_nome);
        final EditText txt_email = (EditText) findViewById(R.id.txt_email);

        //Dados do endereço do cliente
        txt_rua = (EditText) findViewById(R.id.txt_rua);
        txt_numero = (EditText) findViewById(R.id.txt_numero_endereco);
        txt_complemento = (EditText) findViewById(R.id.txt_complemento);
        txt_referencia = (EditText) findViewById(R.id.txt_referencia);
        txt_cep = (EditText) findViewById(R.id.txt_cep);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        txt_cep.addTextChangedListener(this);

        //Dados do telefone do cliente
        final EditText txt_numero_telefone = (EditText) findViewById(R.id.txt_telefone);
      //  final EditText txt_ramal = (EditText) findViewById(R.id.txt_ramal);


        //recupera o botão salvar
        FloatingActionButton btn_salvar = (FloatingActionButton) findViewById(R.id.btn_salvar);

        //Acionado ao clique do botão salvar
        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ArrayList<Endereco> enderecos = new ArrayList<Endereco>();
                //enderecos.add(new Endereco(txt_rua.getText().toString().trim(), txt_numero.getText().toString().trim(), txt_complemento.getText().toString().trim(),
                //       txt_cep.getText().toString(), txt_referencia.getText().toString().trim()));
                Endereco endereco = new Endereco(txt_rua.getText().toString().trim(), txt_numero.getText().toString().trim(), txt_complemento.getText().toString().trim(),
                        txt_cep.getText().toString(), txt_referencia.getText().toString().trim());

                //ArrayList<Telefone> telefones = new ArrayList<Telefone>();
                //telefones.add(new Telefone(txt_ddd.getText().toString().trim(), txt_numero_telefone.getText().toString().trim(), txt_ramal.getText().toString()));
               // Telefone telefone = new Telefone(txt_numero_telefone.getText().toString().trim(), txt_ramal.getText().toString());


                Cliente cliente = new Cliente("", txt_nome.getText().toString().trim(), txt_email.getText().toString().trim(),"",false, endereco, txt_numero_telefone.getText().toString());
                InsereNovoCliente(cliente);
            }
        });
    }

    //Rotina responsavel por incluir um cliente
    public void InsereNovoCliente(Cliente cliente_ins) {

        //captura o identificador do cliente
        String chave = ref.child("cliente").push().getKey();

        //Mapeia o objeto cliente com os parametros identificador, nome e email
        Cliente cliente = new Cliente(chave, cliente_ins.getNome(), cliente_ins.getEmail(),"",false,"");

        //Chama a classe de CRUD de cliente, fazendo referencia ao nó raiz do cadastro de cliente
        ClienteDao clienteDao = new ClienteDao();
        DatabaseReference refNovoCliente = clienteDao.IncluirAlterar(ref, chave, Cliente.MapCliente(cliente));

        //Chama a classe de CRUD de endereçc, fazendo referencia ao nó do cadastro de cliente
        EnderecoDao enderecoDao = new EnderecoDao();
        enderecoDao.Incluir(refNovoCliente, Endereco.MapEndereco(cliente_ins.getEndereco()));

        //Chama a classe de CRUD de telefone, fazendo referencia ao nó do cadastro de cliente
//      //  telefoneDao.Incluir(refNovoCliente, Telefone.MapTelefone(cliente_ins.getTelefone()));

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CadastroClienteActivity.this, ListaClienteActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), ListaClienteActivity.class);
        startActivityForResult(intent, 0);
        return true;
    }

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
                Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    void buscaCEP(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onResponse(final Response response) throws IOException {
                        final String res = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(res);
                                    txt_rua.setText(jsonObject.getString("logradouro"));
                                    txt_complemento.setText(jsonObject.getString("complemento"));
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


}
