/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gemalto.split.beans;

import java.util.List;

/**
 *
 * @author Aldo
 */
public class SucursalRegistros {
    
    private String sucursal;
    private List<Record> records;

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.sucursal != null ? this.sucursal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SucursalRegistros other = (SucursalRegistros) obj;
        if ((this.sucursal == null) ? (other.sucursal != null) : !this.sucursal.equals(other.sucursal)) {
            return false;
        }
        return true;
    }
    
    
    
    
}
