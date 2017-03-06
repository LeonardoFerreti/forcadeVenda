package com.forcavenda.Fragments.Pedido;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.forcavenda.Adapters.ResumoItensVendaAdapter;
import com.forcavenda.Adapters.SpinnerFormaPgtoAdapter;
import com.forcavenda.Dao.PedidoDao;
import com.forcavenda.Entidades.Cliente;
import com.forcavenda.Entidades.FormaPgto;
import com.forcavenda.Entidades.ItemPedido;
import com.forcavenda.Entidades.Pedido;
import com.forcavenda.Enums.Status;
import com.forcavenda.R;
import com.forcavenda.Telas.Nav_PrincipalActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 26/02/2017.
 */
public class FinalizaPedidoFragment extends Fragment {
    List<FormaPgto> listaFormaPgto = new ArrayList<FormaPgto>();
    List<ItemPedido> listaItensPedido = new ArrayList<ItemPedido>();
    Spinner cboFormaPgto;
    ProgressBar progressBar;
    ResumoItensVendaAdapter resumoItensVendaAdapter;
    ListView listaResumoItens;

    public TextView getLblValorTotal() {
        return lblValorTotal;
    }

    TextView lblValorTotal;
    NumberFormat formatter = NumberFormat.getCurrencyInstance();
    Cliente cliente;
    TextView txt_nomecliente;

    public Cliente getCliente() {
        return cliente;
    }

    public FormaPgto getFormaPgto() {
        return (FormaPgto) cboFormaPgto.getItemAtPosition(cboFormaPgto.getSelectedItemPosition());
    }

    public List<ItemPedido> getListaItensPedido() {
        return listaItensPedido;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        if (cliente != null && txt_nomecliente != null) {
            txt_nomecliente.setText(cliente.getNome());
        }
    }


    public void setListaItensPedido(List<ItemPedido> listaItensPedido) {
        this.listaItensPedido = listaItensPedido;
        if (resumoItensVendaAdapter != null) {
            resumoItensVendaAdapter = new ResumoItensVendaAdapter(getActivity().getApplicationContext(),
                    listaItensPedido);
            listaResumoItens.setAdapter(resumoItensVendaAdapter);
            resumoItensVendaAdapter.notifyDataSetChanged();
            lblValorTotal.setText(formatter.format(resumoItensVendaAdapter.getValorTotal()));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finaliza_pedido, container, false);

        cboFormaPgto = (Spinner) view.findViewById(R.id.cboFormapgto);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        listaResumoItens = (ListView) view.findViewById(R.id.lista_resumo_pedido);
        txt_nomecliente = (TextView) view.findViewById(R.id.txt_nomeCliente);
//        btn_salvar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatabaseReference tabPedido = FirebaseDatabase.getInstance().getReference().child("pedido");
//                Cliente cliente_pedido;
//                FormaPgto formaPgto;
//                List<ItemPedido> itensPedido;
//
//                cliente_pedido = cliente;
//                formaPgto = (FormaPgto) cboFormaPgto.getItemAtPosition(cboFormaPgto.getSelectedItemPosition());
//                itensPedido = listaItensPedido;
//
//                boolean online = (cliente_pedido.getAdmin() == false) ? true : false;
//                final Pedido pedido = new Pedido(Long.valueOf(String.valueOf(0)), "", cliente, formaPgto, listaItensPedido,
//                        Double.valueOf(lblValorTotal.getText().toString().replace("R$", "").replace(",", ".")), Double.valueOf(String.valueOf(0)), Double.valueOf(lblValorTotal.getText().toString().replace("R$", "").replace(",", ".")), online);
//
//                final Pedido novo_pedido = InsereNovoPedido(pedido, cliente, formaPgto, itensPedido);
//
//                PedidoDao pedidoDao = new PedidoDao(getActivity().getApplicationContext(), novo_pedido);
//                pedidoDao.IncluirIdPedido(getActivity());
//
//            }
//        });

        View header = (View) getLayoutInflater(savedInstanceState).inflate(R.layout.lista_resumo_pedido_header, null);
        View footer = (View) getLayoutInflater(savedInstanceState).inflate(R.layout.lista_resumo_pedido_footer, null);

        lblValorTotal = (TextView) footer.findViewById(R.id.lbl_valorTotal);
        lblValorTotal.setText(formatter.format(0));

        listaResumoItens.addHeaderView(header);
        listaResumoItens.setEmptyView(view.findViewById(android.R.id.empty));
        listaResumoItens.addFooterView(footer);

        progressBar.setVisibility(View.VISIBLE);

        DefineAdapterResumo();

        final FirebaseDatabase banco = FirebaseDatabase.getInstance();
        DatabaseReference tabFormaPgto = banco.getReference("formaPgto");
        Query resultadoFormaPgto = tabFormaPgto.orderByChild("nome");
        final SpinnerFormaPgtoAdapter adapterFormaPgto = new SpinnerFormaPgtoAdapter(getActivity().getApplicationContext(), listaFormaPgto);
        cboFormaPgto.setAdapter(adapterFormaPgto);

        resultadoFormaPgto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
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

        });
        return view;
    }

    private void DefineAdapterResumo() {
        if (resumoItensVendaAdapter == null) {
            resumoItensVendaAdapter = new ResumoItensVendaAdapter(getActivity().getApplicationContext(),
                    listaItensPedido);
            listaResumoItens.setAdapter(resumoItensVendaAdapter);
            resumoItensVendaAdapter.notifyDataSetChanged();
        }
    }

    //Rotina responsavel por incluir um pedido
    public Pedido InsereNovoPedido(Pedido pedido_ins, Cliente cliente, FormaPgto formaPgto, List<ItemPedido> itensPedido) {
        //Recupera a instancia do Banco de dados da aplicação
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Recupera a raiz do nó do banco de dados
        DatabaseReference ref = database.getReference();
        //captura o identificador do pedido
        String chave = ref.child("pedido").push().getKey();
        //Mapeia o objeto pedido
        Pedido pedido = new Pedido(chave, pedido_ins.getIdPedido(), pedido_ins.getValorTotal(), pedido_ins.getDesconto(),
                pedido_ins.getValorPago(), pedido_ins.getOnline(), Status.Pendente);

        PedidoDao pedidoDao = new PedidoDao(getActivity().getApplicationContext(), pedido);
        DatabaseReference refNovoPedido = pedidoDao.IncluirNoRegistro(ref, chave, Pedido.MapPedido(pedido));

        Map<String, Object> objDao = new HashMap<>();
        objDao.put("cliente", cliente);
        refNovoPedido.updateChildren(objDao);

        objDao = new HashMap<>();
        objDao.put("formaPgto", formaPgto);
        refNovoPedido.updateChildren(objDao);

        for (ItemPedido item : itensPedido) {
            //captura o identificador do item do pedido
            String chave_item = refNovoPedido.child("itens").push().getKey();

            objDao = new HashMap<>();
            objDao.put("item", item);
            refNovoPedido.child("itens").child(chave_item).updateChildren(objDao);
        }

        return pedido;
    }

}
