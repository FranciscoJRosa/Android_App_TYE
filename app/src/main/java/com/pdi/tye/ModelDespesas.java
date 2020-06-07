package com.pdi.tye;

public class ModelDespesas {
    String regDespID;
    String contaID;
    String catID;
    String valor;
    String data;
    String designacaoDespesa;

    public ModelDespesas(String regDespID, String contaID, String catID, String valor, String data, String designacaoDespesa) {
        this.regDespID = regDespID;
        this.contaID = contaID;
        this.catID = catID;
        this.valor = valor;
        this.data = data;
        this.designacaoDespesa = designacaoDespesa;
    }

    public String getRegDespID() {
        return regDespID;
    }

    public void setRegDespID(String regDespID) {
        this.regDespID = regDespID;
    }

    public String getContaID() {
        return contaID;
    }

    public void setContaID(String contaID) {
        this.contaID = contaID;
    }

    public String getCatID() {
        return catID;
    }

    public void setCatID(String catID) {
        this.catID = catID;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDesignacaoDespesa() {
        return designacaoDespesa;
    }

    public void setDesignacaoDespesa(String designacaoDespesa) {
        this.designacaoDespesa = designacaoDespesa;
    }
}
