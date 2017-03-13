package com.forcavenda.Entidades;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 12/02/2017.
 */

public class ItemPedido implements Serializable {
    private Produto produto;
    private Long Quantidade;

    public void setQuantidade(Long quantidade) {
        Quantidade = quantidade;
    }

    public Long getQuantidade() {
        return Quantidade;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Produto getProduto() {
        return produto;
    }

    public ItemPedido() {
    }

    public ItemPedido(Produto produto, Long quantidade) {
        this.produto = produto;
        Quantidade = quantidade;
    }


}
