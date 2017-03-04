package com.forcavenda.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.forcavenda.Entidades.Cliente;
import com.forcavenda.Entidades.ItemPedido;
import com.forcavenda.Fragments.Pedido.FinalizaPedidoFragment;
import com.forcavenda.Fragments.Pedido.SelecionaItensPedidoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 26/02/2017.
 */

public class PedidoAdapter extends FragmentStatePagerAdapter {
    private Cliente clienteLogado;
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

    public Cliente getClienteSelecionado(){
        Cliente cliente =null;
        if (tabList!=null && tabList[0] !=null){
            SelecionaItensPedidoFragment selecionaItensPedidoFragment = (SelecionaItensPedidoFragment) tabList[0];
            cliente = selecionaItensPedidoFragment.getClienteSelecionado();
        }
        return  cliente;
    }

    public void setClienteSelecionado(){
        FinalizaPedidoFragment finalizaPedidoFragment = (FinalizaPedidoFragment) tabList[1];
        finalizaPedidoFragment.setCliente(getClienteSelecionado());
    }

    public void setItensSelecionados() {
        if (tabList != null && tabList[1] != null) {
            FinalizaPedidoFragment finalizaPedidoFragment = (FinalizaPedidoFragment) tabList[1];
            finalizaPedidoFragment.setListaItensPedido(getItensSelecionados());
        }
    }

    public PedidoAdapter(FragmentManager fm,Cliente clienteLogado) {
        super(fm);
        this.clienteLogado = clienteLogado;
    }

    @Override
    public Fragment getItem(int position) {
        if (tabList[position] != null) {
            //retorna a tab anteriormente criada
            return tabList[position];
        } else {
            switch (position) {
                case 0:
                    SelecionaItensPedidoFragment selecionaItensPedidoFragment = new SelecionaItensPedidoFragment();
                    selecionaItensPedidoFragment.setCliente(this.clienteLogado);
                    tabList[0] = selecionaItensPedidoFragment;
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
