/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gemalto.split.beans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aldmendo
 */
public class Outputfile {
    private HeaderKey headerKey=new HeaderKey();
    private String fileName;
    private List<Record> records=new ArrayList<Record>();

    public Outputfile(HeaderKey key){
        this.headerKey=key;
    }
    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    
    public HeaderKey getHeaderKey() {
        return headerKey;
    }

    public void setHeaderKey(HeaderKey headerKey) {
        this.headerKey = headerKey;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
    
    
    
}
