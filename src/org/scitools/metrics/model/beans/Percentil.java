/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scitools.metrics.model.beans;

import java.util.ArrayList;

/**
 *
 * @author fernando
 */
public class Percentil {
    private float[] percentis;
    private String nomeProjeto;

    public Percentil() {
        this.percentis = new float[20];
        this.nomeProjeto = "";
    }

    public Percentil(float[] percentis, String nomeProjeto) {
        this.percentis = percentis;
        this.nomeProjeto = nomeProjeto;
    }

    public float[] getPercentis() {
        return percentis;
    }

    public void setPercentis(float[] percentis) {
        this.percentis = percentis;
    }

    public String getNomeProjeto() {
        return nomeProjeto;
    }

    public void setNomeProjeto(String nomeProjeto) {
        this.nomeProjeto = nomeProjeto;
    }
    
    
}
