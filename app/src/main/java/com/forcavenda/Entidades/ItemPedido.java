package com.forcavenda.Entidades;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 12/02/2017.
 */
public class ItemPedido implements Serializable {

    //Atributo de Produto
    private Produto produto;
    //Atributo de quantidade
    private Long Quantidade;

    //Getters e Setters
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

    //Construtor padrão
    public ItemPedido() {
    }

    //Construtor com os parametros
    public ItemPedido(Produto produto, Long quantidade) {
        this.produto = produto;
        Quantidade = quantidade;
    }
}
