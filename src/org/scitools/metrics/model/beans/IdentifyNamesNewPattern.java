/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scitools.metrics.model.beans;

/**
 *
 * @author fernando
 */
public class IdentifyNamesNewPattern {
   
    public static String getMetricOfMnemonic(String mnemonic){
        String saida = "";
        switch (mnemonic) {
            case "WMC":
                saida = "weightedMethodCount";
                break;
            case "WOC":
                saida = "weightOfAClass";
                break;
            case "TNOC":
                saida = "totalNumberOfChildren";
                break;
            case "MEO":
                saida = "numberOfMethodsOverriden";
                break;
            case "FAN-OUT":
                saida = "fanOut";
                break;
            case "FAN-IN":
                saida = "fanIn";
                break;
            case "NOC":
                saida = "numberOfChildren";
                break;
            case "PUBA":
                saida = "numberOfPublicAttributes";
                break;
            case "INHA":
                saida = "numberOfAttributesInherited";
                break;
            case "PRIA":
                saida = "numberOfPrivateAttributes";
                break;
            case "NOA":
                saida = "numberOfAttributes";
                break;
            case "INHM":
                saida = "numberOfMethodsInherited";
                break;
            case "PRIM":
                saida = "numberOfPrivateMethods";
                break;
            case "PUBM":
                saida = "numberOfPublicMethods";
                break;
            case "NOM":
                saida = "numberOfMethods";
                break;
            case "LOC":
                saida = "numberOfLinesOfCode";
                break;
            case "CBO":
                saida = "couplingBetweenClasses";
                break;
            case "LCOM":
                saida = "lackOfCohesionInMethods";
                break;
            case "RFC":
                saida = "responseForClass";
                break;
            case "HNL":
                saida = "hierarchyNestingLevel";
                break;
            default:
                saida = mnemonic;
        }
        return saida;
    }
    
    public static String getMnemonicOfMetric(String metric){
        String saida = "";
        switch (metric) {
            case "weightedMethodCount":
                saida = "WMC";
                break;
            case "weightOfAClass":
                saida = "WOC";
                break;
            case "totalNumberOfChildren":
                saida = "TNOC";
                break;
            case "numberOfMethodsOverriden":
                saida = "MEO";
                break;
            case "fanOut":
                saida = "FAN-OUT";
                break;
            case "fanIn":
                saida = "FAN-IN";
                break;
            case "numberOfChildren":
                saida = "NOC";
                break;
            case "numberOfPublicAttributes":
                saida = "PUBA";
                break;
            case "numberOfAttributesInherited":
                saida = "INHA";
                break;
            case "numberOfPrivateAttributes":
                saida = "PRIA";
                break;
            case "numberOfAttributes":
                saida = "NOA";
                break;
            case "numberOfMethodsInherited":
                saida = "INHM";
                break;
            case "numberOfPrivateMethods":
                saida = "PRIM";
                break;
            case "numberOfPublicMethods":
                saida = "PUBM";
                break;
            case "numberOfMethods":
                saida = "NOM";
                break;
            case "numberOfLinesOfCode":
                saida = "LOC";
                break;
            case "couplingBetweenClasses":
                saida = "CBO";
                break;
            case "lackOfCohesionInMethods":
                saida = "LCOM";
                break;
            case "responseForClass":
                saida = "RFC";
                break;
            case "hierarchyNestingLevel":
                saida = "HNL";
                break;
            default:
                saida = metric;
        }
        return saida;
    }
}
