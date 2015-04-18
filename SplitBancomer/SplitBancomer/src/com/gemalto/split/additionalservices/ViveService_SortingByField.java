/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gemalto.split.additionalservices;

import com.gemalto.split.beans.HeaderKey;
import com.gemalto.split.beans.Record;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author aldmendo
 */
public class ViveService_SortingByField {
   private static Logger logger=Logger.getLogger(ViveService_SortingByField.class);
   private String outputPath;
   private File inputFile;
  
   public static Map<HeaderKey,List<Record>> allRecords=new HashMap<HeaderKey, List<Record>>();

   public  void addAndSortRecords(HeaderKey toAdd,Map<String,List<Record>> mapGroupBySortfield){
       logger.info("Sorting Header(Rememsa)..."+toAdd.getHeaderKey());
       Set<String> sortingFields=mapGroupBySortfield.keySet();
       logger.info("Sorting Mensajería");
       List<String> mensajerias=new ArrayList<String>(sortingFields);
       
       Collections.sort(mensajerias,new StringComparator());
       
       
       for (String sortingField : mensajerias) { //sort by PAN
              logger.info("   Sorting Array of Field (Mensajeria):"+sortingField+" Qty of records:"+mapGroupBySortfield.get(sortingField).size());
              
              Collections.sort(mapGroupBySortfield.get(sortingField),new PanComparator());
              //allRecords.put(toAdd, mapGroupBySortfield.get(sortingField));  
             //After sorting the sets of records by PAN we add them to the AllRecrods Map
              if(allRecords.get(toAdd)==null) {
                    List<Record> tmp=new ArrayList<Record>();
                     allRecords.put(toAdd, tmp);
              }
              
              allRecords.get(toAdd).addAll( mapGroupBySortfield.get(sortingField));
             
       }
       logger.info("    Records sorted :"+allRecords.get(toAdd).size());
       
    }

 
   
   
     public void writeNewFile() {
       
        File toWrite= new File(outputPath+"\\"+inputFile.getName());
        logger.info("ArchivoToWrite:"+toWrite.getName());
        int countHeader=0;
        Set<HeaderKey> headers=allRecords.keySet();
         try {
            for (HeaderKey headerKey : headers) {
                headerKey.setQuantity(allRecords.get(headerKey).size());
               logger.info("Header:"+headerKey.getHeaderKey()+ " quantity:"+headerKey.getQuantity());
               if(countHeader==0)  FileUtils.write(toWrite, headerKey.getHeaderString() , true);
               else FileUtils.write(toWrite, headerKey.getHeaderStringWithoutExtraPart(), true);
               countHeader++;
               List<Record> recordsByHeader= allRecords.get(headerKey);
               for (Record record : recordsByHeader) {
                   FileUtils.write(toWrite, record.getRecord()+ "\r\n", true);
                }
            }
        } catch (IOException ex) {
               
               logger.error(ex.getMessage(), ex);
           }
    }
   
    
    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }
   
     
     static class PanComparator implements Comparator<Record>{
        public int compare(Record o1, Record o2) {
               return o1.getPan().compareTo(o2.getPan());
            }

    }
    
     static class StringComparator implements Comparator<String>{
      public int compare(String o1, String o2) {
               return o1.compareTo(o2);
            }
     }
    
    
}
