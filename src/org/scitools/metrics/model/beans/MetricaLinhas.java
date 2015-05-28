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
public class MetricaLinhas {

    private ArrayList<Linha> linhas;
    private String nomeMetrica;
    private float k;

    public float getK() {
        return k;
    }

    public void setK(float k) {
        this.k = k;
    }

    public ArrayList<Linha> getLinhas() {
        return linhas;
    }

    public void setLinhas(ArrayList<Linha> linhas) {
        this.linhas = linhas;
    }

    public String getNomeMetrica() {
        return nomeMetrica;
    }

    public void setNomeMetrica(String nomeMetrica) {
        this.nomeMetrica = nomeMetrica;
    }
}
