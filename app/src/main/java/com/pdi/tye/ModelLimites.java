package com.pdi.tye;

public class ModelLimites {
    String limiteID;
    String designacaoLimite;
    String valorLimite;
    String dataInicioLimite;
    String dataFimLimite;
    String categoriaLimite;
    String contaIDLimite;

    public ModelLimites(String limiteID, String designacaoLimite, String valorLimite, String dataInicioLimite, String dataFimLimite, String categoriaLimite, String contaIDLimite) {
        this.limiteID = limiteID;
        this.designacaoLimite = designacaoLimite;
        this.valorLimite = valorLimite;
        this.dataInicioLimite = dataInicioLimite;
        this.dataFimLimite = dataFimLimite;
        this.categoriaLimite = categoriaLimite;
        this.contaIDLimite = contaIDLimite;
    }

    public String getLimiteID() {
        return limiteID;
    }

    public void setLimiteID(String limiteID) {
        this.limiteID = limiteID;
    }

    public String getDesignacaoLimite() {
        return designacaoLimite;
    }

    public void setDesignacaoLimite(String designacaoLimite) {
        this.designacaoLimite = designacaoLimite;
    }

    public String getValorLimite() {
        return valorLimite;
    }

    public void setValorLimite(String valorLimite) {
        this.valorLimite = valorLimite;
    }

    public String getDataInicioLimite() {
        return dataInicioLimite;
    }

    public void setDataInicioLimite(String dataInicioLimite) {
        this.dataInicioLimite = dataInicioLimite;
    }

    public String getDataFimLimite() {
        return dataFimLimite;
    }

    public void setDataFimLimite(String dataFimLimite) {
        this.dataFimLimite = dataFimLimite;
    }

    public String getCategoriaLimite() {
        return categoriaLimite;
    }

    public void setCategoriaLimite(String categoriaLimite) {
        this.categoriaLimite = categoriaLimite;
    }

    public String getContaIDLimite() {
        return contaIDLimite;
    }

    public void setContaIDLimite(String contaIDLimite) {
        this.contaIDLimite = contaIDLimite;
    }
}
