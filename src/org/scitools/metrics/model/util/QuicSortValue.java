/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scitools.metrics.model.util;

import java.util.ArrayList;
import org.scitools.metrics.model.beans.datain_xml.Value;

/**
 *
 * @author Fernando Paim Lima
 */
public class QuicSortValue {

    public static int CAMPO_NAME = 1;
    public static int CAMPO_PACKAGE = 2;
    public static int CAMPO_AUX = 3;// CAMPO AUXILIAR, UTILIZADO PARA COLOCAR O NOME DE SUA MÉTRICA
    public static int CAMPO_PACKAGE_NAME = 4;
    public static int CAMPO_PACKAGE_TYPE_NAME = 5;
    public static int CAMPO_PACKAGE_TYPE = 6;
    public static int CAMPO_VALUE = 7;

    public static void quick_sort(ArrayList<Value> vIndice, int CAMPO_ORDENAR, int ini, int fim) {
        int meio;

        if (ini < fim) {
            meio = partition(vIndice, CAMPO_ORDENAR, ini, fim);
            quick_sort(vIndice, CAMPO_ORDENAR, ini, meio);
            quick_sort(vIndice, CAMPO_ORDENAR, meio + 1, fim);
        }
    }

    public static int partition(ArrayList<Value> vIndice, int CAMPO_ORDENAR, int ini, int fim) {
        int topo, i;
        Value pivox = vIndice.get(ini);
        topo = ini;

        for (i = ini + 1; i <= fim; i++) {
            switch (CAMPO_ORDENAR) {
                case 1:
                    try {
                        if (vIndice.get(i).getName().compareToIgnoreCase(pivox.getName()) < 0) {
                            vIndice.set(topo, vIndice.get(i));
                            vIndice.set(i, vIndice.get(topo + 1));
                            topo++;
                        }
                    } catch (Exception x) {
                        //System.err.println(x);
                        //x.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        if (vIndice.get(i).getPkg().compareToIgnoreCase(pivox.getPkg()) < 0) {
                            vIndice.set(topo, vIndice.get(i));
                            vIndice.set(i, vIndice.get(topo + 1));
                            topo++;
                        }
                    } catch (Exception x) {
                        //System.err.println(x);
                        //x.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        if (vIndice.get(i).getAux().compareToIgnoreCase(pivox.getAux()) < 0) {
                            vIndice.set(topo, vIndice.get(i));
                            vIndice.set(i, vIndice.get(topo + 1));
                            topo++;
                        }
                    } catch (Exception x) {
                        //System.err.println(x);
                        //x.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        if ((vIndice.get(i).getPkg() + ":" + vIndice.get(i).getName()).compareToIgnoreCase(pivox.getPkg() + ":" + pivox.getName()) < 0) {
                            vIndice.set(topo, vIndice.get(i));
                            vIndice.set(i, vIndice.get(topo + 1));
                            topo++;
                        }
                    } catch (Exception x) {
                        //System.err.println(x);
                        //x.printStackTrace();
                    }
                    break;
                case 5:
                    try {
                        if ((vIndice.get(i).getPkg() + ":" + vIndice.get(i).getSource() + ":" + vIndice.get(i).getName()).compareToIgnoreCase(pivox.getPkg() + ":" + pivox.getSource() + ":" + pivox.getName()) < 0) {
                            vIndice.set(topo, vIndice.get(i));
                            vIndice.set(i, vIndice.get(topo + 1));
                            topo++;
                        }
                    } catch (Exception x) {
                        //System.err.println(x);
                        //x.printStackTrace();
                    }
                    break;
                case 6:
                    try {
                        if ((vIndice.get(i).getPkg() + ":" + vIndice.get(i).getSource()).compareToIgnoreCase(pivox.getPkg() + ":" + pivox.getSource()) < 0) {
                            vIndice.set(topo, vIndice.get(i));
                            vIndice.set(i, vIndice.get(topo + 1));
                            topo++;
                        }
                    } catch (Exception x) {
                        //System.err.println(x);
                        //x.printStackTrace();
                    }
                    break;
                case 7:
                    try {
                        if (Float.parseFloat(vIndice.get(i).getValue()) < Float.parseFloat(pivox.getValue())) {
                            vIndice.set(topo, vIndice.get(i));
                            vIndice.set(i, vIndice.get(topo + 1));
                            topo++;
                        }
                    } catch (Exception x) {
                        //System.err.println(x);
                        //x.printStackTrace();
                    }
                    break;
            }
        }
        vIndice.set(topo, pivox);
        return topo;
    }
}
