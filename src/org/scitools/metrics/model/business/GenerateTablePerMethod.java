/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scitools.metrics.model.business;

import java.util.ArrayList;
import org.scitools.metrics.model.beans.datain_xml.*;
import org.scitools.metrics.model.util.*;

/**
 *
 * @author Fernando Paim Lima
 */
public class GenerateTablePerMethod {

    public static String IS_METHOD = "method";
    private ArrayList<Value> listaPreOrdem;

    private void geraListaPreOrdenacao(ArrayList<Metrics> listMetrics) {
        listaPreOrdem = new ArrayList();
        //Adiciona na lista preordem
        for (Metrics ms : listMetrics) {//listagem de arquivos do projeto
            for (Metric m : ms.getMetricList()) { //listagem de metric
                try {
                    //verifica que a métric está por type
                    if (m.getValues().getPer().equalsIgnoreCase(GenerateTablePerMethod.IS_METHOD)) {
                        listaPreOrdem.addAll(m.getValues().getValueList());//adiciona os values referentes a type
                    }
                } catch (NullPointerException cc) {
                    //System.err.println("Teste em Per nulo na lista PreOrdem:" + cc);
                    //System.err.print(".");
                } catch (Exception cc) {
                    //System.err.println("Erro inesperado ao gera lista PreOrdem:" + cc);
                    //cc.printStackTrace();
                }
            }
        }
    }

    private ArrayList<Integer> getQtdMetodosRepetidos() {
        ArrayList<Integer> qtdDeMetodosRepetidos = new ArrayList();
        String auxc = "-1";
        int ic = 0, cont = 0, tam = listaPreOrdem.size();
        for (Value x : listaPreOrdem) {
            if (!(x.getPkg() + ":" + x.getSource() + ":" + x.getName()).equalsIgnoreCase(auxc) && !auxc.equalsIgnoreCase("-1")) {
                qtdDeMetodosRepetidos.add(ic);
                ic = 1;
                cont++;//contador geral
            } else {
                ic++;
                cont++;//contador geral
                if (cont >= (tam)) {
                    qtdDeMetodosRepetidos.add(ic);
                }
            }
            auxc = x.getPkg() + ":" + x.getSource() + ":" + x.getName();
        }
        return qtdDeMetodosRepetidos;
    }

    private void ordenarMetricasPorIntervalos(ArrayList<Integer> qtdMetodosRepetidos) {
        int i = 0;
        int pp = 0;
        for (int t : qtdMetodosRepetidos) {
            pp += t;
            QuicSortValue.quick_sort(listaPreOrdem, QuicSortValue.CAMPO_AUX, i, pp - 1);
            i += t;
        }
    }

    private ArrayList<Value> getListaAgrupada() {
        float sumValue = 0;
        int contLPO = 0, tam = listaPreOrdem.size();
        ArrayList<Value> agrupado = new ArrayList();
        Value auxv = new Value("-1", "", "-1", "");
        auxv.setAux("-1");
        for (Value x : listaPreOrdem) {
            if ((!x.getPkg().equalsIgnoreCase(auxv.getPkg()) && !auxv.getPkg().equalsIgnoreCase("-1"))
                    || (!x.getSource().equalsIgnoreCase(auxv.getSource()) && !auxv.getSource().equalsIgnoreCase("-1"))
                    || (!x.getName().equalsIgnoreCase(auxv.getName()) && !auxv.getName().equalsIgnoreCase("-1"))
                    || (!x.getAux().equalsIgnoreCase(auxv.getAux()) && !auxv.getAux().equalsIgnoreCase("-1"))) {

                //evitando que a primeira linha seje -1 -1
                if (auxv.getName().equalsIgnoreCase("-1")) {
                    auxv = x;
                    if (auxv.getSource() == null) {
                        auxv.setSource("");
                    }
                    if (auxv.getValue() == null) {
                        auxv.setValue("0");
                    }
                } else {
                    auxv.setValue(sumValue + "");
                }
                agrupado.add(auxv);
                contLPO++;
                if (contLPO >= tam) {// se o no final tiver mais um valor+++++++++++++++++++
                    agrupado.add(x);
                }
                try {
                    sumValue = Float.parseFloat(x.getValue());
                } catch (Exception ee) {
                    //System.err.println("Incompatibilidade de carctere: " + ee + "; Source:" + x.getSource() + "; Name:" + x.getName() + "; Value:" + x.getValue());
                    //   ee.printStackTrace();
                }
            } else {
                try {
                    sumValue += Float.parseFloat(x.getValue());
                } catch (Exception ee) {
                    //System.err.println("Incompatibilidade de carctere: " + ee + "; Source:" + x.getSource() + "; Name:" + x.getName() + "; Value:" + x.getValue());
                    // ee.printStackTrace();
                }
                contLPO++;
                if (contLPO >= tam) {// se o no final tiver mais um valor+++++++++++++++++++
                    if (!x.getAux().equalsIgnoreCase(auxv.getAux())) {
                        agrupado.add(x);
                    } else {
                        auxv.setValue(sumValue + "");
                        agrupado.add(auxv);
                    }
                }
            }
            auxv = x;
        }
        return agrupado;
    }

    public StringBuffer geraTabelaMethod(ArrayList<Metrics> listMetrics) {

        //cria a lista de preordenação
        geraListaPreOrdenacao(listMetrics);

        //cria a lista de nomes de métricas;
        ArrayList<String> nomesMetricas = getNomesMetricasFilter(listMetrics);

        //System.out.println("Ordenando...");
        //passo 1, ordena todos os pacotes
        QuicSortValue.quick_sort(listaPreOrdem, QuicSortValue.CAMPO_PACKAGE_TYPE_NAME, 0, listaPreOrdem.size() - 1);

        //System.out.println("Obtendo a quantidade de métodos repetidos...");
        //conta a quantidades de métodos 
        ArrayList<Integer> qtdMetodosRepetidos = getQtdMetodosRepetidos();

        //System.out.println("Ordenado as métricas por intevalos de métodos...");
        //Ordena intervalos por metrica
        ordenarMetricasPorIntervalos(qtdMetodosRepetidos);
        qtdMetodosRepetidos.clear();//aqui esta lista não será mais necessária

        //System.out.println("Agrupando por intervalos...");
        // Agrupando por métodos
        ArrayList<Value> agrupado = getListaAgrupada();
        listaPreOrdem.clear();//aqui esta lista não será mais útil

        //System.out.println("Preparando a string de gravação...");
        //imprime na variavel saida
        StringBuffer saida = new StringBuffer("Metodos,");
        for (String x : nomesMetricas) {
            saida.append(x + ",");
        }
        String auxp = "-1";
        String auxm = "-1";
        String auxc = "-1";
        int contNome = 0, contGeral = 0, tamNomes = nomesMetricas.size(), tamAgrup = agrupado.size();
        for (Value x : agrupado) {
            if (contGeral != 0) {
                if ((!x.getPkg().equalsIgnoreCase(auxp) || !x.getName().equalsIgnoreCase(auxm) || !x.getSource().equalsIgnoreCase(auxc))) {//quando muda de linha
                    saida.append("\n" + x.getPkg() + ":" + x.getSource() + ":" + x.getName());
                    contGeral++;
                    //verificação se está imprimindo na métrica correta
                    contNome = 0;
                    if (x.getAux().equalsIgnoreCase(nomesMetricas.get(contNome))) {//se estiver imprimindo na metrica correta
                        saida.append("," + x.getValue());
                    } else {// senão procura a métrica correta
                        for (int r = contNome; r < tamNomes; r++) {
                            if (x.getAux().equalsIgnoreCase(nomesMetricas.get(r))) { //se achou a métrica correta
                                saida.append("," + x.getValue());
                                contNome = r;
                                r = tamNomes;// finaliza a procura da métrica correta
                            } else {//se não encontrou a métrica correta, passe para a próxima
                                saida.append(",");
                            }
                        }
                    }

                } else {//se continua na mesma linha
                    contGeral++;
                    contNome++;
                    if (x.getAux().equalsIgnoreCase(nomesMetricas.get(contNome))) {//se estiver imprimindo na metrica correta
                        saida.append("," + x.getValue());
                    } else {// senão procura a métrica correta
                        for (int r = contNome; r < tamNomes; r++) {
                            if (x.getAux().equalsIgnoreCase(nomesMetricas.get(r))) { //se achou a métrica correta
                                saida.append("," + x.getValue());
                                contNome = r;
                                r = tamNomes;// finaliza a procura da métrica correta
                            } else {//se não encontrou a métrica correta, passe para a próxima
                                saida.append(",");
                            }
                        }
                    }
                }
            }//fim da primeira rodada
            auxp = x.getPkg();
            auxm = x.getName();
            auxc = x.getSource();
            if (contGeral == 0) { // pula a primeira
                auxc = "-1";
                contGeral++;
            }
        }
        //System.out.println("Gravando agora!!!");
        return saida;
    }

    private ArrayList<String> getNomesMetricasFilter(ArrayList<Metrics> listMetrics) {
        ArrayList<String> nomes = getNomesMetricasType(listMetrics);
        ArrayList<String> nomesAux = new ArrayList();
        String nomeAux = "-1";
        QuicSortString.quick_sort(nomes, 0, nomes.size() - 1,QuicSortString.TYPE_STRING);

        //se existem mais arquivos os nomes das métricas irão se repetir
        if (listMetrics.size() > 1) {
            for (String nome : nomes) {
                if (!nomeAux.equalsIgnoreCase(nome)) {
                    nomesAux.add(nome);
                }
                nomeAux = nome;
            }
            nomes = nomesAux;
        }
        return nomes;
    }

    private ArrayList<String> getNomesMetricasType(ArrayList<Metrics> listMetrics) {
        ArrayList<String> saida = new ArrayList();
        for (Metrics ms : listMetrics) {//listagem de arquivos do projeto
            for (Metric m : ms.getMetricList()) {
                try {
                    if (m.getValues().getPer().equalsIgnoreCase(GenerateTablePerMethod.IS_METHOD)) {
                        saida.add(m.getId());
                    }
                } catch (NullPointerException cc) {
                    //System.err.println("Per nulo desconsiderado na geração de nomes de métricas:" + cc);
                   // System.err.print(":");
                } catch (Exception cc) {
                    //System.err.println("Erro inesperado na geração de nomes de métricas:" + cc);
                    //cc.printStackTrace();
                }
            }
        }
        return saida;
    }
}
