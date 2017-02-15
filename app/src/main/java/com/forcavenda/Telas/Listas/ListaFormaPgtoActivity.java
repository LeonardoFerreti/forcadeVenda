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

import com.forcavenda.Adapters.ListaClienteAdapter;
import com.forcavenda.Adapters.ListaFormaPgtoAdapter;
import com.forcavenda.Entidades.Cliente;
import com.forcavenda.Entidades.FormaPgto;
import com.forcavenda.R;
import com.forcavenda.Telas.Cadastros.CadastroClienteActivity;
import com.forcavenda.Telas.Cadastros.CadastroFormaPgtoActivity;
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

public class ListaFormaPgtoActivity extends AppCompatActivity {
    List<FormaPgto> listaFormaPgto = new ArrayList<FormaPgto>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        ListView listView = (ListView) findViewById(R.id.lista);

        final FirebaseDatabase banco = FirebaseDatabase.getInstance();
        DatabaseReference tabFormaPgto = banco.getReference("formaPgto");

        Query resultado = tabFormaPgto.orderByChild("nome");

        final ListaFormaPgtoAdapter adapter = new ListaFormaPgtoAdapter(getApplicationContext(),
                listaFormaPgto);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FormaPgto formaPgto = (FormaPgto) adapter.getItem(position);

                Intent intent = new Intent(ListaFormaPgtoActivity.this, CadastroFormaPgtoActivity.class);
                intent.putExtra("formaPgto", formaPgto);
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
                FormaPgto formaPgto = snapshot.getValue(FormaPgto.class);
                listaFormaPgto.add(formaPgto);
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
                Intent intent = new Intent(ListaFormaPgtoActivity.this, CadastroFormaPgtoActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
        startActivityForResult(intent, 0);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ListaFormaPgtoActivity.this, PrincipalActivity.class);
        startActivity(intent);
        finish();
    }
}
