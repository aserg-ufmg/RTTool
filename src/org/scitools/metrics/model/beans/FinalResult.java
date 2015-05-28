/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scitools.metrics.model.beans;

/**
 *
 * @author fernando
 */
public class FinalResult {
    private String metrica;
    private float percentil;
    private float k;

    public FinalResult() {
    }

    public FinalResult(String metrica, float percentil, float k) {
        this.metrica = metrica;
        this.percentil = percentil;
        this.k = k;
    }

    public String getMetrica() {
        return metrica;
    }

    public void setMetrica(String metrica) {
        this.metrica = metrica;
    }

    public float getPercentil() {
        return percentil;
    }

    public void setPercentil(float percentil) {
        this.percentil = percentil;
    }

    public float getK() {
        return k;
    }

    public void setK(float k) {
        this.k = k;
    }
    
}
