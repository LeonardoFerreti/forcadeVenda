package com.forcavenda.Fragments.Cadastros;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.forcavenda.Dao.ProdutoDao;
import com.forcavenda.Entidades.Produto;
import com.forcavenda.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Leo on 01/03/2017.
 */

public class CadastroProdutoFragment extends DialogFragment {

    private static final String PARAM_PRODUTO = "produto";
    public Produto produto;
    private DatabaseReference ref; //Instancia uma variavel para recuperar a referencia do nó
    DatabaseReference tabProduto; //Instancia uma variavel para recuperar a referencia do produto selecionado.

    android.support.v7.widget.AppCompatCheckBox checkBox;
    TextInputLayout input_nome;
    TextInputLayout input_preco;
    TextInputLayout input_descricao;
    EditText txt_nome;
    EditText txt_preco;
    EditText txt_descricao;
    ProgressBar progressBarCadastro;

    public CadastroProdutoFragment() {
    }

    public static CadastroProdutoFragment newInstance(Produto produto) {
        CadastroProdutoFragment fragment = new CadastroProdutoFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAM_PRODUTO, produto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            produto = (Produto) getArguments().getSerializable(PARAM_PRODUTO);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.salvar, null);

        LayoutInflater i = getActivity().getLayoutInflater();
        final View view = i.inflate(R.layout.activity_cadastro_produto, null);

        //Recupera a instancia do Banco de dados da aplicação
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //recupera a raiz do nó do banco de dados
        ref = database.getReference();

        if (produto != null) {
            tabProduto = database.getReference("produto").child(produto.getId());
        }

        input_nome = (TextInputLayout) view.findViewById(R.id.input_nome);
        input_preco = (TextInputLayout) view.findViewById(R.id.input_preco);
        input_descricao = (TextInputLayout) view.findViewById(R.id.input_descricao);
        txt_nome = (EditText) view.findViewById(R.id.txt_nome);
        txt_preco = (EditText) view.findViewById(R.id.txt_preco);
        txt_descricao = (EditText) view.findViewById(R.id.txt_descricao);
        checkBox = (android.support.v7.widget.AppCompatCheckBox) view.findViewById(R.id.chk_ativo);
        progressBarCadastro = (ProgressBar) view.findViewById(R.id.progressBar);

        if (produto != null) {
            progressBarCadastro.setVisibility(View.VISIBLE);

            tabProduto.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    produto = dataSnapshot.getValue(Produto.class);

                    txt_nome.setText(produto.getNome().toString());
                    txt_preco.setText(produto.getPreco().toString());
                    txt_descricao.setText(produto.getDescricao().toString());
                    checkBox.setChecked(produto.getAtivo());
                    progressBarCadastro.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        builder.setView(view);

        View titulo = i.inflate(R.layout.layout_titulo_fragment, null);
        TextView txt_titulo = (TextView) titulo.findViewById(R.id.txt_1);
        txt_titulo.setText(R.string.cadastrar_produto);
        builder.setCustomTitle(titulo);

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btn_salvar = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btn_salvar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (com.forcavenda.Util.Util.estaConectadoInternet(getActivity().getApplicationContext())) {
                            if (validaCampos()) {
                                try {
                                    //Recupera a instancia do Banco de dados da aplicação
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    //recupera a raiz do nó do banco de dados
                                    ref = database.getReference();
                                    //captura o identificador do Produto
                                    String chave = (produto == null) ? ref.child("produto").push().getKey() : produto.getId();
                                    //Mapeia o objeto produto com os parametros identificador, nome e a coluna ativo
                                    Produto novoProduto = new Produto(chave, txt_nome.getText().toString().trim(), Double.valueOf(txt_preco.getText().toString().trim()),
                                            checkBox.isChecked(), txt_descricao.getText().toString().trim());
                                    //Chama a classe de CRUD de forma de pagamento, fazendo referencia ao nó raiz do cadastro de Produto
                                    ProdutoDao produtoDao = new ProdutoDao();
                                    produtoDao.IncluirAlterar(ref, chave, novoProduto.MapFormaPgto(novoProduto));
                                    String texto = (produto == null) ? "incluído" : "alterado";
                                    Toast.makeText(getActivity().getApplicationContext(), "Produto " + texto + " com sucesso.", Toast.LENGTH_SHORT).show();
                                    produto = novoProduto;
                                    getDialog().dismiss();

                                } catch (Exception e) {
                                    Toast.makeText(getActivity().getApplicationContext(), R.string.erro_salvar_dados, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), R.string.verificar_dados_informados, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), R.string.verificar_conexao, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        return alertDialog;
    }

    public Boolean validaCampos() {
        Boolean validado = true;
        if (txt_nome.getText().length() == 0) {
            validado = false;
            input_nome.setError(getString(R.string.nome_produto_vazio));
        } else {
            input_nome.setError("");
        }
        if (txt_preco.getText().length() == 0) {
            validado = false;
            input_preco.setError(getString(R.string.preco_produto_vazio));
        } else {
            input_preco.setError("");
        }
        if (txt_descricao.getText().length() == 0) {
            validado = false;
            input_descricao.setError(getString(R.string.descricao_produto_vazio));
        } else {
            input_descricao.setError("");
        }

        return validado;
    }

}
