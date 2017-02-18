package com.forcavenda.Telas.Cadastros;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.forcavenda.Adapters.SpinnerFormaPgtoAdapter;
import com.forcavenda.Adapters.ListItensVendaAdapter;
import com.forcavenda.Adapters.TextViewClienteAdapter;
import com.forcavenda.Adapters.TextViewProdutoAdapter;
import com.forcavenda.Dao.PedidoDao;
import com.forcavenda.Entidades.Cliente;
import com.forcavenda.Entidades.FormaPgto;
import com.forcavenda.Entidades.ItemPedido;
import com.forcavenda.Entidades.Pedido;
import com.forcavenda.Entidades.Produto;
import com.forcavenda.R;
import com.forcavenda.Telas.PrincipalActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CadastroPedidoActivity extends AppCompatActivity {

    List<FormaPgto> listaFormaPgto = new ArrayList<FormaPgto>();
    List<ItemPedido> listaitemPedidos = new ArrayList<ItemPedido>();
    ArrayList<Cliente> listaCliente = new ArrayList<Cliente>();
    ArrayList<Produto> listaProduto = new ArrayList<Produto>();

    private Long identificadorAtual = Long.valueOf(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pedido);
        final FirebaseDatabase banco = FirebaseDatabase.getInstance();
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        final AutoCompleteTextView txt_cliente = (AutoCompleteTextView) findViewById(R.id.txt_cliente);
        final AutoCompleteTextView txt_item = (AutoCompleteTextView) findViewById(R.id.txt_item);
        final ListView listViewItens = (ListView) findViewById(R.id.lista_item);
        Button btn_adiciona_item = (Button) findViewById(R.id.btn_adicionar_item);
        FloatingActionButton btn_Salvar = (FloatingActionButton) findViewById(R.id.btn_salvar);
        final Spinner cboFormaPgto = (Spinner) findViewById(R.id.cboFormapgto);

        final ListItensVendaAdapter itensVendaAdapter = new ListItensVendaAdapter(this, R.layout.lista_itens_venda, listaitemPedidos);
        listViewItens.setAdapter(itensVendaAdapter);

        DatabaseReference tabFormaPgto = banco.getReference("formaPgto");
        Query resultadoFormaPgto = tabFormaPgto.orderByChild("nome");
        final SpinnerFormaPgtoAdapter adapterFormaPgto = new SpinnerFormaPgtoAdapter(getApplicationContext(), listaFormaPgto);
        cboFormaPgto.setAdapter(adapterFormaPgto);
        resultadoFormaPgto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        resultadoFormaPgto.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                FormaPgto formaPgto = snapshot.getValue(FormaPgto.class);
                listaFormaPgto.add(formaPgto);
                adapterFormaPgto.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            // ....
        });

        DatabaseReference tabProdutos = banco.getReference("produto");
        Query resultadoProduto = tabProdutos.orderByChild("nome");
        txt_item.setThreshold(1);
        final TextViewProdutoAdapter adapterProduto = new TextViewProdutoAdapter(this, R.layout.layout_text, listaProduto);
        resultadoProduto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                txt_item.setAdapter(adapterProduto);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        resultadoProduto.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Produto produto = snapshot.getValue(Produto.class);
                listaProduto.add(produto);
                adapterProduto.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            // ....
        });

        DatabaseReference tabClientes = banco.getReference("cliente");
        Query resultadoCliente = tabClientes.orderByChild("nome");
        txt_cliente.setThreshold(1);
        final TextViewClienteAdapter adapterCliente = new TextViewClienteAdapter(this, R.layout.layout_text, listaCliente);
        resultadoCliente.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                txt_cliente.setAdapter(adapterCliente);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        resultadoCliente.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Cliente cliente = snapshot.getValue(Cliente.class);
                listaCliente.add(cliente);
                adapterCliente.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            // ....
        });

        btn_adiciona_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Produto> itens = ((TextViewProdutoAdapter) txt_item.getAdapter()).getItems();
                for (Produto produto : itens) {
                    if (produto.getNome().equals(txt_item.getText().toString())) {
                        boolean jaIncluiuNaLista = false;
                        for (ItemPedido item : listaitemPedidos) {
                            if (produto.getNome().equals(item.getProduto().getNome())) {
                                jaIncluiuNaLista = true;
                            }
                        }
                        if (jaIncluiuNaLista == false) {
                            ItemPedido itemPedido = new ItemPedido(produto, 1);
                            listaitemPedidos.add(itemPedido);
                            itensVendaAdapter.notifyDataSetChanged();
                            txt_item.setText("");
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "Atenção: Item já adicionado.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        btn_Salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference tabPedido = banco.getReference("pedido");
                Cliente cliente;
                FormaPgto formaPgto;
                List<ItemPedido> itensPedido;

                cliente = adapterCliente.getItembyName(txt_cliente.getText().toString().trim());
                formaPgto = (FormaPgto) cboFormaPgto.getItemAtPosition(cboFormaPgto.getSelectedItemPosition());
                itensPedido = listaitemPedidos;

                final Pedido pedido = new Pedido(Long.valueOf(String.valueOf(0)), "", cliente, formaPgto, itensVendaAdapter.getItems(),
                        Double.valueOf(String.valueOf(50)), Double.valueOf(String.valueOf(0)), Double.valueOf(String.valueOf(50)), false);

                final Pedido novo_pedido = InsereNovoPedido(pedido,cliente,formaPgto,itensPedido);

                PedidoDao pedidoDao = new PedidoDao(getApplicationContext(), novo_pedido);
                pedidoDao.IncluirIdPedido(CadastroPedidoActivity.this);

            }
        });
    }


    //Rotina responsavel por incluir um cliente
    public Pedido InsereNovoPedido(Pedido pedido_ins,Cliente cliente, FormaPgto formaPgto, List<ItemPedido> itensPedido) {
        //Recupera a instancia do Banco de dados da aplicação
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Recupera a raiz do nó do banco de dados
        DatabaseReference ref = database.getReference();
        //captura o identificador do pedido
        String chave = ref.child("pedido").push().getKey();
        //Mapeia o objeto pedido
        Pedido pedido = new Pedido(chave, pedido_ins.getIdPedido(), pedido_ins.getValorTotal(), pedido_ins.getDesconto(),
                pedido_ins.getValorPago(), pedido_ins.getOnline());

        PedidoDao pedidoDao = new PedidoDao(getApplicationContext(), pedido);
        DatabaseReference refNovoPedido = pedidoDao.IncluirNoRegistro(ref, chave, Pedido.MapPedido(pedido));


        Map<String, Object> objDao = new HashMap<>();
        objDao.put("cliente", cliente);
        refNovoPedido.updateChildren(objDao);

        objDao = new HashMap<>();
        objDao.put("formaPgto", formaPgto);
        refNovoPedido.updateChildren(objDao);



        return pedido;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CadastroPedidoActivity.this, PrincipalActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
        startActivityForResult(intent, 0);
        return true;
    }
}
