package com.forcavenda.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.forcavenda.Entidades.ItemPedido;
import com.forcavenda.Fragments.Pedido.FinalizaPedidoFragment;
import com.forcavenda.Fragments.Pedido.SelecionaItensPedidoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 26/02/2017.
 */

public class PedidoAdapter extends FragmentStatePagerAdapter {

    private List<ItemPedido> itensSelecionados = new ArrayList<ItemPedido>();
    private static final int QTDE_DE_TAB = 2;
    private Fragment[] tabList = new Fragment[QTDE_DE_TAB];

    public List<ItemPedido> getItensSelecionados() {
        List<ItemPedido> lista = null;
        if (tabList != null && tabList[0] != null) {
            SelecionaItensPedidoFragment selecionaItensPedidoFragment = (SelecionaItensPedidoFragment) tabList[0];
            lista = selecionaItensPedidoFragment.getItensVenda();
        }
        return lista;
    }

    public void setItensSelecionados() {
        if (tabList != null && tabList[1] != null) {
            FinalizaPedidoFragment finalizaPedidoFragment = (FinalizaPedidoFragment) tabList[1];
            finalizaPedidoFragment.setListaItensPedido(getItensSelecionados());
        }
    }

    public PedidoAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (tabList[position] != null) {
            //retorna a tab anteriormente criada
            return tabList[position];
        } else {
            switch (position) {
                case 0:
                    tabList[0] = new SelecionaItensPedidoFragment();
                    return tabList[0];
                case 1:
                    tabList[1] = new FinalizaPedidoFragment();
                    return tabList[1];
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return QTDE_DE_TAB;
    }
}
