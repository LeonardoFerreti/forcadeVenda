package com.forcavenda.Dao;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 09/02/2017.
 */

public class ClienteDao {

    public DatabaseReference Incluir(DatabaseReference ref,String chave, Map<String, Object> cliente){
        Map<String, Object> objDao = new HashMap<>();
        objDao.put("cliente/" + chave, cliente);
        ref.updateChildren(objDao);
        DatabaseReference refNovoCliente = ref.child("cliente").child(chave);
        return refNovoCliente;
    }
}
