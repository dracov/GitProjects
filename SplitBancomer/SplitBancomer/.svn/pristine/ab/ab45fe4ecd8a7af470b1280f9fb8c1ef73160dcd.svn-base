/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gemalto.split.masivos.beans;

import java.util.List;

/**
 *
 * @author aldmendo
 */
public class OutputOfCsv {
    private String remesa;
    
    private int count_total; //total number of files (where there is a CCT>LIMIT)
    private String estado;
    private List<CsvFileBean> records;

    
    public OutputOfCsv(String remesa, String estado,int count_total ){
    this.remesa=remesa;
    this.estado=estado;
    this.count_total=count_total;
    
    }

    public int getCount_total() {
        return count_total;
    }

    public void setCount_total(int count_total) {
        this.count_total = count_total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    
    public String getRemesa() {
        return remesa;
    }

    public void setRemesa(String remesa) {
        this.remesa = remesa;
    }

    
    public List<CsvFileBean> getRecords() {
        return records;
    }

    public void setRecords(List<CsvFileBean> records) {
        this.records = records;
    }
    
    
    
    
}
