package com.forcavenda.Entidades;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 08/02/2017.
 */
public class Telefone implements Serializable {

    //Parametros
    private String ddd;
    private String numero;
    private String ramal;

    //Construtor padrão
    public Telefone() {
    }

    //Construtor com os parametros
    public Telefone(String ddd, String numero, String ramal) {
        this.ddd = ddd;
        this.numero = numero;
        this.ramal = ramal;
    }

    //retorna o DDD
    public String getDdd() {
        return ddd;
    }

    //Retorna o numero do telefone
    public String getNumero() {
        return numero;
    }

    //Retorna o numero o Ramal
    public String getRamal() {
        return ramal;
    }

    //Retorna um HasMap com a propriedades e os valores de um objeto telefone
    public static Map<String, Object> MapTelefone(Telefone telefone) {
        HashMap<String, Object> resultado = new HashMap<>();
        resultado.put("ddd", telefone.getDdd());
        resultado.put("numero", telefone.getNumero());
        resultado.put("ramal", telefone.getRamal());
        return resultado;
    }
}
