package com.forcavenda.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.forcavenda.Fragments.FinalizaPedidoFragment;
import com.forcavenda.Fragments.ListaItensFragment;
import com.forcavenda.Fragments.PedidoFragment;

/**
 * Created by Leo on 26/02/2017.
 */

public class PedidoAdapter extends FragmentStatePagerAdapter {

    int qtdeTab;

    public PedidoAdapter(FragmentManager fm, int qtdeTab) {
        super(fm);
        this.qtdeTab = qtdeTab;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ListaItensFragment tab1 = new ListaItensFragment();
                return tab1;
            case 1:
                FinalizaPedidoFragment tab2 = new FinalizaPedidoFragment();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return qtdeTab;
    }
}
