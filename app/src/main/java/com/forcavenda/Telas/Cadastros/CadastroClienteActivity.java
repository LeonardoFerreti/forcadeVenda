package com.forcavenda.Telas.Cadastros;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.forcavenda.Dao.ClienteDao;
import com.forcavenda.Dao.EnderecoDao;
import com.forcavenda.Dao.TelefoneDao;
import com.forcavenda.Entidades.Cliente;
import com.forcavenda.Entidades.Endereco;
import com.forcavenda.Entidades.Telefone;
import com.forcavenda.R;
import com.forcavenda.Telas.Listas.ListaClienteActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CadastroClienteActivity extends AppCompatActivity implements TextWatcher {
    private DatabaseReference ref; //Instancia uma variavel para recuperar a referencia do nó
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Recupera a instancia do Banco de dados da aplicação
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //recupera a raiz do nó do banco de dados
        ref = database.getReference();

        //Dados do cliente
        final TextView txt_nome = (TextView) findViewById(R.id.txt_nome);
        final TextView txt_email = (TextView) findViewById(R.id.txt_email);

        //Dados do endereço do cliente
        final TextView txt_rua = (TextView) findViewById(R.id.txt_rua);
        final TextView txt_numero = (TextView) findViewById(R.id.txt_numero_endereco);
        final TextView txt_complemento = (TextView) findViewById(R.id.txt_complemento);
        final TextView txt_referencia = (TextView) findViewById(R.id.txt_referencia);
        final TextView txt_cep = (TextView) findViewById(R.id.txt_cep);


        txt_cep.addTextChangedListener(this);

        //Dados do telefone do cliente
        final TextView txt_ddd = (TextView) findViewById(R.id.txt_ddd);
        final TextView txt_numero_telefone = (TextView) findViewById(R.id.txt_telefone);
        final TextView txt_ramal = (TextView) findViewById(R.id.txt_ramal);

        //recupera o botão salvar
        Button btn_salvar = (Button) findViewById(R.id.btn_salvar);

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
                Telefone telefone = new Telefone(txt_ddd.getText().toString().trim(), txt_numero_telefone.getText().toString().trim(), txt_ramal.getText().toString());
                ;


                Cliente cliente = new Cliente("", txt_nome.getText().toString().trim(), txt_email.getText().toString().trim(), endereco, telefone);
                InsereNovoCliente(cliente);
            }
        });
    }

    //Rotina responsavel por incluir um cliente
    public void InsereNovoCliente(Cliente cliente_ins) {

        //captura o identificador do cliente
        String chave = ref.child("cliente").push().getKey();

        //Mapeia o objeto cliente com os parametros identificador, nome e email
        Cliente cliente = new Cliente(chave, cliente_ins.getNome(), cliente_ins.getEmail());

        //Chama a classe de CRUD de cliente, fazendo referencia ao nó raiz do cadastro de cliente
        ClienteDao clienteDao = new ClienteDao();
        DatabaseReference refNovoCliente = clienteDao.Incluir(ref, chave, Cliente.MapCliente(cliente));

        //Chama a classe de CRUD de endereçc, fazendo referencia ao nó do cadastro de cliente
        EnderecoDao enderecoDao = new EnderecoDao();
        enderecoDao.Incluir(refNovoCliente, Endereco.MapEndereco(cliente_ins.getEndereco()));

        //Chama a classe de CRUD de telefone, fazendo referencia ao nó do cadastro de cliente
        TelefoneDao telefoneDao = new TelefoneDao();
        telefoneDao.Incluir(refNovoCliente, Telefone.MapTelefone(cliente_ins.getTelefone()));
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

        String cep = String.valueOf(s);

        if (cep.length() == 9) {
            cep = cep.replace("-", "");
//            OkHttpClient httpClient = new OkHttpClient();
            client.setReadTimeout(5, TimeUnit.SECONDS); //Aguarda 5 segundos
            client.setConnectTimeout(10, TimeUnit.SECONDS);//10 segundos para timeout
//            Request request = new Request.Builder()
//                    .url("https://viacep.com.br/ws/" + cep + "/json/")
//                    .build();
            // Response response = null;


            post("https://viacep.com.br/ws/" + cep + "/json/",  new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.i("Erro:", e.getMessage().toString());
                    Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();
                        Log.i("retorno:", responseStr);
                        // Do what you want to do with the response.
                        Toast.makeText(getApplicationContext(), responseStr, Toast.LENGTH_SHORT).show();
                    } else {
                        // Request not successful
                    }
                }

            });
        }


    }

    Call post(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

}
