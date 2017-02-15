package com.forcavenda.Entidades;

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

    public void setChave(String chave) {
        this.chave = chave;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    private Cliente cliente;
    private FormaPgto formaPgto;
    private List<ItemPedido> listaItens;
    private Double valorTotal;
    private Double desconto;
    private Double valorPago;


    //retorna se o pedido foi feito pelo Cliente(true), ou não
    public Boolean getOnline() {
        return online;
    }

    private Boolean online;


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

    //retorna o cliente da venda
    public Cliente getCliente() {
        return cliente;
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

    //retorna o desconto concedido na venda
    public Double getDesconto() {
        return desconto;
    }

    //retorna o valor pago pelo cliente
    public Double getValorPago() {
        return valorPago;
    }

    //Construtor padrão
    public Pedido() {
    }

    //Construtor com os parametros
    public Pedido(Long idPedido, String chave, Cliente cliente, FormaPgto formaPgto, List<ItemPedido> listaItens, Double valorTotal, Double desconto, Double valorPago, Boolean online) {
        this.idPedido = idPedido;
        this.chave = chave;
        this.cliente = cliente;
        this.formaPgto = formaPgto;
        this.listaItens = listaItens;
        this.valorTotal = valorTotal;
        this.desconto = desconto;
        this.valorPago = valorPago;
        this.online = online;
    }

    //Construtor com os parametros
    public Pedido(String chave, Long idPedido, Double valorTotal, Double desconto, Double valorPago, Boolean online) {
        this.chave = chave;
        this.idPedido = idPedido;
        this.valorTotal = valorTotal;
        this.desconto = desconto;
        this.valorPago = valorPago;
        this.online = online;
    }

    //retorna um HasMap com as propriedades e valores de um objeto Cliente
    public static Map<String, Object> MapPedido(Pedido pedido) {
        HashMap<String, Object> resultado = new HashMap<>();
        resultado.put("chave", pedido.getChave());
        resultado.put("idPedido", pedido.getIdPedido());
        resultado.put("valorTotal", pedido.getValorTotal());
        resultado.put("desconto", pedido.getDesconto());
        resultado.put("valorPago", pedido.getValorPago());
        resultado.put("online", pedido.getOnline());
        return resultado;
    }

}
