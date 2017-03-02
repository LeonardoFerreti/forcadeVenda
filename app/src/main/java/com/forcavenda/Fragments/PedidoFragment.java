package com.forcavenda.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forcavenda.Adapters.PedidoAdapter;
import com.forcavenda.Entidades.Cliente;
import com.forcavenda.Entidades.ItemPedido;
import com.forcavenda.Fragments.Listas.ProdutoFragment;
import com.forcavenda.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 25/02/2017.
 */
public class PedidoFragment extends Fragment {
    private static final String ARG_CLIENTE = "cliente";
    Cliente cliente;
    List<ItemPedido> itensSelecionados = new ArrayList<ItemPedido>();

    public PedidoFragment() {
    }

    public static ProdutoFragment newInstance(Cliente cliente) {
        ProdutoFragment fragment = new ProdutoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLIENTE, cliente);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cliente = (Cliente) getArguments().getSerializable(ARG_CLIENTE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedido_cliente, container, false);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Selecionar itens"));
        tabLayout.addTab(tabLayout.newTab().setText("Concluir pedido"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        final PedidoAdapter adapter = new PedidoAdapter(getFragmentManager());


        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Fragment fragment = adapter.getItem(viewPager.getCurrentItem());

                if (fragment instanceof FinalizaPedidoFragment) {
                    ((FinalizaPedidoFragment) fragment).setListaItensPedido(itensSelecionados);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Fragment fragment = adapter.getItem(viewPager.getCurrentItem());
                if (fragment instanceof ListaItensFragment) {
                    itensSelecionados = ((ListaItensFragment) fragment).getItensVenda();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }
}
