package com.pdi.tye;

public class ModelRendimentos {
    String regRendID;
    String contaID;
    String catRendID;
    String valorRend;
    String dataRend;
    String designacaoRend;

    public ModelRendimentos(String regRendID, String contaID, String catRendID, String valorRend, String dataRend, String designacaoRend) {
        this.regRendID = regRendID;
        this.contaID = contaID;
        this.catRendID = catRendID;
        this.valorRend = valorRend;
        this.dataRend = dataRend;
        this.designacaoRend = designacaoRend;
    }

    public String getRegRendID() {
        return regRendID;
    }

    public void setRegRendID(String regRendID) {
        this.regRendID = regRendID;
    }

    public String getContaID() {
        return contaID;
    }

    public void setContaID(String contaID) {
        this.contaID = contaID;
    }

    public String getCatRendID() {
        return catRendID;
    }

    public void setCatRendID(String catRendID) {
        this.catRendID = catRendID;
    }

    public String getValorRend() {
        return valorRend;
    }

    public void setValorRend(String valorRend) {
        this.valorRend = valorRend;
    }

    public String getDataRend() {
        return dataRend;
    }

    public void setDataRend(String dataRend) {
        this.dataRend = dataRend;
    }

    public String getDesignacaoRend() {
        return designacaoRend;
    }

    public void setDesignacaoRend(String designacaoRend) {
        this.designacaoRend = designacaoRend;
    }
}
