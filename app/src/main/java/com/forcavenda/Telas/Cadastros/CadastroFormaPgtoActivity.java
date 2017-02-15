package com.forcavenda.Telas.Cadastros;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.forcavenda.Dao.FormaPgtoDao;
import com.forcavenda.Entidades.FormaPgto;
import com.forcavenda.R;
import com.forcavenda.Telas.Listas.ListaFormaPgtoActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroFormaPgtoActivity extends AppCompatActivity {
    private DatabaseReference ref; //Instancia uma variavel para recuperar a referencia do nó

    FormaPgto formaPgto;
    android.support.v7.widget.AppCompatCheckBox checkBox;
    EditText txt_nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_forma_pgto);

        txt_nome = (EditText) findViewById(R.id.txt_nome);
        checkBox = (android.support.v7.widget.AppCompatCheckBox) findViewById(R.id.chk_ativo);
        formaPgto = (FormaPgto) getIntent().getSerializableExtra("formaPgto");

        if (formaPgto != null) {
            txt_nome.setText(formaPgto.getNome().toString());
            checkBox.setChecked(formaPgto.getAtivo());
        }

        FloatingActionButton btn_salvar = (FloatingActionButton) findViewById(R.id.btn_salvar);
        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //Recupera a instancia do Banco de dados da aplicação
                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                    //recupera a raiz do nó do banco de dados
                    ref = database.getReference();

                    //captura o identificador da forma de pagamento
                    String chave = (formaPgto == null) ? ref.child("formaPgto").push().getKey() : formaPgto.getId();

                    //Mapeia o objeto forma de pagamento com os parametros identificador, nome e a coluna ativo
                    FormaPgto novaformaPgto = new FormaPgto(chave, txt_nome.getText().toString().trim(), checkBox.isChecked());

                    //Chama a classe de CRUD de forma de pagamento, fazendo referencia ao nó raiz do cadastro de forma de pagamento
                    FormaPgtoDao formaPgtoDao = new FormaPgtoDao();
                    formaPgtoDao.IncluirAlterar(ref, chave, novaformaPgto.MapFormaPgto(novaformaPgto));

                    String texto = (formaPgto == null) ? "incluída" : "alterada";
                    Snackbar.make(findViewById(android.R.id.content), "Forma de pagamento " + texto + " com sucesso .",
                            Snackbar.LENGTH_SHORT).show();

                    formaPgto = novaformaPgto;

                } catch (Exception e) {
                    Snackbar.make(findViewById(android.R.id.content), "Erro ao atualizar os dados.",
                            Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CadastroFormaPgtoActivity.this, ListaFormaPgtoActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), ListaFormaPgtoActivity.class);
        startActivityForResult(intent, 0);
        return true;
    }
}
