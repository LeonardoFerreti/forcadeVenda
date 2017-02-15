package com.forcavenda.Entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 08/02/2017.
 */
public class Cliente implements Serializable {

    //Parametros da classe
    String id;
    String nome;
    String email;
    Endereco endereco;
    Telefone telefone;

    //Construtor padrão
    public Cliente() {
    }

    //Construtor com os parametros: identificador, nome e email
    public Cliente(String id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    //Construtor com todos os parametros
    public Cliente(String id, String nome, String email, Endereco endereco, Telefone telefone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.endereco = endereco;
        this.telefone = telefone;
    }

    //retorna o identificador do cliente
    public String getId() {
        return id;
    }

    //retorna o nome do cliente
    public String getNome() {
        return nome;
    }

    //retorna o email do cliente
    public String getEmail() {
        return email;
    }

    //retorna o endereco de um cliente
    public Endereco getEndereco() {
        return endereco;
    }

    //retorna a lista de telefones de um cliente
    public Telefone getTelefone() {
        return telefone;
    }

    //retorna um HasMap com as propriedades e valores de um objeto Cliente
    public static Map<String, Object> MapCliente(Cliente cliente) {
        HashMap<String, Object> resultado = new HashMap<>();
        resultado.put("id", cliente.getId());
        resultado.put("nome", cliente.getNome());
        resultado.put("email", cliente.getEmail());
        return resultado;
    }

    @Override
    public String toString() {
        return getNome();
    }
}
