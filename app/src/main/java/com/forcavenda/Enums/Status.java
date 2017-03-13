package com.forcavenda.Enums;

/**
 * Created by Leo on 18/02/2017.
 */

public enum Status {
    Pendente("Pendente"),
    Andamento("Em andamento"),
    Preparo("Preparando"),
    Entrega("Saiu pra entrega"),
    Concluido("Concluído"),
    Cancelado("Cancelado");

    private String descricao;

    public String getDescricao() {
        return descricao;
    }

    Status(String descricao) {
        this.descricao = descricao;
    }

    public Status retornaStatusPelaDescricao(String descricao) {
        Status[] values = Status.values();
        Status selecionado = null;
        for (Status status : values) {
            if (descricao.equals(status.getDescricao())) {
                selecionado = status;
            }
        }
        return selecionado;
    }

}
