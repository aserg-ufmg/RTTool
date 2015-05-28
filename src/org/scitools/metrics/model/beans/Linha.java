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
public class Linha {

    private int k;

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }
    private ArrayList<CoordXY> linha;
    private String nomeLinha;

    public String getNomeLinha() {
        return nomeLinha;
    }

    public void setNomeLinha(String nomeLinha) {
        this.nomeLinha = nomeLinha;
    }

    public ArrayList<CoordXY> getLinha() {
        return linha;
    }

    public void setLinha(ArrayList<CoordXY> linha) {
        this.linha = linha;
    }
}
