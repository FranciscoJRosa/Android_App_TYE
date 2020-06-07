package com.pdi.tye;

public class ModelTransferencias {
    String regTransfID;
    String designacaoTransf;
    String contaDestinoID;
    String valorTransf;
    String dataTransf;
    String contaID;

    public ModelTransferencias(String regTransfID, String designacaoTransf, String contaDestinoID, String valorTransf, String dataTransf, String contaID) {
        this.regTransfID = regTransfID;
        this.designacaoTransf = designacaoTransf;
        this.contaDestinoID = contaDestinoID;
        this.valorTransf = valorTransf;
        this.dataTransf = dataTransf;
        this.contaID = contaID;
    }

    public String getRegTransfID() {
        return regTransfID;
    }

    public void setRegTransfID(String regTransfID) {
        this.regTransfID = regTransfID;
    }

    public String getDesignacaoTransf() {
        return designacaoTransf;
    }

    public void setDesignacaoTransf(String designacaoTransf) {
        this.designacaoTransf = designacaoTransf;
    }

    public String getContaDestinoID() {
        return contaDestinoID;
    }

    public void setContaDestinoID(String contaDestinoID) {
        this.contaDestinoID = contaDestinoID;
    }

    public String getValorTransf() {
        return valorTransf;
    }

    public void setValorTransf(String valorTransf) {
        this.valorTransf = valorTransf;
    }

    public String getDataTransf() {
        return dataTransf;
    }

    public void setDataTransf(String dataTransf) {
        this.dataTransf = dataTransf;
    }

    public String getContaID() {
        return contaID;
    }

    public void setContaID(String contaID) {
        this.contaID = contaID;
    }
}
