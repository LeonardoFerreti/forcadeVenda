package com.forcavenda.Entidades;

import java.io.Serializable;

/**
 * Created by Leo on 12/02/2017.
 */

public class ItemPedido implements Serializable {

    private Produto produto;
    private int Quantidade;

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
