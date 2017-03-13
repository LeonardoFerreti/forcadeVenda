package com.forcavenda.Fragments.Pedido;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.forcavenda.Adapters.ResumoItensVendaAdapter;
import com.forcavenda.Adapters.SpinnerFormaPgtoAdapter;
import com.forcavenda.Adapters.SpinnerStatusAdapter;
import com.forcavenda.Dao.PedidoDao;
import com.forcavenda.Entidades.Cliente;
import com.forcavenda.Entidades.ItemPedido;
import com.forcavenda.Entidades.Pedido;
import com.forcavenda.Enums.Status;
import com.forcavenda.R;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Leo on 12/03/2017.
 */

public class PedidoFragment extends DialogFragment {
    private static final String ARG_PEDIDO = "pedido";
    private static final String ARG_CLIENTE = "cliente";
    NumberFormat formatter = NumberFormat.getCurrencyInstance();
    Spinner cboStatusPedido;

    Pedido pedido;
    Cliente clienteLogado;

    //construtor padrão
    public PedidoFragment() {
    }

    //construtor do fragment sendo enviado um objeto do tipo pedido
    public static PedidoFragment newInstance(Pedido pedido, Cliente clienteLogado) {
        PedidoFragment fragment = new PedidoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PEDIDO, pedido);
        args.putSerializable(ARG_CLIENTE, clienteLogado);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pedido = (Pedido) getArguments().getSerializable(ARG_PEDIDO);
            clienteLogado = (Cliente) getArguments().getSerializable(ARG_CLIENTE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.salvar, null);

        LayoutInflater i = getActivity().getLayoutInflater();
        View view = i.inflate(R.layout.fragment_pedido, null);

        TextView txtNomeCliente = (TextView) view.findViewById(R.id.txt_nomeCliente);
        TextView txtTelefone = (TextView) view.findViewById(R.id.txt_numero_telefone);
        TextView txtEndereco = (TextView) view.findViewById(R.id.txt_nomeEndereco);
        TextView txtFormaPgto = (TextView) view.findViewById(R.id.txt_nomeFormaPgto);
        TextView txtStatusPedido = (TextView) view.findViewById(R.id.txt_nomeStatusPedido);
        LinearLayout linearLayoutUsuarioSimples = (LinearLayout) view.findViewById(R.id.linearStatusClienteSimples);
        LinearLayout linearLayoutUsuarioAdmin = (LinearLayout) view.findViewById(R.id.linearStatusClienteAdmin);
        cboStatusPedido = (Spinner) view.findViewById(R.id.cboStatus);
        ListView listaResumoItens = (ListView) view.findViewById(R.id.lista_resumo_pedido);

        View header = getActivity().getLayoutInflater().inflate(R.layout.lista_resumo_pedido_header, null);
        View footer = getActivity().getLayoutInflater().inflate(R.layout.lista_resumo_pedido_footer, null);

        TextView lblValorTotal = (TextView) footer.findViewById(R.id.lbl_valorTotal);
        lblValorTotal.setText(formatter.format(pedido.getValorTotal()));

        listaResumoItens.addHeaderView(header);
        listaResumoItens.setEmptyView(view.findViewById(android.R.id.empty));
        listaResumoItens.addFooterView(footer);

        List<ItemPedido> listaItensPedido = pedido.getListaItens();

        ResumoItensVendaAdapter resumoItensVendaAdapter = new ResumoItensVendaAdapter(getActivity().getApplicationContext(),
                listaItensPedido);
        listaResumoItens.setAdapter(resumoItensVendaAdapter);

        if (clienteLogado != null && clienteLogado.getAdmin()) {
            linearLayoutUsuarioSimples.setVisibility(View.GONE);
            //Define o adapter da combobox de status
            List<Status> lista = Arrays.asList(Status.values());
            final SpinnerStatusAdapter adapter = new SpinnerStatusAdapter(getActivity().getApplicationContext(), lista);
            cboStatusPedido.setAdapter(adapter);
            if (!pedido.getStatus().equals(null)) {
                cboStatusPedido.setSelection(pedido.getStatus().ordinal());
            }
        } else {
            linearLayoutUsuarioAdmin.setVisibility(View.GONE);
            txtStatusPedido.setText(pedido.getStatus().getDescricao());
        }
        //Define o texto do nome do cliente
        txtNomeCliente.setText(pedido.getCliente().getNome());
        //Define o texto do telefone
        txtTelefone.setText(pedido.getCliente().getTelefone());
        //Define o texto do endereço
        txtEndereco.setText((pedido.getCliente().getEndereco().getLogradouro() != null) ? pedido.getCliente().getEndereco().getLogradouro() + ", " +
                pedido.getCliente().getEndereco().getNumero() + ", " +
                pedido.getCliente().getEndereco().getComplemento() + ", " +
                pedido.getCliente().getEndereco().getReferencia() :
                pedido.getCliente().getEndereco().getReferencia());
        //Define o texto da forma de pagamento
        txtFormaPgto.setText(pedido.getFormaPgto().getNome());
        //Define o view do Dialog
        builder.setView(view);

        //Define o titulo do DialogFragment
        View titulo = i.inflate(R.layout.layout_titulo_fragment, null);
        TextView txt_titulo = (TextView) titulo.findViewById(R.id.txt_1);
        txt_titulo.setText(getResources().getString(R.string.resumo_pedido) + " " + pedido.getIdPedido().toString());
        builder.setCustomTitle(titulo);

        final AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btn_salvar = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btn_salvar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PedidoDao pedidoDao = new PedidoDao(getActivity().getApplicationContext(), pedido);
                        pedidoDao.alteraStatus((Status) cboStatusPedido.getItemAtPosition(cboStatusPedido.getSelectedItemPosition()));
                        getDialog().dismiss();
                    }
                });
            }
        });

        return alertDialog;
    }

}
