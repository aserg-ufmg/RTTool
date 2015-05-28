package org.scitools.metrics.model.business;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.scitools.metrics.model.beans.datain_xml.*;

/**
 *
 * @author fernando
 */
public class ReadXML {

    public static void readXML(String path_archive, Metrics dadoMetrics, boolean ifNotText) {
        File f = new File(path_archive);

        SAXBuilder builder = new SAXBuilder();

        Document doc1 = null;
        try {
            doc1 = builder.build(f);
        } catch (Exception x) {
            //System.err.println("falha ao ler arquivo;" + x);
            //x.printStackTrace();
        }

        Element metrics = (Element) doc1.getRootElement(); //metrics

        //Metrics dadoMetrics = new Metrics();
        dadoMetrics.setScope(metrics.getAttributeValue("scope"));
        dadoMetrics.setDate(metrics.getAttributeValue("date"));
        /*System.out.println(" Atributos metrics: " + metrics.getAttributeValue("scope")
         + ", " + metrics.getAttributeValue("type")
         + ", " + metrics.getAttributeValue("date")
         + ", " + metrics.getAttributeValue("xmlns"));//scope type date xmlns 
         */


        List lMetric = metrics.getChildren();//metric podem ter mais de, mas vamos pegar só um values dela
        Iterator i = lMetric.iterator();
        ArrayList<Metric> listMetric = new ArrayList();
        while (i.hasNext()) { //listagem dos metric 
            Element metric_cycle = (Element) i.next();
            if (metric_cycle.getAttributeValue("id") == null) {// verifica que o atributo de metric nÃ£o existe
                //atributos de cycles
                /*System.out.println(" Atributos Cycles: " + metric_cycle.getAttributeValue("name")
                 + ", " + metric_cycle.getAttributeValue("nodes")
                 + ", " + metric_cycle.getAttributeValue("diamenter")
                 + "++++++");
                 //listagem de packages
                 List lPackages = metric_cycle.getChildren();
                 Iterator q = lPackages.iterator();
                 while (q.hasNext()) { 
                 Element packages = (Element) q.next();
                 System.out.println(" -" + packages.getText());
                 }*/
            } else {
                //se 'e metric
                Metric dadoMetric = new Metric();
                dadoMetric.setId(metric_cycle.getAttributeValue("id"));
                dadoMetric.setDescription(metric_cycle.getAttributeValue("description"));
                listMetric.add(dadoMetric);
                /*System.out.println("Metric ID:" + metric_cycle.getAttributeValue("id")
                 + ", Description:" + metric_cycle.getAttributeValue("description"));
                 */
                List lValues = metric_cycle.getChildren();//essa lista é atoa, pois é só um values
                Iterator w = lValues.iterator();
                /// não é necessário criar lista de values

                Values dadoValues = new Values();
                while (w.hasNext()) { //listagem dos values
                    Element values = (Element) w.next();//só existe um values para qualquer metric, lista inutil;


                    dadoValues.setPer(values.getAttributeValue("per"));
                    dadoValues.setTotal(values.getAttributeValue("total"));

                    //System.out.println("  Per:" + values.getAttributeValue("per"));//per avg stddev max
                    List lValue = values.getChildren();// existem vários value
                    Iterator j = lValue.iterator();
                    ArrayList<Value> listValue = new ArrayList();
                    while (j.hasNext()) { //listagem dos value

                        Element value = (Element) j.next();//só existe um values para qualquer metric;
                        if (ifNotText) {//se o usuário escolheu não isar dados com a substrinc text nas classes ou pacotes
                            try {
                                String auxSource = value.getAttributeValue("source");
                                if (auxSource == null) {
                                    auxSource = "";
                                }
                                String auxName = value.getAttributeValue("name");
                                if (auxName == null) {
                                    auxName = "";
                                }
                                if (auxName.contains("test") || auxSource.contains("test") || value.getAttributeValue("package").contains("test")) {
                                    /*System.out.println("   Name:" + value.getAttributeValue("name")
                                     + ", Source:" + value.getAttributeValue("source")
                                     + ", Package" + value.getAttributeValue("package")
                                     + ", Value" + value.getAttributeValue("value"));*/
                                    //System.err.print("-");
//                                    System.out.println("Valores desconsiderados 'TEST' Name:" + value.getAttributeValue("name")
//                                            + ", Source:" + value.getAttributeValue("source")
//                                            + ", Package" + value.getAttributeValue("package")
//                                            + ", Value" + value.getAttributeValue("value"));
                                } else {
                                    Value dadoValue = new Value();
                                    dadoValue.setName(auxName);
                                    dadoValue.setSource(auxSource);
                                    dadoValue.setPkg(value.getAttributeValue("package"));
                                    dadoValue.setValue(value.getAttributeValue("value"));
                                    dadoValue.setAux(metric_cycle.getAttributeValue("id"));
                                    listValue.add(dadoValue);
                                }
                            } catch (NullPointerException cc) {
                                //System.err.println("Teste em Per nulo na lista PreOrdem:" + cc);
                                //System.err.println(value.getAttributeValue("name") + "." + value.getAttributeValue("value"));
//                                System.err.println("Valores considerados nulos Name:" + value.getAttributeValue("name")
//                                        + ", Source:" + value.getAttributeValue("source")
//                                        + ", Package" + value.getAttributeValue("package")
//                                        + ", Value" + value.getAttributeValue("value"));
                            } catch (Exception cc) {
                                //System.err.println("Erro inesperado ao Carregar Dados:" + cc);
                                //cc.printStackTrace();
                            }
                        } else {//senão, se o usuário optou por não excluir 
                            Value dadoValue = new Value();
                            dadoValue.setName(value.getAttributeValue("name"));
                            dadoValue.setSource(value.getAttributeValue("source"));
                            dadoValue.setPkg(value.getAttributeValue("package"));
                            dadoValue.setValue(value.getAttributeValue("value"));
                            dadoValue.setAux(metric_cycle.getAttributeValue("id"));
                            listValue.add(dadoValue);

                        }
                    }
                    dadoValues.setValueList(listValue);
                }
                dadoMetric.setValues(dadoValues);
            }
        }
        dadoMetrics.setMetricList(listMetric);
    }
}
