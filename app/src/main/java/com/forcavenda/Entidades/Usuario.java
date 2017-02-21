package com.forcavenda.Entidades;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 16/02/2017.
 */

public class Usuario implements Serializable {
    //Atributos
    private String email;
    private String idUsuario;
    private String idCliente;
    private Boolean admin;

    //retorna o identificador do cliente
    public String getIdCliente() {
        return idCliente;
    }

    //retornar o identificador do usuario
    public String getIdUsuario() {
        return idUsuario;
    }

    //retora se o usuario é administrador ou não
    public Boolean getAdmin() {
        return admin;
    }

    //retorna o Email do usuário
    public String getEmail() {
        return email;
    }
    //Construtor padrao
    public Usuario() {
    }

    //Construtor com os parametros
    public Usuario(String idUsuario, String idCliente,String email, Boolean admin) {
        this.idUsuario = idUsuario;
        this.idCliente = idCliente;
        this.admin = admin;
        this.email = email;
    }

    //Retorna um HasMap com a propriedades e os valores de um objeto Usuario
    public static Map<String, Object> MapUsuario(Usuario usuario) {
        HashMap<String, Object> resultado = new HashMap<>();
        resultado.put("idUsuario", usuario.getIdUsuario());
        resultado.put("idCliente", usuario.getIdCliente());
        resultado.put("admin", usuario.getAdmin());
        resultado.put("email",usuario.getEmail());
        return resultado;
    }
}
