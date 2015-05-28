/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scitools.metrics.model.business;

import java.util.ArrayList;
import org.scitools.metrics.model.beans.datain_xml.*;
import org.scitools.metrics.model.util.QuicSortString;
import org.scitools.metrics.model.util.QuicSortValue;

/**
 *
 * @author Fernando Paim Lima
 */
public class GenerateTablePerPackages {

    public static String IS_PACKAGE = "packageFragment";

    public ArrayList<Value> getListaPreOrdem(ArrayList<Metrics> listMetrics) {
        ArrayList<Value> listaPreOrdem = new ArrayList();
        for (Metrics ms : listMetrics) {//listagem de arquivos do projeto
            ArrayList<Metric> ml = ms.getMetricList();
            for (Metric m : ml) { //listagem de metric
                try {
                    //verifica que a métric está por pacote
                    if (m.getValues().getPer().equalsIgnoreCase(GenerateTablePerPackages.IS_PACKAGE)) {
                        listaPreOrdem.addAll(m.getValues().getValueList());
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
        return listaPreOrdem;
    }

    private ArrayList<Integer> getQtdPacotesRepetidos(ArrayList<Value> listaPreOrdem) {
        ArrayList<Integer> qtdDePacotesRepetidos = new ArrayList();
        String auxp = "-1";
        int i = 0, pp = 0, cont = 0, tam = listaPreOrdem.size();
        for (Value x : listaPreOrdem) {
            if (!x.getPkg().equalsIgnoreCase(auxp) && !auxp.equalsIgnoreCase("-1")) {
                qtdDePacotesRepetidos.add(i);
                i = 1;
                cont++;//contador geral
            } else {
                i++;
                cont++;//contador geral
                if (cont >= (tam)) {
                    qtdDePacotesRepetidos.add(i);
                }
            }
            auxp = x.getPkg();
        }
        return qtdDePacotesRepetidos;
    }

    private ArrayList<Value> getAgrupamentoPacotes(ArrayList<Value> listaPreOrdem) {
        float sumValue = 0;
        int tamLPO = listaPreOrdem.size(), contlpo = 0;
        ArrayList<Value> agrupado = new ArrayList();
        Value auxv = new Value("", "", "-1", "");
        auxv.setAux("-1");
        for (Value x : listaPreOrdem) {
            if ((!x.getPkg().equalsIgnoreCase(auxv.getPkg()) && !auxv.getPkg().equalsIgnoreCase("-1"))
                    || (!x.getAux().equalsIgnoreCase(auxv.getAux()) && !auxv.getAux().equalsIgnoreCase("-1"))) {

                auxv.setValue(sumValue + "");
                agrupado.add(auxv);
                contlpo++;
                if (contlpo >= tamLPO) {// se o no final tiver mais um valor+++++++++++++++++++
                    agrupado.add(x);
                }
                try {
                    sumValue = Float.parseFloat(x.getValue());
                } catch (Exception ee) {
                    //System.err.println("Incompatibilidade de carctere: " + ee + "; Name:" + x.getName() + "; Value:" + x.getValue());
                    //   ee.printStackTrace();
                }
            } else {
                try {
                    sumValue += Float.parseFloat(x.getValue());
                } catch (Exception ee) {
                    //System.err.println("Incompatibilidade de carctere: " + ee + "; " + x.getValue());
                    // ee.printStackTrace();
                }
                contlpo++;
                if (contlpo >= tamLPO) {// se o no final tiver mais um valor+++++++++++++++++++
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

    private StringBuffer getStringBuffer(ArrayList<String> nomes, ArrayList<Value> agrupado) {
        //imprime na variavel saida
        StringBuffer saida = new StringBuffer("Pacotes,");
        for (String x : nomes) {
            saida.append(x + ",");
        }
        String auxp = "-1";
        int contNome = 0, contGeral = 0, tamNomes = nomes.size(), tamAgrup = agrupado.size();
        for (Value x : agrupado) {
            if (!x.getPkg().equalsIgnoreCase(auxp)) {//quando muda de linha
                saida.append("\n" + x.getPkg());
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
        }
        return saida;
    }

    public StringBuffer geraTabelaPacote(ArrayList<Metrics> listMetrics) {
        //Adiciona na lista preordem
        ArrayList<Value> listaPreOrdem = getListaPreOrdem(listMetrics);

        //passo 1, ordena todos os pacotes
        QuicSortValue.quick_sort(listaPreOrdem, QuicSortValue.CAMPO_PACKAGE, 0, listaPreOrdem.size() - 1);

        //gerando a quantidade de pacotes
        ArrayList<Integer> qtdDePacotesRepetidos = getQtdPacotesRepetidos(listaPreOrdem);

        //Ordena por metrica
        int i = 0, pp = 0;
        for (int t : qtdDePacotesRepetidos) {
            pp += t;
            QuicSortValue.quick_sort(listaPreOrdem, QuicSortValue.CAMPO_AUX, i, pp - 1);
            i += t;
        }

        // Agrupar por pacote e metric
        ArrayList<Value> agrupado = getAgrupamentoPacotes(listaPreOrdem);

        //preparando para imprimir tabela
        ArrayList<String> nomes = getNomesMetricasPacotes(listMetrics);
        ArrayList<String> nomesAux = new ArrayList<>();
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
        //listMetrics.clear(); // não é mais necessária, liberando memória

        //imprime tabela na variavel saida
        StringBuffer saida = getStringBuffer(nomes, agrupado);

        return saida;
    }

    private ArrayList<String> getNomesMetricasPacotes(ArrayList<Metrics> listMetrics) {
        int total = 0, i = 0;
        ArrayList<String> saida = new ArrayList();
        for (Metrics ms : listMetrics) {//listagem de arquivos do projeto
            for (Metric m : ms.getMetricList()) {
                try {
                    if (m.getValues().getPer().equalsIgnoreCase(GenerateTablePerPackages.IS_PACKAGE)) {
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
