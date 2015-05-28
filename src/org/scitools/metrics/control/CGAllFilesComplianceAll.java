package org.scitools.metrics.control;

import java.util.ArrayList;
import org.scitools.metrics.model.beans.Compliance;
import org.scitools.metrics.model.business.GenerateCompliancePerMetric;
import org.scitools.metrics.model.util.ManipulaDir;
import org.scitools.metrics.model.util.RWArquivoNew;

/**
 *
 * @author fernando
 */
public class CGAllFilesComplianceAll {

    public ArrayList<String> getNamesFilter(String subStringOfEliminate, ArrayList<String> listFilesNames) {
        //System.out.println("Lista de arquivos desprezados");
        ArrayList<String> saida = new ArrayList();
        for (String x : listFilesNames) {
            if (!x.contains(subStringOfEliminate)) {
                saida.add(x);
            } else {
               // System.out.println(x);
            }
        }

        //System.out.println("num arquivos filtrados:" + saida.size() + ", N total de aruqivos:" + listFilesNames.size());
        return saida;
    }

    public void geraCompilances(String pathOrigem, String pathDestino, String nomeArquivo, int min, int tail_mediana, int wIdeal, int wReal) {
        GenerateCompliancePerMetric gcpm = new GenerateCompliancePerMetric();
        RWArquivoNew f = new RWArquivoNew();
        Compliance c = new Compliance();
        ManipulaDir md = new ManipulaDir();
        md.criaDiretorio(pathDestino, "//data-compliance");
        int i = 0;
        c = gcpm.getTabelaComplianceAll(pathOrigem, nomeArquivo, min, tail_mediana, wIdeal, wReal);
        try {
            f.setUrl(pathDestino + "//data-compliance" + "//weigth_" + nomeArquivo.substring(0, nomeArquivo.length() - 4) + ".csv");
            StringBuffer x = c.getiPerPercentiles_weigth();
            if (x != null) {
                f.criaArq();
                f.escreveLinha(x);
                f.fechaArq();
            }
        } catch (Exception x) {
            //     x.printStackTrace();
        }
        try {
            f.setUrl(pathDestino + "//data-compliance" + "//crate_" + nomeArquivo.substring(0, nomeArquivo.length() - 4) + ".csv");
            StringBuffer x = c.getiPerPercentiles_c();
            if (x != null) {
                f.criaArq();
                f.escreveLinha(x);
                f.fechaArq();
            }
        } catch (Exception x) {
//            x.printStackTrace();
        }
        try {
            f.setUrl(pathDestino + "//data-compliance" + "//k_" + nomeArquivo.substring(0, nomeArquivo.length() - 4) + ".csv");
            StringBuffer x = c.getiPerPercentiles_k();
            if (x != null) {
                f.criaArq();
                f.escreveLinha(c.getiPerPercentiles_k());
                f.fechaArq();
            }
        } catch (Exception x) {
            //          x.printStackTrace();
        }
        try {
            f.setUrl(pathDestino + "//data-compliance" + "//p1_" + nomeArquivo.substring(0, nomeArquivo.length() - 4) + ".csv");
            StringBuffer x = c.getiPerPercentiles_p1();
            if (x != null) {
                f.criaArq();
                f.escreveLinha(x);
                f.fechaArq();
            }
        } catch (Exception x) {
            //        x.printStackTrace();
        }
        try {
            f.setUrl(pathDestino + "//data-compliance" + "//p2_" + nomeArquivo.substring(0, nomeArquivo.length() - 4) + ".csv");
            StringBuffer x = c.getiPerPercentiles_p2();
            if (x != null) {
                f.criaArq();
                f.escreveLinha(x);
                f.fechaArq();
            }
        } catch (Exception x) {
            //      x.printStackTrace();
        }
    }
}
