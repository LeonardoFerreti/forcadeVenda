package com.forcavenda.Telas;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


import com.forcavenda.Dao.ClienteDao;
import com.forcavenda.Entidades.Cliente;
import com.forcavenda.Fragments.ClienteFragment;
import com.forcavenda.Fragments.FormaPgtoFragment;
import com.forcavenda.Fragments.PerfilFragment;
import com.forcavenda.Fragments.ProdutoFragment;
import com.forcavenda.Fragments.TrocaSenhaFragment;
import com.forcavenda.R;
import com.forcavenda.Util.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Nav_PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // private ProgressDialog progressDialog;
    private Cliente clienteLogado;
    private boolean viewHome;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedpreferences = getSharedPreferences(Util.PREFERENCIA, Context.MODE_PRIVATE);

        //  progressDialog = Util.CriaProgressDialog(getApplicationContext());
        //  progressDialog.show();

        //Inserir nome e email do usuario no cabeçalho
        informacoesCabecalho();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "chamar a acao de cadastrar pedidos", Snackbar.LENGTH_LONG)
                        .setAction("Ação", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        verificaUsuarioSimples();
        navView.getMenu().getItem(0).setChecked(true);
    }

    private void informacoesCabecalho() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);

        TextView txt_nome_usuario = (TextView) header.findViewById(R.id.txt_nome);
        TextView txt_email_usuario = (TextView) header.findViewById(R.id.txt_email);

        txt_nome_usuario.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        txt_email_usuario.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }

    private void verificaUsuarioSimples() {
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        final Query refUsuarioCliente = ref.child("cliente").orderByChild("email").equalTo(user.getEmail());

        refUsuarioCliente.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //se nao foi encontrado um valor valido para o email, entao insere o cliente com os dados do usuario atual
                if (dataSnapshot.getValue() == null) {
                    Log.i("cliente-usuario:", "Cliente ainda não cadastrado, será cadastrado e vinculado ao usuário.");
                    //Gera um identificador para o cliente
                    String chave = ref.child("cliente").push().getKey();

                    //Chama a classe de CRUD de usuário, fazendo referencia ao nó do banco de dados
                    Cliente cliente_usuario_ins = new Cliente(chave, "", user.getEmail(), user.getUid(), false, "");

                    //Chama a classe de CRUD de cliente, fazendo referencia ao nó raiz do cadastro de cliente
                    ClienteDao clienteDao = new ClienteDao();
                    clienteDao.IncluirAlterar(ref, chave, Cliente.MapCliente(cliente_usuario_ins));

                    escondeItensUsuarioSimples();
                    clienteLogado = cliente_usuario_ins;
                    editor.putString(Util.chaveCliente, chave);
                    editor.putBoolean(Util.isAdmin, false);

                } else {
                    Cliente cliente = null;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        cliente = snapshot.getValue(Cliente.class);
                    }
                    //verifico se o identificador desse cliente está vazio. Se estiver, tenho que vincular.(Esse caso foi o usuario
                    // admin que cadastrou, entao usuario completa seu cadastro.)
                    if (cliente.getId_usuario() == null || cliente.getId_usuario().equals("")) {
                        ref.child("cliente").child(cliente.getId()).child("id_usuario").setValue(user.getUid());
                        Log.i("cliente-usuario:", "Email já cadastrado anteriormente, vinculando usuário.");
                    } else {
                        Log.i("cliente-usuario:", "Email já vinculado ao usuário.");
                    }
                    if (!cliente.getAdmin()) {
                        escondeItensUsuarioSimples();
                    }
                    clienteLogado = cliente;
                    editor.putString(Util.chaveCliente, cliente.getId());
                    editor.putBoolean(Util.isAdmin, cliente.getAdmin());
                }

                //        progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void escondeItensUsuarioSimples() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_clientes).setVisible(false);
        nav_Menu.findItem(R.id.nav_produtos).setVisible(false);
        nav_Menu.findItem(R.id.nav_forma_pgto).setVisible(false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav__principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displayView(item.getItemId());
        return true;
    }


    public void displayView(int viewId) {

        Fragment fragment = null;
        String titulo = getString(R.string.app_name);

        switch (viewId) {
            case R.id.nav_perfil:
                fragment = PerfilFragment.newInstance(clienteLogado);
                titulo = "Perfil";
                break;
            case R.id.nav_clientes:
                fragment = new ClienteFragment();
                titulo = "Clientes";
                viewHome = true;
                break;
            case R.id.nav_produtos:
                fragment = new ProdutoFragment();
                titulo = "Produtos";
                viewHome = false;
                break;
            case R.id.nav_forma_pgto:
                fragment = new FormaPgtoFragment();
                titulo = "Formas de pagamento";
                viewHome = false;
                break;
            case R.id.nav_trocar_senha:
                fragment = new TrocaSenhaFragment();
                titulo = "Trocar senha";
                viewHome = false;
                break;

        }

        if (viewId == R.id.nav_sair) {

            AlertDialog.Builder builder = new AlertDialog.Builder(Nav_PrincipalActivity.this);
            builder.setMessage("Deseja realmente sair do sistema?");
            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(Nav_PrincipalActivity.this, LoginActivity.class));
                    finish();
                }
            });
            builder.create().show();

        } else if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_container, fragment);
            ft.commit();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titulo);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
