package com.forcavenda.Entidades;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 08/02/2017.
 */
public class Endereco implements Serializable {

    //Parametros
    String logradouro;
    String numero;
    String complemento;
    String cep;
    String referencia;

    //construtor padrão
    public Endereco() {
    }

    //construtor com os parametros
    public Endereco(String logradouro, String numero, String complemento, String cep, String referencia) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.cep = cep;
        this.referencia = referencia;
    }

    //retorna a referencia do endereco
    public String getReferencia() {
        return referencia;
    }

    //retorna o CEP
    public String getCep() {
        return cep;
    }

    //retorna o complemento
    public String getComplemento() {
        return complemento;
    }

    //retorna o numero do endereco
    public String getNumero() {
        return numero;
    }

    //retorna o nome da rua
    public String getLogradouro() {
        return logradouro;
    }

    //Retorna um HasMap com as propriedades e os valores de um objeto Endereco
    public static Map<String, Object> MapEndereco(Endereco endereco) {
        HashMap<String, Object> resultado = new HashMap<>();
        resultado.put("logradouro", endereco.getLogradouro());
        resultado.put("numero", endereco.getNumero());
        resultado.put("complemento", endereco.getComplemento());
        resultado.put("cep", endereco.getCep());
        resultado.put("referencia", endereco.getReferencia());
        return resultado;
    }
}
