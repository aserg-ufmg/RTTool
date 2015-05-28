/*
 * 
 * 
 */
package org.scitools.metrics.control;

import java.io.File;
import java.util.ArrayList;
import org.scitools.metrics.model.beans.MetricaLinhas;
import org.scitools.metrics.model.business.charts.GenerateDataForCharts;
import org.scitools.metrics.model.util.ManipulaDir;

/**
 *
 * @author fernando
 */
public class CGAllFInalCharts {

    public ArrayList<MetricaLinhas> generateDataCRP(String path, ArrayList<String> nomesArquivos, int limiteInferior) {
        File filex = new File(path);
        ArrayList<String> nomesArquivosx = new ArrayList();
        ManipulaDir.listar(filex, 0, nomesArquivosx);
        nomesArquivosx = getNamesFilter("weigth", nomesArquivosx);
        GenerateDataForCharts gdfg = new GenerateDataForCharts();

        return gdfg.geraDataChartsComplianceRatePenality(path, nomesArquivosx,limiteInferior);

    }
    public ArrayList<MetricaLinhas> generateDataCR(String path, ArrayList<String> nomesArquivos, int limiteInferior) {
        File filex = new File(path);
        ArrayList<String> nomesArquivosx = new ArrayList();
        ManipulaDir.listar(filex, 0, nomesArquivosx);
        nomesArquivosx = getNamesFilter("crate_metric", nomesArquivosx);
        GenerateDataForCharts gdfg = new GenerateDataForCharts();

        return gdfg.geraDataChartsComplianceRate(path, nomesArquivosx,limiteInferior);

    }
    
    public ArrayList<MetricaLinhas> generateDataP(String path, ArrayList<String> nomesArquivos) {
        File filex = new File(path);
        ArrayList<String> nomesArquivosx = new ArrayList();
        ManipulaDir.listar(filex, 0, nomesArquivosx);
        nomesArquivosx = getNamesFilter("metric_", nomesArquivosx);
        GenerateDataForCharts gdfg = new GenerateDataForCharts();
        return gdfg.geraDataChartsPercentile(path, nomesArquivosx);

    }

    public ArrayList<String> getNamesFilter(String subStringPrefix, ArrayList<String> listFilesNames) {
       // System.out.println("Lista de arquivos desprezados");
        ArrayList<String> saida = new ArrayList();
        for (String x : listFilesNames) {
            if (x.startsWith(subStringPrefix)) {
                saida.add(x);
            } else {
                //System.out.println(x);
            }
        }

        //System.out.println("num arquivos filtrados:" + saida.size() + ", N total de aruqivos:" + listFilesNames.size());
        return saida;
    }
}
