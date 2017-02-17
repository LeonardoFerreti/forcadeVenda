package com.forcavenda.Telas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.forcavenda.R;
import com.forcavenda.Telas.Cadastros.CadastroFormaPgtoActivity;
import com.forcavenda.Telas.Cadastros.CadastroPedidoActivity;
import com.forcavenda.Telas.Listas.ListaClienteActivity;
import com.forcavenda.Telas.Listas.ListaFormaPgtoActivity;
import com.forcavenda.Telas.Listas.ListaProdutoActivity;

public class PrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Button btn_cliente = (Button) findViewById(R.id.btn_cliente);
        btn_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this, ListaClienteActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btn_formapgto = (Button) findViewById(R.id.btn_formapgto);
        btn_formapgto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this, ListaFormaPgtoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btn_produto = (Button) findViewById(R.id.btn_produto);
        btn_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this, ListaProdutoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btn_pedido = (Button) findViewById(R.id.btn_pedido);
        btn_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this, CadastroPedidoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, PrincipalActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
    }
}
