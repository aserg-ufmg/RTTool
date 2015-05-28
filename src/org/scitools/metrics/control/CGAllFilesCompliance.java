/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scitools.metrics.control;

import java.util.ArrayList;
import org.scitools.metrics.model.business.GenerateCompliancePerMetric;
import org.scitools.metrics.model.util.ManipulaDir;
import org.scitools.metrics.model.util.RWArquivoNew;

/**
 *
 * @author fernando
 */
public class CGAllFilesCompliance {

    public ArrayList<String> getNamesFilter(String subStringOfEliminate, ArrayList<String> listFilesNames) {
        //System.out.println("Lista de arquivos desprezados");
        ArrayList<String> saida = new ArrayList();
        for (String x : listFilesNames) {
            if (!x.contains(subStringOfEliminate)) {
                saida.add(x);
            } else {
                //System.out.println(x);
            }
        }
        // System.out.println("num arquivos filtrados:" + saida.size() + ", N total de aruqivos:" + listFilesNames.size());
        return saida;
    }

    public void geraCompilances(String pathOrigem, String pathDestino, String nomeArquivo, int min, int tail_mediana, int wIdeal, int wReal) {
        GenerateCompliancePerMetric gcpm = new GenerateCompliancePerMetric();
        RWArquivoNew f = new RWArquivoNew();
        ManipulaDir md = new ManipulaDir();
        md.criaDiretorio(pathDestino, "//data-compliance");
        try {
            f.setUrl(pathDestino + "//data-compliance" + "//" + nomeArquivo.substring(0, nomeArquivo.length() - 4) + "_T" + tail_mediana + "_m" + min + "_wi" + wIdeal + "_wr" + wReal + ".csv");
            f.criaArq();
            f.escreveLinha(gcpm.getTabelaComplianceUnique(pathOrigem, nomeArquivo, min, tail_mediana, wIdeal, wReal));
            f.fechaArq();
        } catch (Exception x) {
            System.err.println(x);
//x.printStackTrace();
        }
    }
}
