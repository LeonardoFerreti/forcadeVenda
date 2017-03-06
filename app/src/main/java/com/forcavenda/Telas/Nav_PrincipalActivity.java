package com.forcavenda.Telas;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;


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
import android.widget.TextView;


import com.forcavenda.Dao.ClienteDao;
import com.forcavenda.Entidades.Cliente;
import com.forcavenda.Fragments.Cadastros.CadastroClienteFragment;
import com.forcavenda.Fragments.Cadastros.CadastroFormaPgtoFragment;
import com.forcavenda.Fragments.Cadastros.CadastroProdutoFragment;
import com.forcavenda.Fragments.Listas.ClienteFragment;
import com.forcavenda.Fragments.Listas.FormaPgtoFragment;
import com.forcavenda.Fragments.Listas.PedidoFragment;
import com.forcavenda.Fragments.Pedido.CadastroPedidoFragment;
import com.forcavenda.Fragments.Cadastros.CadastroPerfilFragment;
import com.forcavenda.Fragments.Listas.ProdutoFragment;
import com.forcavenda.Fragments.TrocaSenhaFragment;
import com.forcavenda.R;
import com.forcavenda.Util.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Nav_PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Cliente clienteLogado;
    private boolean viewHome;
    ProgressDialog progressDialog;
    SharedPreferences sharedpreferences;
    FloatingActionButton floatButton;
    MenuItem itemSelecionado;
    NavigationView navView;
    boolean blnCommitFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedpreferences = getSharedPreferences(Util.PREFERENCIA, Context.MODE_PRIVATE);
        progressDialog = Util.CriaProgressDialog(this);
        progressDialog.show();

        //Inserir nome e email do usuario no cabeçalho
        informacoesCabecalho();

        //Recupera o floatActionButton, botão que chama os novos cadastros das entidades
        floatButton = (FloatingActionButton) findViewById(R.id.fab);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                switch (itemSelecionado.getItemId()) {
                    case R.id.nav_clientes:
                        CadastroClienteFragment fragmentCliente = CadastroClienteFragment.newInstance(null);
                        fragmentCliente.show(fm, "Cadastrar cliente");
                        break;
                    case R.id.nav_forma_pgto:
                        CadastroFormaPgtoFragment fragmentFormaPgto = CadastroFormaPgtoFragment.newInstance(null);
                        fragmentFormaPgto.show(fm, "Cadastrar forma de pagamento");
                        break;
                    case R.id.nav_produtos:
                        CadastroProdutoFragment fragmentProduto = CadastroProdutoFragment.newInstance(null);
                        fragmentProduto.show(fm, "Cadastrar produto");
                        break;
                    case R.id.nav_criar_pedido:
                        CadastroPedidoFragment fragmentPedido   = CadastroPedidoFragment.newInstance(null,clienteLogado);
                        fragmentPedido.show(fm, "Cadastrar pedido");
                        break;
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,  R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        verificaUsuarioSimples();
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

        refUsuarioCliente.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                clienteLogado = dataSnapshot.getValue(Cliente.class);
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
        });
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
                    if (cliente.getAdmin()) {
                        mostraItensUsuarioSimples();
                    }
                    Log.i("cliente admin:", cliente.getAdmin().toString());

                    clienteLogado = cliente;
                    editor.putString(Util.chaveCliente, cliente.getId());
                    editor.putBoolean(Util.isAdmin, cliente.getAdmin());
                }

                progressDialog.dismiss();

                //Seleciona o primeiro item de Menu
                navView.getMenu().getItem(0).setChecked(true);
                onNavigationItemSelected(navView.getMenu().getItem(0));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void mostraItensUsuarioSimples() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_clientes).setVisible(true);
        nav_Menu.findItem(R.id.nav_produtos).setVisible(true);
        nav_Menu.findItem(R.id.nav_forma_pgto).setVisible(true);
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
        // getMenuInflater().inflate(R.menu.nav__principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        itemSelecionado = item;
        displayView(item.getItemId());
        return true;
    }

    public void displayView(int viewId) {

        Fragment fragment = null;
        String titulo = getString(R.string.app_name);

        switch (viewId) {
            case R.id.nav_perfil:
                fragment = CadastroPerfilFragment.newInstance(clienteLogado);
                titulo = "Meu perfil";
                floatButton.setVisibility(View.GONE);
                break;
            case R.id.nav_clientes:
                fragment = new ClienteFragment();
                titulo = "Clientes cadastrados";
                viewHome = true;
                floatButton.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_produtos:
                fragment = new ProdutoFragment();
                titulo = "Produtos";
                viewHome = false;
                floatButton.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_forma_pgto:
                fragment = new FormaPgtoFragment();
                titulo = "Formas de pagamento";
                viewHome = false;
                floatButton.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_criar_pedido:
                fragment = new PedidoFragment();
                titulo = "Pedidos";
                viewHome = false;
                floatButton.setVisibility(View.VISIBLE);
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
            if (blnCommitFragment) {
                ft.commit();
            }
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titulo);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        blnCommitFragment = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        blnCommitFragment = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        blnCommitFragment = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        blnCommitFragment = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        blnCommitFragment = false;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        blnCommitFragment = true;
    }
}
