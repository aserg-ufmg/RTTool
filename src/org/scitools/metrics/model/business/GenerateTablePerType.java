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
public class GenerateTablePerType {

    public static String IS_TYPE = "type";
    private ArrayList<Value> listaPreOrdem;

    private void geraListaPreOrdenacao(ArrayList<Metrics> listMetrics) {
        listaPreOrdem = new ArrayList();
        //Adiciona na lista preordem
        for (Metrics ms : listMetrics) {//listagem de arquivos do projeto
            for (Metric m : ms.getMetricList()) { //listagem de metric
                try {
                    //verifica que a métric está por type
                    if (m.getValues().getPer().equalsIgnoreCase(GenerateTablePerType.IS_TYPE)) {
                        listaPreOrdem.addAll(m.getValues().getValueList());//adiciona os values referentes a type
                    }
                } catch (NullPointerException cc) {
                    //System.err.println("Falha, Lista PreOrdem:" + cc + ", Metric:" + m.getValues().getPer() + ", V:" + m.getValues().getValueList());
                    //System.err.print(".");
                } catch (Exception cc) {
                    //System.err.println("Erro inesperado ao gera lista PreOrdem type:" + cc);
                    //cc.printStackTrace();
                }
            }
        }
    }

    private ArrayList<Integer> getQtdClassesRepetidas() {
        ArrayList<Integer> qtdDeClassesRepetidas = new ArrayList();
        String auxc = "-1";
        int ic = 0, cont = 0, tam = listaPreOrdem.size();
        for (Value x : listaPreOrdem) {
            if (!(x.getPkg() + ":" + x.getName()).equalsIgnoreCase(auxc) && !auxc.equalsIgnoreCase("-1")) {
                qtdDeClassesRepetidas.add(ic);
                ic = 1;
                cont++;//contador geral
            } else {
                ic++;
                cont++;//contador geral
                if (cont >= (tam)) {
                    qtdDeClassesRepetidas.add(ic);
                }
            }
            auxc = x.getPkg() + ":" + x.getName();
        }
        return qtdDeClassesRepetidas;
    }

    private void ordenarMetricasPorIntervalos(ArrayList<Integer> qtdDeClassesRepetidas) {
        int i = 0;
        int pp = 0;
        for (int t : qtdDeClassesRepetidas) {
            pp += t;
            QuicSortValue.quick_sort(listaPreOrdem, QuicSortValue.CAMPO_AUX, i, pp - 1);
            i += t;
        }
    }

    private ArrayList<Value> getListaAgrupada() {
        int contlpo = 0, tam = listaPreOrdem.size();
        float sumValue = 0;
        ArrayList<Value> agrupado = new ArrayList();
        Value auxv = new Value("-1", "", "-1", "");
        auxv.setAux("-1");
        for (Value x : listaPreOrdem) {
            if ((!x.getPkg().equalsIgnoreCase(auxv.getPkg()) && !auxv.getPkg().equalsIgnoreCase("-1"))
                    || (!x.getName().equalsIgnoreCase(auxv.getName()) && !auxv.getName().equalsIgnoreCase("-1"))
                    || (!x.getAux().equalsIgnoreCase(auxv.getAux()) && !auxv.getAux().equalsIgnoreCase("-1"))) {

                auxv.setValue(sumValue + "");
                agrupado.add(auxv);
                contlpo++;
                if (contlpo >= tam) {// se o no final tiver mais um valor+++++++++++++++++++
                    agrupado.add(x);
                    break;
                }
                try {
                    sumValue = Float.parseFloat(x.getValue());
                } catch (NumberFormatException ee) {
                    //System.err.println("Falha, Incomp. de carctere: " + ee + "; N:" + x.getName() + "; V:" + x.getValue()+ "; M:" + x.getAux());
                    //   ee.printStackTrace();
                }
                catch (NullPointerException ee) {
                    //System.err.println("Falha, Algo vazio: " + ee + "; N:" + x.getName() + "; V:" + x.getValue()+ "; M:" + x.getAux());
                    //   ee.printStackTrace();
                }catch (Exception ee) {
                    //System.err.println("Falha, Algo vazio: " + ee + "; N:" + x.getName() + "; V:" + x.getValue());
                    //ee.printStackTrace();
                }
            } else {
                try {
                    sumValue += Float.parseFloat(x.getValue());
                } catch (NumberFormatException ee) {
                    //System.err.println("Falha, Incomp. de carctere: " + ee + "; N:" + x.getName() + "; V:" + x.getValue()+ "; M:" + x.getAux());
                    //   ee.printStackTrace();
                }
                catch (NullPointerException ee) {
                    //System.err.println("Falha, Algo vazio: " + ee + "; N:" + x.getName() + "; V:" + x.getValue());
                    //   ee.printStackTrace();
                }catch (Exception ee) {
                    //System.err.println("Falha, Algo vazio: " + ee + "; N:" + x.getName() + "; V:" + x.getValue());
                    //ee.printStackTrace();
                }
                contlpo++;
                if (contlpo >= tam - 1) {// se o no final tiver mais um valor+++++++++++++++++++
                    if (!x.getAux().equalsIgnoreCase(auxv.getAux())) {
                        agrupado.add(x);
                    } else {
                        auxv.setValue(sumValue + "");
                        agrupado.add(auxv);
                    }
                    break;
                }
            }
            auxv = x;
        }
        return agrupado;
    }

    private ArrayList<String> getNomesMetricasFilter(ArrayList<Metrics> listMetrics) {
        ArrayList<String> nomes = getNomesMetricasType(listMetrics);
        ArrayList<String> nomesAux = new ArrayList();
        String nomeAux = "-1";
        QuicSortString.quick_sort(nomes, 0, nomes.size() - 1, QuicSortString.TYPE_STRING);

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

    public StringBuffer geraTabelaType(ArrayList<Metrics> listMetrics) {
        //System.out.println("Gerando lista para ordenação...");
        //gera listaPreOrdem
        geraListaPreOrdenacao(listMetrics);

        //System.out.println("Pega o nome das métricas usadas no arquivo(motivo: liberar memória)...");
        //pega o nome das métricas usadas no arquivo
        ArrayList<String> nomes = getNomesMetricasFilter(listMetrics);
        //listMetrics.clear();//a partir daqui não será necessária a utilização dessa lista

        //System.out.println("Ordenando por pacote e classe...");
        //passo 1, ordena todos os pacotes
        QuicSortValue.quick_sort(listaPreOrdem, QuicSortValue.CAMPO_PACKAGE_NAME, 0, listaPreOrdem.size() - 1);

        //System.out.println("Contando classes repetidas...");
        //conta a quantidades de classes 
        ArrayList<Integer> qtdDeClassesRepetidas = getQtdClassesRepetidas();

        //System.out.println("Ordenando métricas por intevalos de classes...");
        //Ordena metrica por intevalos de classes
        ordenarMetricasPorIntervalos(qtdDeClassesRepetidas);

        //System.out.println("Agrupando resultados(com operações aritméticas)...");
        // Agrupando pelas classes
        ArrayList<Value> agrupado = getListaAgrupada();

        //System.out.println("Prepara string para gravação...");
        //imprime na variavel saida
        StringBuffer saida = new StringBuffer("Pacotes,");
        for (String x : nomes) {
            saida.append(x + ",");
        }
        //saida = ";\n";
        String auxp = "-1", auxc = "-1";
        int contNome = 0, contGeral = 0, tamNomes = nomes.size(), tamAgrup = agrupado.size();
        for (Value x : agrupado) {
            //System.out.println("-" + auxv.getPackagi() + ", "+auxv.getName() + ", " + auxv.getAux() + ", " + auxv.getValue());
            if (!x.getPkg().equalsIgnoreCase(auxp) || !x.getName().equalsIgnoreCase(auxc)) {//quando muda de linha
                saida.append("\n" + x.getPkg() + ":" + x.getName());
                //saida += "," + x.getValue();
                contGeral++;
                //verificação se está imprimindo na métrica correta
                contNome = 0;
                if (x.getAux().equalsIgnoreCase(nomes.get(contNome))) {//se estiver imprimindo na metrica correta
                    saida.append("," + x.getValue());
                } else {// senão procura a métrica correta
                    for (int r = contNome; r < tamNomes; r++) {
                        if (x.getAux().equalsIgnoreCase(nomes.get(r))) { //se achou a métrica correta
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
                //saida += "," + x.getValue();
                if (x.getAux().equalsIgnoreCase(nomes.get(contNome))) {//se estiver imprimindo na metrica correta
                    saida.append("," + x.getValue());
                } else {// senão procura a métrica correta
                    for (int r = contNome; r < tamNomes; r++) {
                        if (x.getAux().equalsIgnoreCase(nomes.get(r))) { //se achou a métrica correta
                            saida.append("," + x.getValue());
                            contNome = r;
                            r = tamNomes;// finaliza a procura da métrica correta
                        } else {//se não encontrou a métrica correta, passe para a próxima
                            saida.append(",");
                        }
                    }
                }
            }
            auxp = x.getPkg();
            auxc = x.getName();
            if (contGeral >= tamAgrup) {
                //PODE SER QUE TENHA QUE INSERIR AQUELE CÓDIGO DO TESTE CONTNOME
                //saida.append("," + x.getValue());
            }
        }
        System.out.println("Gravando...");
        return saida;//imprimir(mlp, nomesMetric);
    }

    private ArrayList<String> getNomesMetricasType(ArrayList<Metrics> listMetrics) {
        ArrayList<String> saida = new ArrayList();
        for (Metrics ms : listMetrics) {//listagem de arquivos do projeto
            for (Metric m : ms.getMetricList()) {
                try {
                    if (m.getValues().getPer().equalsIgnoreCase(GenerateTablePerType.IS_TYPE)) {
                        saida.add(m.getId());
                    }
                } catch (NullPointerException cc) {
                    //System.err.println("Per nulo desconsiderado na geração de nomes de métricas:" + cc);
                    //System.err.print(":");
                } catch (Exception cc) {
                    //System.err.println("Erro inesperado na geração de nomes de métricas:" + cc);
                    //cc.printStackTrace();
                }
            }
        }
        return saida;
    }
}
