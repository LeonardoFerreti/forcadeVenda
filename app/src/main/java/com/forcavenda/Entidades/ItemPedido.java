package com.forcavenda.Entidades;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 12/02/2017.
 */

public class ItemPedido implements Serializable {

    private Produto produto;
    private int Quantidade;

    public void setQuantidade(int quantidade) {
        Quantidade = quantidade;
    }

    public int getQuantidade() {
        return Quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public ItemPedido() {
    }

    public ItemPedido(Produto produto, int quantidade) {
        this.produto = produto;
        Quantidade = quantidade;
    }


}
