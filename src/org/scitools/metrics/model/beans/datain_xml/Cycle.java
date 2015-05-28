/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scitools.metrics.model.beans.datain_xml;

import java.util.ArrayList;

/**
 *
 * @author fernando
 */
public class Cycle {
    private String name;
    private String nodes;
    private String diameter;
    private ArrayList<String> packagi;

    public Cycle() {
        this.name = "";
        this.nodes = "";
        this.diameter = "";
        this.packagi = new ArrayList<String>();
    }

    public Cycle(String name, String nodes, String diameter, ArrayList<String> packagi) {
        this.name = name;
        this.nodes = nodes;
        this.diameter = diameter;
        this.packagi = packagi;
    }

    public String getName() {
        return name;
    }

    public String getNodes() {
        return nodes;
    }

    public String getDiameter() {
        return diameter;
    }

    public ArrayList<String> getPackagi() {
        return packagi;
    }
    
    
}
