/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scitools.metrics.model.util;

import java.util.ArrayList;

/**
 *
 * @author Fernando Paim Lima
 */
public class QuicSortString {

    public static int TYPE_FLOAT = 0;
    public static int TYPE_STRING = 1;

    public static void quick_sort(ArrayList<String> vIndice, int ini, int fim, int TYPE) {
        int meio;
        if (ini < fim) {
            meio = partition(vIndice, ini, fim, TYPE);
            quick_sort(vIndice, ini, meio, TYPE);
            quick_sort(vIndice, meio + 1, fim, TYPE);
        }
    }

    public static int partition(ArrayList<String> vIndice, int ini, int fim, int TYPE) {
        int topo, i;
        String pivo;
        pivo = vIndice.get(ini);
        topo = ini;
        if (TYPE == 0) {
            for (i = ini + 1; i <= fim; i++) {//se for float
                try {
                    if (Float.parseFloat(vIndice.get(i)) < Float.parseFloat(pivo)) {
                        vIndice.set(topo, vIndice.get(i));
                        vIndice.set(i, vIndice.get(topo + 1));
                        topo++;
                    }
                } catch (Exception x) {
                    //System.err.println(x);
                    //x.printStackTrace();
                }
            }
            vIndice.set(topo, pivo);
        } else if (TYPE == 1) { // se for srtring
            for (i = ini + 1; i <= fim; i++) {
                try {
                    if (vIndice.get(i).compareToIgnoreCase(pivo) < 0) {
                        vIndice.set(topo, vIndice.get(i));
                        vIndice.set(i, vIndice.get(topo + 1));
                        topo++;
                    }
                } catch (Exception x) {
                    //System.err.println(x);
                    //x.printStackTrace();
                }
            }
            vIndice.set(topo, pivo);
        }
        return topo;
    }
}
