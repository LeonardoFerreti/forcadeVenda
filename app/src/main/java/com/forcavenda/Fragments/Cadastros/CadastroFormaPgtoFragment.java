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
import android.widget.TextView;
import android.widget.Toast;

import com.forcavenda.Dao.FormaPgtoDao;
import com.forcavenda.Entidades.FormaPgto;
import com.forcavenda.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Leo on 01/03/2017.
 */
public class CadastroFormaPgtoFragment extends DialogFragment {
    private static final String PARAM_FORMAPGTO = "formaPgto";
    public FormaPgto formaPgto;
    private DatabaseReference ref; //Instancia uma variavel para recuperar a referencia do nó

    android.support.v7.widget.AppCompatCheckBox checkBox;
    TextInputLayout input_nome;
    EditText txt_nome;

    public CadastroFormaPgtoFragment() {
    }

    public static CadastroFormaPgtoFragment newInstance(FormaPgto formaPgto) {
        CadastroFormaPgtoFragment fragment = new CadastroFormaPgtoFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAM_FORMAPGTO, formaPgto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            formaPgto = (FormaPgto) getArguments().getSerializable(PARAM_FORMAPGTO);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.salvar, null);

        LayoutInflater i = getActivity().getLayoutInflater();
        final View view = i.inflate(R.layout.fragment_cadastro_forma_pgto, null);

        //Recupera a instancia do Banco de dados da aplicação
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //recupera a raiz do nó do banco de dados
        ref = database.getReference();

        input_nome = (TextInputLayout) view.findViewById(R.id.input_nome);
        txt_nome = (EditText) view.findViewById(R.id.txt_nome);
        checkBox = (android.support.v7.widget.AppCompatCheckBox) view.findViewById(R.id.chk_ativo);

        if (formaPgto != null) {
            txt_nome.setText(formaPgto.getNome().toString());
            checkBox.setChecked(formaPgto.getAtivo());
        }

        builder.setView(view);

        View titulo = i.inflate(R.layout.layout_titulo_fragment, null);
        TextView txt_titulo = (TextView) titulo.findViewById(R.id.txt_1);
        txt_titulo.setText((formaPgto == null) ? R.string.cadastrar_forma_pgto : R.string.alterar_forma_pgto);
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
                                    //captura o identificador da forma de pagamento
                                    String chave = (formaPgto == null) ? ref.child("formaPgto").push().getKey() : formaPgto.getId();
                                    //Mapeia o objeto forma de pagamento com os parametros identificador, nome e a coluna ativo
                                    FormaPgto novaformaPgto = new FormaPgto(chave, txt_nome.getText().toString().trim(), checkBox.isChecked());
                                    //Chama a classe de CRUD de forma de pagamento, fazendo referencia ao nó raiz do cadastro de forma de pagamento
                                    FormaPgtoDao formaPgtoDao = new FormaPgtoDao();
                                    //Monta o texto a ser apresentado ao usuário
                                    String texto = (formaPgto == null) ? "incluída" : "alterada";
                                    texto = "Forma de pagamento " + texto + " com sucesso.";
                                    //Atualiza a forma de pagamento
                                    formaPgtoDao.IncluirAlterar(getActivity().getApplicationContext(), chave, novaformaPgto.MapFormaPgto(novaformaPgto),texto);
                                    formaPgto = novaformaPgto;
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
            input_nome.setError(getString(R.string.nome_formaPgto_vazio));
        } else {
            input_nome.setError("");
        }

        return validado;
    }
}


