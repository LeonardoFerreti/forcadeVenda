package com.forcavenda.Telas.Cadastros;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.forcavenda.Dao.ProdutoDao;
import com.forcavenda.Entidades.Produto;
import com.forcavenda.R;
import com.forcavenda.Telas.Listas.ListaProdutoActivity;
import com.forcavenda.Telas.PrincipalActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroProdutoActivity extends AppCompatActivity {
    private DatabaseReference ref; //Instancia uma variavel para recuperar a referencia do nó

    Produto produto;
    EditText txt_nome;
    EditText txt_preco;
    android.support.v7.widget.AppCompatCheckBox checkBox;
    Button btn_salvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produto);

        txt_nome = (EditText) findViewById(R.id.txt_nome);
        txt_preco = (EditText) findViewById(R.id.txt_preco);
        checkBox = (android.support.v7.widget.AppCompatCheckBox) findViewById(R.id.chk_ativo);
        btn_salvar = (Button) findViewById(R.id.btn_salvar);
        produto = (Produto) getIntent().getSerializableExtra("produto");

        if (produto != null) {
            txt_nome.setText(produto.getNome().toString());
            txt_preco.setText(produto.getPreco().toString());
            checkBox.setChecked(produto.getAtivo());
        }

        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Recupera a instancia do Banco de dados da aplicação
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                //recupera a raiz do nó do banco de dados
                ref = database.getReference();

                //captura o identificador do produto
                String chave = (produto == null) ? ref.child("produto").push().getKey() : produto.getId();

                //Mapeia o objeto produto com os parametros identificador, nome e preço.
                Produto novoProduto = new Produto(chave, txt_nome.getText().toString().trim(), Double.valueOf(txt_preco.getText().toString().trim()), checkBox.isChecked());

                //Chama a classe de CRUD de produto, fazendo referencia ao nó raiz do cadastro de produto
                ProdutoDao produtoDao = new ProdutoDao();
                produtoDao.Incluir(ref, chave, novoProduto.MapFormaPgto(novoProduto));

                String texto = (produto == null) ? "incluído" : "alterado";
                Snackbar.make(findViewById(android.R.id.content), "Produto " + texto + " com sucesso .",
                        Snackbar.LENGTH_SHORT).show();

                produto = novoProduto;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CadastroProdutoActivity.this, ListaProdutoActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), ListaProdutoActivity.class);
        startActivityForResult(intent, 0);
        return true;
    }
}
