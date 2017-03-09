package com.forcavenda.Fragments.Pedido;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.forcavenda.Adapters.PedidoAdapter;
import com.forcavenda.Dao.PedidoDao;
import com.forcavenda.Entidades.Cliente;
import com.forcavenda.Entidades.FormaPgto;
import com.forcavenda.Entidades.ItemPedido;
import com.forcavenda.Entidades.Pedido;
import com.forcavenda.Enums.Status;
import com.forcavenda.Fragments.Listas.ProdutoFragment;
import com.forcavenda.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 25/02/2017.
 */
public class CadastroPedidoFragment extends DialogFragment {
    private static final String ARG_PEDIDO = "pedido";
    private static final String ARG_CLIENTE = "cliente";
    Pedido pedido;
    Cliente cliente;
    AlertDialog viewPedido;
    List<ItemPedido> itensSelecionados = new ArrayList<ItemPedido>();

    public CadastroPedidoFragment() {
    }

    public static CadastroPedidoFragment newInstance(Pedido pedido, Cliente cliente) {
        CadastroPedidoFragment fragment = new CadastroPedidoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PEDIDO, pedido);
        args.putSerializable(ARG_CLIENTE, cliente);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cliente = (Cliente) getArguments().getSerializable(ARG_CLIENTE);
            pedido = (Pedido) getArguments().getSerializable(ARG_PEDIDO);
        }
    }

//    @Override
//    public void onResume() {
//        // Obter parâmetros de layout existentes para a janela
//        ViewGroup.LayoutParams parametros = getDialog().getWindow().getAttributes();
//        // Atribuir propriedades da janela para preencher a Dialog
//        parametros.width = WindowManager.LayoutParams.MATCH_PARENT;
//        parametros.height = WindowManager.LayoutParams.MATCH_PARENT;
//        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) parametros);
//
//        // Chamar o onResume da classe pai após o redimensionamento
//        super.onResume();
//
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedido_cliente, container, false);
        final Button btn_cancelar = (Button) view.findViewById(R.id.btn_cancelar);
        final Button btn_salvar = (Button) view.findViewById(R.id.btn_salvar);
        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Selecionar itens"));
        tabLayout.addTab(tabLayout.newTab().setText("Concluir pedido"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        final PedidoAdapter adapter = new PedidoAdapter(getChildFragmentManager(), cliente);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Fragment fragment = adapter.getItem(viewPager.getCurrentItem());
                adapter.setClienteSelecionado();
                if (fragment instanceof FinalizaPedidoFragment) {
                    btn_salvar.setText("SALVAR");
                    ((FinalizaPedidoFragment) fragment).setListaItensPedido(itensSelecionados);
                    btn_salvar.setEnabled(itensSelecionados.size() > 0);
                } else if (fragment instanceof SelecionaItensPedidoFragment) {
                    btn_salvar.setText("AVANÇAR");
                    btn_salvar.setEnabled(true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Fragment fragment = adapter.getItem(viewPager.getCurrentItem());
                if (fragment instanceof SelecionaItensPedidoFragment) {
                    itensSelecionados = ((SelecionaItensPedidoFragment) fragment).getItensVenda();
                    if (((SelecionaItensPedidoFragment) fragment).getClienteSelecionado() != null) {
                        cliente = ((SelecionaItensPedidoFragment) fragment).getClienteSelecionado();
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = adapter.getItem(viewPager.getCurrentItem());

                if (fragment instanceof FinalizaPedidoFragment) {
                    //Finaliza o pedido
                    Cliente cliente_pedido;
                    FormaPgto formaPgto;
                    List<ItemPedido> itensPedido;
                    TextView lblValorTotal;

                    cliente_pedido = ((FinalizaPedidoFragment) fragment).getCliente();
                    formaPgto = ((FinalizaPedidoFragment) fragment).getFormaPgto();
                    itensPedido = ((FinalizaPedidoFragment) fragment).getListaItensPedido();
                    lblValorTotal = ((FinalizaPedidoFragment) fragment).getLblValorTotal();

                    boolean online = (!cliente_pedido.getAdmin()) ? true : false;
                    final Pedido pedido = new Pedido(Long.valueOf(String.valueOf(0)), "", cliente, formaPgto, itensPedido,
                            Double.valueOf(lblValorTotal.getText().toString().replace("R$", "").replace(",", ".")), Double.valueOf(String.valueOf(0)), Double.valueOf(lblValorTotal.getText().toString().replace("R$", "").replace(",", ".")), online);

                    final Pedido novo_pedido = InsereNovoPedido(pedido, cliente, formaPgto, itensPedido);

                    PedidoDao pedidoDao = new PedidoDao(getActivity().getApplicationContext(), novo_pedido);
                    pedidoDao.IncluirIdPedido(getActivity());
                    getDialog().dismiss();

                } else if (fragment instanceof SelecionaItensPedidoFragment) {
                    //Avança para a proxima tela
                    viewPager.setCurrentItem(1);
                    adapter.setClienteSelecionado();
                    if (fragment instanceof FinalizaPedidoFragment) {
                        ((FinalizaPedidoFragment) fragment).setListaItensPedido(itensSelecionados);
                    }
                }
            }
        });

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
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
