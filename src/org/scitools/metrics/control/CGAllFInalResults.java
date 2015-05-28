/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scitools.metrics.control;

import java.util.ArrayList;
import org.scitools.metrics.model.beans.FinalResult;
import org.scitools.metrics.model.business.GenerateFinalResults;
import org.scitools.metrics.model.util.QuicSortString;

/**
 *
 * @author fernando
 */
public class CGAllFInalResults {

    public ArrayList<String> getMetricasEmArquivos(String path, ArrayList<String> nomesArquivos) {
        ArrayList<String> todas = new ArrayList();
        //ter todas os nomes de métricas dos arquivos

        for (String aux : nomesArquivos) {
            todas.add(aux.substring(13, aux.length() - 1));
        }
        //ordena métricas
        QuicSortString.quick_sort(todas, 0, todas.size() - 1, QuicSortString.TYPE_STRING);

        //agrupa métricas
        ArrayList<String> auxTodas = new ArrayList();
        String nomeAux = "";
        for (String nome : todas) {
            if (!nomeAux.equalsIgnoreCase(nome)) {
                auxTodas.add(nome);
            }
            nomeAux = nome;
        }
        todas = auxTodas;
        return todas;
    }

    public ArrayList<StringBuffer> geraOutlayersNew(String pathDefaultFiles, ArrayList<String> nomeArquivoPercentil, ArrayList<FinalResult> resultadosMetricas) {
        GenerateFinalResults gfr = new GenerateFinalResults();

        return gfr.getOutlayersNew(pathDefaultFiles, nomeArquivoPercentil, resultadosMetricas);
    }

    public ArrayList<StringBuffer> geraOutlayers(String pathDefaultFiles, ArrayList<String> nomeArquivo, ArrayList<FinalResult> resultadosMetricas, boolean isNewPattern) {
        GenerateFinalResults gfr = new GenerateFinalResults();

        return gfr.getOutlayers(pathDefaultFiles, nomeArquivo, resultadosMetricas, isNewPattern);
    }

    private ArrayList<String> getNamesFilter(String subStringOfActive, ArrayList<String> listFilesNames) {
        //System.out.println("Lista de arquivos desprezados");
        ArrayList<String> saida = new ArrayList();
        for (String x : listFilesNames) {
            if (x.contains(subStringOfActive)) {
                saida.add(x);
            } else {
                //System.out.println(x);
            }
        }
        //this.listFilesNames = saida;
        //System.out.println("num arquivos filtrados:" + saida.size() + ", N total de aruqivos:" + listFilesNames.size());
        return saida;
    }

    public ArrayList<FinalResult> geraFinalResults(String pathOrigem, ArrayList<String> nomesArquivos) {
        GenerateFinalResults gfr = new GenerateFinalResults();
        return gfr.getTabelaFinalResults(pathOrigem, getNamesFilter("weigth", nomesArquivos));
    }
}
