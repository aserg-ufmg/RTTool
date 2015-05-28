/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scitools.metrics.model.business;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.scitools.metrics.model.beans.datain_xml.Value;

/**
 *
 * @author fernando
 */
public class ReadCSV {

    public static char SEPARADOR_VIGULA = ',';
    public static char SEPARADOR_PONTOVIGULA = ';';
    public static char SEPARADOR_DOISPONTOS = ':';
    private String urlArquivo;

    public String getUrlArquivo() {
        return urlArquivo;
    }

    public void setUrlArquivo(String urlArquivo) {
        this.urlArquivo = urlArquivo;
    }

    public ArrayList<String> getColuna(String colunaMetrica, char SEPARADOR) {
        ArrayList<String> linhas = new ArrayList();
        colunaMetrica = colunaMetrica.replaceAll("\"", "");
        try {
            BufferedReader in = new BufferedReader(new FileReader(urlArquivo));
            String str;
            Value aux = new Value();
            int cont = 0, col = -1;

            while ((str = in.readLine()) != null) {
                String x[] = str.replaceAll("\"", "").split(SEPARADOR + "");
                if (cont == 0) {// se for a primeira linha, verifica a coluna
                    for (int i = 0; i < x.length; i++) {// procura indice da coluna da métrica
                        if (colunaMetrica.equalsIgnoreCase(x[i])) {
                            col = i;
                        }
                    }
                } else {
                    if (col == -1) {
                        break;
                    }
                    if (x.length > 1) {// a linha tem que existir 
                        linhas.add(x[col]);
                    }
                }
                cont++;
            }
            in.close();
        } catch (IOException e) {
            //e.printStackTrace(); // pelo menos imprima o stack trace !   
        }
        return linhas;
    }

    public ArrayList<String> getColuna(int coluna, char SEPARADOR) {
        ArrayList<String> linhas = new ArrayList();
        try {
            BufferedReader in = new BufferedReader(new FileReader(urlArquivo));
            String str;
            while ((str = in.readLine()) != null) {
                try {
                    linhas.add(str.replaceAll("\"", "").split(SEPARADOR + "")[coluna]);
                } catch (Exception x) {
                    //ultima linha sem número, sem colunas
                    //System.err.println("texto: "+str);
                    //x.printStackTrace();
                }
            }
            in.close();
        } catch (IOException e) {
            //e.printStackTrace(); // pelo menos imprima o stack trace !   
        }
        return linhas;
    }

    public ArrayList<Float> getColunaFloat(int coluna, char SEPARADOR) {
        ArrayList<Float> linhas = new ArrayList();
        try {
            BufferedReader in = new BufferedReader(new FileReader(urlArquivo));
            String str;
            while ((str = in.readLine()) != null) {
                try {
                    linhas.add(Float.parseFloat(str.replaceAll("\"", "").split(SEPARADOR + "")[coluna]));
                } catch (Exception x) {
                    //ultima linha sem número, sem colunas
                    //System.err.println("texto: "+str);
                    //x.printStackTrace();
                }
            }
            in.close();
        } catch (IOException e) {
            //e.printStackTrace(); // pelo menos imprima o stack trace !   
        }
        return linhas;
    }
    
    public String[] getLinha(int linha, char SEPARADOR) {
        String[] linhas = null;
        try {
            BufferedReader in = new BufferedReader(new FileReader(urlArquivo));
            String str;
            int cont = 0;
            while ((str = in.readLine()) != null) {
                if (cont == linha) {
                    linhas = str.replaceAll("\"", "").split(SEPARADOR + "");
                    break;
                }
                cont++;
            }
            in.close();
        } catch (IOException e) {
            //e.printStackTrace(); // pelo menos imprima o stack trace !   
        }
        return linhas;
    }

    public int getQtdColunas(char SEPARADOR) {
        int qtdColunas = 0;
        try {
            BufferedReader in = new BufferedReader(new FileReader(urlArquivo));
            String str;
            if ((str = in.readLine()) != null) {
                try {
                    qtdColunas = str.split(SEPARADOR + "").length;
                } catch (Exception x) {
                    //System.err.println("Erro ao pegar qtd colunas do arquivo: " + urlArquivo);
                    //x.printStackTrace();
                }
            }
            in.close();
        } catch (IOException e) {
            //e.printStackTrace(); // pelo menos imprima o stack trace !   
        }
        return qtdColunas;
    }

    public ArrayList<Value> getValueColuna(int coluna, char SEPARADOR) {
        ArrayList<Value> linhas = new ArrayList();
        try {
            BufferedReader in = new BufferedReader(new FileReader(urlArquivo));
            String str;
            Value aux = new Value();
            while ((str = in.readLine()) != null) {
                String x[] = str.replaceAll("\"", "").split(SEPARADOR + "");
                aux.setPkg(x[0].split(":")[0]);
                aux.setSource(x[0].split(":")[1]);
                aux.setValue(x[coluna]);
                linhas.add(aux);
            }
            in.close();
        } catch (IOException e) {
            //e.printStackTrace(); // pelo menos imprima o stack trace !   
        }
        return linhas;
    }

    public ArrayList<Value> getValueColuna(String colunaMetrica, char SEPARADOR) {
        ArrayList<Value> linhas = new ArrayList();
        try {
            BufferedReader in = new BufferedReader(new FileReader(urlArquivo));
            String str;
            Value aux = new Value();
            int cont = 0, col = -1;
            colunaMetrica = colunaMetrica.replaceAll("\"", "");
            while ((str = in.readLine()) != null) {
                String x[] = str.replaceAll("\"", "").split(SEPARADOR + "");
                if (cont == 0) {// se for a primeira linha, verifica a coluna
                    for (int i = 0; i < x.length; i++) {// procura indice da coluna da métrica
                        if (colunaMetrica.equalsIgnoreCase(x[i])) {
                            col = i;
                        }
                    }
                } else {
                    if (col == -1) {
                        break;
                    }
                    aux = new Value();
                    aux.setPkg(x[0].split(":")[0]);
                    //estre tratamento de excessão é só por causa dos arquivos pkg, que não possuem classe
                    try {
                        aux.setSource(x[0].split(":")[1]);
                    } catch (NullPointerException k) {
                    } catch (Exception k) {
                    }
                    aux.setAux(colunaMetrica);
                    try {
                        aux.setValue(x[col]);
                    } catch (ArrayIndexOutOfBoundsException k) {
                        //System.err.println("Sem conteúdo: " + str);
                    } catch (Exception k) {
                        //k.printStackTrace();
                    }
                    linhas.add(aux);
                }
                cont++;
            }
            in.close();
        } catch (IOException e) {
            //e.printStackTrace(); // pelo menos imprima o stack trace !   
        }
        return linhas;
    }

    public ArrayList<Float> getValueColunaNewPattern(String colunaMetrica, char SEPARADOR) {
        ArrayList<Float> linhas = new ArrayList();
        try {
            BufferedReader in = new BufferedReader(new FileReader(urlArquivo));
            String str;
            Value aux = new Value();
            int cont = 0, col = -1;
            colunaMetrica = colunaMetrica.replaceAll("\"", "");
            while ((str = in.readLine()) != null) {
                String x[] = str.replaceAll("\"", "").split(SEPARADOR + "");
                if (cont == 0) {// se for a primeira linha, verifica a coluna
                    for (int i = 0; i < x.length; i++) {// procura indice da coluna da métrica
                        if (colunaMetrica.equalsIgnoreCase(x[i])) {
                            col = i;
                        }
                    }
                } else {
                    if (col == -1) {
                        break;
                    }
                    try {
                        linhas.add(Float.parseFloat(x[col]));
                    } catch (ArrayIndexOutOfBoundsException k) {
                        //System.err.println("Sem conteúdo: " + str);
                    } catch (Exception k) {
                        //k.printStackTrace();
                    }
                }
                cont++;
            }
            in.close();
        } catch (IOException e) {
            //e.printStackTrace(); // pelo menos imprima o stack trace !   
        }
        return linhas;
    }

    public ArrayList<String> getNomesMetricas(char SEPARADOR) {
        ArrayList<String> metricas = new ArrayList();
        try {
            BufferedReader in = new BufferedReader(new FileReader(urlArquivo));
            String str;

            int cont = 0, col = 0;

            if ((str = in.readLine()) != null) {
                String x[] = str.replaceAll("\"", "").split(SEPARADOR + "");
                for (int i = 1; i < x.length; i++) {// procura indice da coluna da métrica
                    metricas.add(x[i]);
                }
            }
            in.close();
        } catch (IOException e) {
            //e.printStackTrace(); // pelo menos imprima o stack trace !   
        }
        return metricas;
    }

    public ArrayList<String> getCabecalhos(char SEPARADOR, int inicio, int fim) {
        ArrayList<String> metricas = new ArrayList();
        try {
            BufferedReader in = new BufferedReader(new FileReader(urlArquivo));
            String str;

            int cont = 0, col = 0;

            if ((str = in.readLine()) != null) {
                String x[] = str.replaceAll("\"", "").split(SEPARADOR + "");
                for (int i = 1; i < x.length; i++) {// procura indice da coluna da métrica
                    if (i >= inicio && i <= fim) {
                        metricas.add(x[i]);
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            //e.printStackTrace(); // pelo menos imprima o stack trace !   
        }
        return metricas;
    }

    public ArrayList<String> getCabecalhos(char SEPARADOR) {
        ArrayList<String> metricas = new ArrayList();
        try {
            BufferedReader in = new BufferedReader(new FileReader(urlArquivo));
            String str;
            int cont = 0, col = 0;
            if ((str = in.readLine()) != null) {
                String x[] = str.split(SEPARADOR + "");
                for (int i = 1; i < x.length; i++) {// procura indice da coluna da métrica
                    metricas.add(x[i]);
                }
            }
            in.close();
        } catch (IOException e) {
            //e.printStackTrace(); // pelo menos imprima o stack trace !   
        }
        return metricas;
    }
}
