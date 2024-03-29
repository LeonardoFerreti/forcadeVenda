package com.forcavenda.Entidades;

import com.forcavenda.Enums.Status;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 13/02/2017.
 */

public class Pedido implements Serializable {
    //Atributos
    private Long idPedido;
    private String chave;
    private Cliente cliente;
    private FormaPgto formaPgto;
    private List<ItemPedido> listaItens;
    private Double valorTotal;
    private Boolean online;
    private Status status;

    public void setChave(String chave) {
        this.chave = chave;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setFormaPgto(FormaPgto formaPgto) {
        this.formaPgto = formaPgto;
    }

    public void setListaItens(List<ItemPedido> listaItens) {
        this.listaItens = listaItens;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    //retorna se o pedido foi feito pelo Cliente(true), ou não
    public Boolean getOnline() {
        return online;
    }

    //retorna a chave gerada automaticamente(GUID)
    public String getChave() {
        return chave;
    }

    //retorna o identificador da venda
    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    //retorna a forma de pagamento da venda
    public FormaPgto getFormaPgto() {
        return formaPgto;
    }

    //retorna a lista de produtos da venda
    public List<ItemPedido> getListaItens() {
        return listaItens;
    }

    //retorna o valor total da venda
    public Double getValorTotal() {
        return valorTotal;
    }

    //retorna o status do pedido
    public Status getStatus() {
        return status;
    }

    //Construtor padrão
    public Pedido() {
    }

    //Construtor com os parametros
    public Pedido(Long idPedido, String chave, Cliente cliente, FormaPgto formaPgto, List<ItemPedido> listaItens, Double valorTotal,  Boolean online) {
        this.idPedido = idPedido;
        this.chave = chave;
        this.cliente = cliente;
        this.formaPgto = formaPgto;
        this.listaItens = listaItens;
        this.valorTotal = valorTotal;
        this.online = online;
    }

    //Construtor com os parametros
    public Pedido(String chave,  Long idPedido, Double valorTotal,   Boolean online,Status status,Cliente cliente, FormaPgto formaPgto) {
        this.chave = chave;
        this.idPedido = idPedido;
        this.valorTotal = valorTotal;
        this.online = online;
        this.status = status;
        this.cliente =cliente;
        this.formaPgto = formaPgto;
    }

    //retorna um HasMap com as propriedades e valores de um objeto Cliente
    public static Map<String, Object> MapPedido(Pedido pedido) {
        HashMap<String, Object> resultado = new HashMap<>();
        resultado.put("chave", pedido.getChave());
        resultado.put("idPedido", pedido.getIdPedido());
        resultado.put("valorTotal", pedido.getValorTotal());
        resultado.put("online", pedido.getOnline());
        resultado.put("status", pedido.getStatus());
        resultado.put("cliente", Cliente.MapCliente(pedido.getCliente()));
        resultado.put("formaPgto",FormaPgto.MapFormaPgto(pedido.getFormaPgto()));

        return resultado;
    }
}
