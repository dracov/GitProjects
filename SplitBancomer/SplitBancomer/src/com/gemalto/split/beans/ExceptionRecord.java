/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gemalto.split.beans;

/**
 *
 * @author aldmendo
 */
public class ExceptionRecord {
    public String cons;
    public String cuenta;
    
    
    
    
    public ExceptionRecord(){
        
    }
    
    public ExceptionRecord(String cons,String cuenta){
        this.cons=cons;
        this.cuenta=cuenta;
    }
    

    public String getCons() {
        return cons;
    }

    public void setCons(String cons) {
        this.cons = cons;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    @Override
    public String toString() {
        return "ExceptionRecord{" + "cons=" + cons + ", cuenta=" + cuenta + '}';
    }
    
    
    
}
