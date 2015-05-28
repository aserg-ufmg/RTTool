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
public class Values {

    private String per;
    private String avg;
    private String stddev;
    private String max;
    private String total;
    private ArrayList<Value> valueList;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Values() {
        per = "";
        avg = "";
        stddev = "";
        max = "";
        valueList = new ArrayList<Value>();
    }

    public Values(String per, String avg, String stddev, String max, ArrayList<Value> valueList) {
        this.per = per;
        this.avg = avg;
        this.stddev = stddev;
        this.max = max;
        this.valueList = valueList;
    }

    public String getPer() {
        return per;
    }

    public String getAvg() {
        return avg;
    }

    public String getStddev() {
        return stddev;
    }

    public String getMax() {
        return max;
    }

    public ArrayList<Value> getValueList() {
        return valueList;
    }

    public void setPer(String per) {
        this.per = per;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    public void setStddev(String stddev) {
        this.stddev = stddev;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public void setValueList(ArrayList<Value> valueList) {
        this.valueList = valueList;
    }
}
