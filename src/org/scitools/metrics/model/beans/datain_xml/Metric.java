/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scitools.metrics.model.beans.datain_xml;

/**
 *
 * @author fernando
 */
public class Metric {
 
    private String id;
    private String description;
    private String max;
    private String hint;
    private Values values;

    public Metric() {
        this.id = "";
        this.description = "";
        this.max = "";
        this.hint = "";
        this.values = new Values();
    }

    public Metric(String id, String description, String max, String hint, Values values) {
        this.id = id;
        this.description = description;
        this.max = max;
        this.hint = hint;
        this.values = values;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getMax() {
        return max;
    }

    public String getHint() {
        return hint;
    }

    public Values getValues() {
        return values;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setValues(Values values) {
        this.values = values;
    }
}
