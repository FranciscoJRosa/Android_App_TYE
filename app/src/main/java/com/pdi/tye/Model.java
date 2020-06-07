package com.pdi.tye;

import android.os.Parcel;
import android.os.Parcelable;

public class Model implements Parcelable {

    String id;

    public Model(String id, String designacao, String saldo) {
        this.id = id;
        this.designacao = designacao;
        this.saldo = saldo;
    }

    String designacao;
    String saldo;

    protected Model(Parcel in) {
        id = in.readString();
        designacao = in.readString();
        saldo = in.readString();
    }

    public static final Creator<Model> CREATOR = new Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesignacao() {
        return designacao;
    }

    public void setDesignacao(String designacao) {
        this.designacao = designacao;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(designacao);
        dest.writeString(saldo);
    }
}
