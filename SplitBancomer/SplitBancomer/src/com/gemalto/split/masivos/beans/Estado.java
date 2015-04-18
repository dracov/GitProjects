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
public class Estado {
    
    private String estado;
    private List<CsvFileBean> registros;

    
    public Estado(){
    }
    
    public Estado(String estado, List<CsvFileBean> registros){
    this.estado=estado;
    this.registros=registros;
    }
    
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<CsvFileBean> getRegistros() {
        return registros;
    }

    public void setRegistros(List<CsvFileBean> registros) {
        this.registros = registros;
    }
    
    
            
            
    
}
