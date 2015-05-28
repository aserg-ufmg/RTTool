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
public class Metrics {
    private String scope;
    private String type;
    private String date;
    private String xmlns;
    private ArrayList<Metric> metricList;

    public Metrics() {
        this.scope = "";
        this.type = "";
        this.date = "";
        this.xmlns = "";
        this.metricList = new ArrayList<Metric>();
    }

    public Metrics(String scope, String type, String date, String xmlns, ArrayList<Metric> metricList) {
        this.scope = scope;
        this.type = type;
        this.date = date;
        this.xmlns = xmlns;
        this.metricList = metricList;
    }

    public String getScope() {
        return scope;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getXmlns() {
        return xmlns;
    }

    public ArrayList<Metric> getMetricList() {
        return metricList;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    public void setMetricList(ArrayList<Metric> metricList) {
        this.metricList = metricList;
    }
    
    
    
}
