/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gemalto.split.beans;

/**
 *
 * @author Aldo
 */
public class Record {
    private String keyGroup_one; // Field of the Record use to group. In this case we only use one
    private String pan;
    private String record;
    private String sortField;
    private static int initKeyPos=0;
    private static int endKeyPos=0;
    private static int initPanPos=0;
    private static int endPanPos=0;
    private static int initSortPos=0;
    private static int endSortPos=0;
    
    public Record(){
        
    }
    
    public Record(byte data[]){
       record = new String(data);
       record = record.substring(0, record.length()-2); //eliminamos el ultimo /r/n (Esto se hace debido al metodo write que toma los elemtos de una collection y los inserta en el archivo
    }
    
    
    public String getKeyGroup_one() {
        return record.substring(initKeyPos, endKeyPos);
    }

    public void setKeyGroup_one(String keyGroup_one) {
        this.keyGroup_one = keyGroup_one;
    }

    public String getPan() {
        if(record!=null) return record.substring(initPanPos,endPanPos);
        else return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getSortField() {
       if(record!=null) return record.substring(initSortPos,endSortPos);
        else return pan;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }
    
    

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    
    public static void setKeyPosition(int init, int end){
        initKeyPos=init;
        endKeyPos=end;
    }
    
    public static void setField1Position(int init,int end){
        initPanPos=init;
        endPanPos=end;
    }
    
    public static void setSortingPosition(int init,int end){
        initSortPos=init;
        endSortPos=end;
    }

    public String toStringPan(){
    return pan;
    }
    
    @Override
    public String toString() {
        return record;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.pan != null ? this.pan.hashCode() : 0);
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
        final Record other = (Record) obj;
        if ((this.getPan() == null) ? (other.getPan() != null) : !this.getPan().equals(other.getPan())) {
            return false;
        }
        return true;
    }
  
  
}
