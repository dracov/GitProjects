/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gemalto.split.beans;

/**
 *
 * @author Aldo
 */
public class HeaderKey {
    
    private String headerString;//all the string that is known as header
    private String headerKey; // Key value in the header String
    private int indice; // atributo solo para saber si es seriado el nombre del archivo.
    private int quantity;  //cantidad que tiene este header, en este caso la cantidad de los registros por cabecera 
    private static int qtyPosStart;
    private static int qtyPosEnd;
    private static String extraPart; //para agregar los 12 primeros caracteres que trae la primer cabecera

    @Override
    public int hashCode() {
        int hash = 7;
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
        final HeaderKey other = (HeaderKey) obj;
        if ((this.headerKey == null) ? (other.headerKey != null) : !this.headerKey.equals(other.headerKey)) {
            return false;
        }
        return true;
    }

    
    public String getHeaderString() {
        String headerFirstPart=headerString.substring(0, qtyPosStart-1);
        String headerSecondPart=headerString.substring(qtyPosEnd-1, headerString.length());
        return extraPart+headerFirstPart+String.format("%06d", quantity)+headerSecondPart;
    }
    
    public String getHeaderStringWithoutExtraPart() {
        String headerFirstPart=headerString.substring(0, qtyPosStart-1);
        String headerSecondPart=headerString.substring(qtyPosEnd-1, headerString.length());
        return headerFirstPart+String.format("%06d", quantity)+headerSecondPart;
    }

    public void setHeaderString(String headerString) {
        this.headerString = headerString;
    }

    public String getHeaderKey() {
        return headerKey;
    }

    public void setHeaderKey(String headerKey) {
        this.headerKey = headerKey;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static String getExtraPart() {
        return extraPart;
    }

    public static void setExtraPart(String extraPart) {
        HeaderKey.extraPart = extraPart;
    }

    public static void setQtyPosition(int qtyStart,int qtyEnd){
        qtyPosStart=qtyStart;
        qtyPosEnd=qtyEnd;
    }
    
   
    
    
    
}
