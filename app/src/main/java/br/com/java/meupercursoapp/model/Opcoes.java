package br.com.java.meupercursoapp.model;

import java.io.Serializable;

public class Opcoes implements Serializable {

    private MeiosDeTransporte meiosDeTransporte;
    private boolean inciarAtual;
    private boolean pararAtual;

    public Opcoes(MeiosDeTransporte meiosDeTransporte, boolean inciarAtual, boolean pararAtual) {
        this.meiosDeTransporte = meiosDeTransporte;
        this.inciarAtual = inciarAtual;
        this.pararAtual = pararAtual;
    }

    public MeiosDeTransporte getMeiosDeTransporte() {
        return meiosDeTransporte;
    }

    public void setMeiosDeTransporte(MeiosDeTransporte meiosDeTransporte) {
        this.meiosDeTransporte = meiosDeTransporte;
    }

    public boolean isInciarAtual() {
        return inciarAtual;
    }

    public void setInciarAtual(boolean inciarAtual) {
        this.inciarAtual = inciarAtual;
    }

    public boolean isPararAtual() {
        return pararAtual;
    }

    public void setPararAtual(boolean pararAtual) {
        this.pararAtual = pararAtual;
    }
}
