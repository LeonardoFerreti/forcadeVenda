package com.forcavenda.Dao;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 09/02/2017.
 */

public class ProdutoDao {

    public void Incluir(DatabaseReference ref, String chave, Map<String, Object> produto){
        Map<String, Object> objDao = new HashMap<>();
        objDao.put("produto/" + chave, produto);
        ref.updateChildren(objDao);
    }
}
