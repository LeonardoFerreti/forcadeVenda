package com.forcavenda.Dao;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 09/02/2017.
 */

public class FormaPgtoDao {

    public void IncluirAlterar(DatabaseReference ref, String chave, Map<String, Object> formaPgto) {
        try {
            Map<String, Object> objDao = new HashMap<>();
            objDao.put("formaPgto/" + chave, formaPgto);
            ref.updateChildren(objDao);
        } catch (Exception e) {
            throw e;
        }

    }
}
