package com.forcavenda.Dao;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 16/02/2017.
 */

public class UsuarioDao {

    public void IncluirAlterar(DatabaseReference ref, String chave, Map<String, Object> usuario) {
        try {
            Map<String, Object> objDao = new HashMap<>();
            objDao.put("usuario/" + chave, usuario);
            ref.updateChildren(objDao);
        } catch (Exception e) {
            throw e;
        }
    }
}
