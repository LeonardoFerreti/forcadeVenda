package com.forcavenda.Entidades;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 09/02/2017.
 */
public class Produto implements Serializable {

    //atributos da classe
    private String id;
    private String nome;
    private Double preco;
    private Boolean ativo;

    //retorna o identificador do produto
    public String getId() {
        return id;
    }

    //retorna se o produto está ativo
    public Boolean getAtivo() {
        return ativo;
    }
    //retorna o nome do produto
    public String getNome() {
        return nome;
    }

    //retorna o preco do produto
    public Double getPreco() {
        return preco;
    }

    //Construtor padrao
    public Produto() {
    }

    //Construtor com os parametros
    public Produto(String id, String nome, Double preco, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.ativo= ativo;
    }

    //Retorna um HasMap com a propriedades e os valores de um objeto Produto
    public static Map<String, Object> MapFormaPgto(Produto produto) {
        HashMap<String, Object> resultado = new HashMap<>();
        resultado.put("id", produto.getId());
        resultado.put("nome", produto.getNome());
        resultado.put("preco" , produto.getPreco());
        resultado.put("ativo" , produto.getAtivo());
        return resultado;
    }

}
