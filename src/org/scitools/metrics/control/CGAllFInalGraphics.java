/*
 * 
 * 
 */
package org.scitools.metrics.control;

import java.util.ArrayList;
import org.scitools.metrics.model.business.graphics.GenerateDataForGraphics;
import org.scitools.metrics.model.business.graphics.GenerateGraphicsForData;
import org.scitools.metrics.model.util.ManipulaDir;

/**
 *
 * @author fernando
 */
public class CGAllFInalGraphics {

    public static int TYPE_PERCENTILES = 0;
    public static int TYPE_COMPLIANCE_RATE = 1;
    public static int TYPE_COMPLIANCE_RATE_PENALITY = 2;

    public void generateData(String path, ArrayList<String> nomesArquivos, String pathDestino, int type) {
        GenerateDataForGraphics gdfg = new GenerateDataForGraphics();
        ManipulaDir md = new ManipulaDir();
        md.criaDiretorio(pathDestino, "//data-graphics");
        switch (type) {
            case 0:
                gdfg.geraDataGraphicsPercentiles(path, nomesArquivos, pathDestino + "//data-graphics");
                break;
            case 1:
                gdfg.geraDataGraphicsComplianceRate(path, nomesArquivos, pathDestino + "//data-graphics");
                break;
            case 2:
                gdfg.geraDataGraphicsComplianceRatePenality(path, nomesArquivos, pathDestino + "//data-graphics");
                break;
        }

    }

    public void generateGraphics(String path, ArrayList<String> nomesArquivos, String pathDestino, int type, String urlGnuplot, int valorPorcentagemInicial) {
        GenerateGraphicsForData gfd = new GenerateGraphicsForData();
        ManipulaDir md = new ManipulaDir();
        md.criaDiretorio(pathDestino, "//graphics");
        switch (type) {
            case 0:
                gfd.geraGraphicsPercentiles(path, nomesArquivos, pathDestino + "//graphics", urlGnuplot);
                break;
            case 1:
                gfd.geraGraphicsCRate(path, nomesArquivos, pathDestino + "//graphics", urlGnuplot, valorPorcentagemInicial);
                break;
            case 2:
                gfd.geraGraphicsCRatePenality(path, nomesArquivos, pathDestino + "//graphics", urlGnuplot, valorPorcentagemInicial);
                break;
        }

    }
}
