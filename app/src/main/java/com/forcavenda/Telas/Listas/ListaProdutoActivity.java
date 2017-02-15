package com.forcavenda.Telas.Listas;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.forcavenda.Adapters.ListaProdutoAdapter;
import com.forcavenda.Entidades.Produto;
import com.forcavenda.R;
import com.forcavenda.Telas.Cadastros.CadastroProdutoActivity;
import com.forcavenda.Telas.PrincipalActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaProdutoActivity extends AppCompatActivity {
    List<Produto> listaProdutos = new ArrayList<Produto>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        ListView listView = (ListView) findViewById(R.id.lista);

        final FirebaseDatabase banco = FirebaseDatabase.getInstance();
        DatabaseReference tabProdutos = banco.getReference("produto");

        Query resultado = tabProdutos.orderByChild("nome");

        final ListaProdutoAdapter adapter = new ListaProdutoAdapter(getApplicationContext(),
                listaProdutos);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Produto produto = (Produto) adapter.getItem(position);

                Intent intent = new Intent(ListaProdutoActivity.this, CadastroProdutoActivity.class);
                intent.putExtra("produto", produto);
                startActivity(intent);
                finish();

            }
        });
        resultado.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        resultado.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Produto cliente = snapshot.getValue(Produto.class);
                listaProdutos.add(cliente);
                adapter.notifyDataSetChanged();
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


        FloatingActionButton btn_adicionar = (FloatingActionButton) findViewById(R.id.btn_adicionar);

        btn_adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaProdutoActivity.this, CadastroProdutoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
        startActivityForResult(intent, 0);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ListaProdutoActivity.this, PrincipalActivity.class);
        startActivity(intent);
        finish();
    }
}
