//package org.mathrider.maximaplugin;
package modelo.externos;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.*;
import javax.swing.JOptionPane;

//import errorlist.*;
/**
 *
 * @author tk
 */
public class GnuPlotComandos {

    private String url = "C:\\Program Files\\gnuplot\\bin\\pgnuplot.exe";

    public GnuPlotComandos(String urlGnuplot) {
        url = urlGnuplot;
    }

    public void setUrlGnuPlot(String url) {
        this.url = url;
    }

    public String getUrlGnuPlot() {
        return url;
    }

    public void exec(ArrayList<String> comandos) {
        try {
            Process p = Runtime.getRuntime().exec(url);
//            try {
//                Thread.sleep(500);
//            } catch (Exception x) {
//            }
            OutputStream outputStream = p.getOutputStream(); //process p
            PrintWriter gp = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
            for (String comando : comandos) {
                gp.println(comando);
                gp.flush();

            }
            gp.close();
        } catch (Exception x) {
            System.out.println(x.getMessage());
        }
    }

    public static void main(String[] args) {
        //GnuPlotComandos ece = new GnuPlotComandos("C:\\Program Files\\gnuplot\\bin\\pgnuplot.exe");
        GnuPlotComandos ece = new GnuPlotComandos("gnuplot");
        String[] teste = new String[7];
//        teste[0] = "reset\n";
//        teste[1] = "set term png transparent\n";
//        teste[2] = "set output \"/home/fernando/testando.png\"\n";
//        teste[3] = "set multiplot\n";
//        teste[4] = "plot sin(x)\n";
//        teste[5] = "unset multiplot\n";
//        teste[6] = "exit";

        teste[0] = "reset\n";
        teste[1] = "set term png \n";
        teste[2] = "set output \"/home/fernando/testando.png\"\n";
        teste[3] = "set multiplot\n";
        teste[4] = "plot sin(x)\n";
        teste[5] = "unset multiplot\n";
        teste[6] = "exit";
        //ece.exec(teste);
    }
}//end class.
// :indentSize=4:lineSeparator=\n:noTabs=false:tabSize=4:folding=explicit:collapseFolds=1:

