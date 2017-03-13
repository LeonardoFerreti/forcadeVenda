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
    String id_usuario;
    Boolean isAdmin;
    String telefone;
    Endereco endereco;

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    //Construtor padrão
    public Cliente() {
    }

    //Construtor com os parametros: identificador, nome e email
    public Cliente(String id, String nome, String email, String id_usuario, Boolean isAdmin, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.id_usuario = id_usuario;
        this.isAdmin = isAdmin;
        this.telefone = telefone;
    }

    //Construtor com todos os parametros
    public Cliente(String id, String nome, String email, String id_usuario, Boolean isAdmin, Endereco endereco, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.id_usuario = id_usuario;
        this.endereco = endereco;
        this.telefone = telefone;
        this.isAdmin = isAdmin;
    }

    //retorna o identificador do usuario
    public String getId_usuario() {
        return id_usuario;
    }

    //retorna se o usuario vinculado é admin ou não
    public Boolean getAdmin() {
        return isAdmin;
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
    public String getTelefone() {
        return telefone;
    }

    //retorna um HasMap com as propriedades e valores de um objeto Cliente
    public static Map<String, Object> MapCliente(Cliente cliente) {
        HashMap<String, Object> resultado = new HashMap<>();
        resultado.put("id", cliente.getId());
        resultado.put("nome", cliente.getNome());
        resultado.put("email", cliente.getEmail());
        resultado.put("id_usuario", cliente.getId_usuario());
        resultado.put("isAdmin", cliente.getAdmin());
        resultado.put("telefone", cliente.getTelefone());
        resultado.put("endereco", Endereco.MapEndereco(cliente.getEndereco()));
        return resultado;
    }

    @Override
    public String toString() {
        return getNome();
    }
}
