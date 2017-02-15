package com.forcavenda.Dao;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 09/02/2017.
 */
public class EnderecoDao {

    public void Incluir(DatabaseReference ref, Map<String, Object> enderecos) {
        Map<String, Object> objDao = new HashMap<>();
        objDao.put("endereco", enderecos);
        ref.updateChildren(objDao);
    }

}
