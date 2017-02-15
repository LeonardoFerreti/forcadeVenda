package com.forcavenda.Entidades;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 09/02/2017.
 */

public class FormaPgto implements Serializable {

    //Atributos de uma forma de pagamento
    private String id;
    private String nome;
    private Boolean ativo;

    //retorna o identificador da forma de pagamento
    public String getId() {
        return id;
    }

    //retorna o nome da forma de pagamento
    public String getNome() {
        return nome;
    }

    //retorna a coluna ativo da forma de pagamento
    public Boolean getAtivo() {
        return ativo;
    }

    //construtor padrao da classe
    public FormaPgto() {
    }

    //construtor com os parametros
    public FormaPgto(String id, String nome, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.ativo = ativo;
    }

    //Retorna um HasMap com a propriedades e os valores de um objeto Formapgto
    public static Map<String, Object> MapFormaPgto(FormaPgto formaPgto) {
        HashMap<String, Object> resultado = new HashMap<>();
        resultado.put("id", formaPgto.getId());
        resultado.put("nome", formaPgto.getNome());
        resultado.put("ativo" , formaPgto.getAtivo());
        return resultado;
    }
}
