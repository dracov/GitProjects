/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gemalto.split.masivos.services;

import com.gemalto.split.masivos.beans.CsvFileBean;
import com.gemalto.split.masivos.beans.Estado;
import com.gemalto.split.masivos.beans.OutputOfCsv;
import com.gemalto.split.masivos.beans.Remesa;
import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.annotations.internal.ValueProcessorProvider;
import com.googlecode.jcsv.reader.CSVEntryParser;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.AnnotationEntryParser;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author aldmendo
 */
public class MasivoService {
    
    private static Logger logger=Logger.getLogger(MasivoService.class);
    
    private static int LIMIT=50;
    private static int EXTRA=10;
    private static String FALSE="FALSE_";
    
    
    public Map<String,List<CsvFileBean>>  groupByRemesa(List<CsvFileBean> cvsRecords){   //Primer agrupamiento por REMESA
        Map<String,List<CsvFileBean>> groupedByRemesa=new HashMap<String, List<CsvFileBean>>();
        for (CsvFileBean csvFileBean : cvsRecords) {
            if(groupedByRemesa.containsKey(csvFileBean.getRemesa())) groupedByRemesa.get(csvFileBean.getRemesa()).add(csvFileBean);
            else { List<CsvFileBean> tmpList=new ArrayList<CsvFileBean>(); 
                    tmpList.add(csvFileBean);
                    groupedByRemesa.put(csvFileBean.getRemesa(), tmpList); 
                }
        }
        return groupedByRemesa;
    }

    public Map<String,List<CsvFileBean>> groupByEstado(List<CsvFileBean> cvsRecords){  //Segundo agrupamiento por Estado
           Map<String,List<CsvFileBean>> groupedByEstado=new HashMap<String, List<CsvFileBean>>();
        for (CsvFileBean csvFileBean : cvsRecords) {
            if(groupedByEstado.containsKey(csvFileBean.getEstado())) groupedByEstado.get(csvFileBean.getEstado()).add(csvFileBean);
            else { List<CsvFileBean> tmpList=new ArrayList<CsvFileBean>(); 
                    tmpList.add(csvFileBean);
                    groupedByEstado.put(csvFileBean.getEstado(), tmpList); 
                }
        }
        return groupedByEstado; 
    }
    
    public List<CsvFileBean> sortByLocalidadCCT(List<CsvFileBean> arrayToSort){ //Ordenamiento por CCT.
        Collections.sort(arrayToSort, new CsvComparator());
        return arrayToSort;
    }
   
    /*
    This method return the quantity that the configuration file tell us.
    */
    public List<OutputOfCsv> splitCsvFile(Map<String,List<CsvFileBean>> groupedByCCT){ 
       List<OutputOfCsv> recordsToWrite=new ArrayList<OutputOfCsv>(); 
        int countCCT=0;
        String remesa= getRemesaFromMap(groupedByCCT);
        String estado= getEstadoFromMap(groupedByCCT);
        Set cctAvailables=groupedByCCT.keySet(); //ccts to check if they exist
        
       for (String cct : groupedByCCT.keySet()) {
            //Caso especial, cuando el cct es mayor que la cantidad mínima SIN agregarle el extra. Se parten en partes iguales.
                   //Si no se suman hasta tener minimo la cantidad minima + la cantidad extra
            if(groupedByCCT.get(cct).size() > LIMIT + EXTRA  ){
                int noFiles= (int) Math.ceil((double) groupedByCCT.get(cct).size() / LIMIT);
                List<CsvFileBean> records=groupedByCCT.get(cct);
                for(int i=0 ; i< noFiles; i ++){
                              OutputOfCsv tempEspecial=new OutputOfCsv(remesa, estado, i);
                                 if(i != (noFiles-1))
                                    {
                                        tempEspecial.setRecords( records.subList(i* LIMIT, (i+1)*LIMIT) );
                                        recordsToWrite.add(tempEspecial);
                                    }
                                 else 
                                 {
                                     tempEspecial.setRecords( records.subList(i* LIMIT , records.size()) );
                                     recordsToWrite.add(tempEspecial);
                                 }
                          }
                cctAvailables.remove(cct);
            }
            else{  //Si el cantidad de q este cct es menor o igual al limi+extra entonces 
                 OutputOfCsv temporal=new OutputOfCsv(remesa, estado,0); //creampos un nuevo archivo de salida
                 temporal.setRecords(groupedByCCT.get(cct)); //agregamos los registros.
                 cctAvailables.remove(cct); //lo removemos de los cctAvailables.
                 String cctFound= getCCTAvailableToAdd(groupedByCCT,cctAvailables, (LIMIT+EXTRA)-temporal.getRecords().size());
                 while( cctFound.compareTo(FALSE)!=0){
                        temporal.getRecords().addAll(groupedByCCT.get(cctFound));
                        cctAvailables.remove(cctFound);
                        cctFound= getCCTAvailableToAdd(groupedByCCT,cctAvailables, (LIMIT+EXTRA)-temporal.getRecords().size());       
                    }
                //temporal ya tiene una capacidad máxima, no existe algún cct que quepa en el arreglo. Lo agregamos al arreglo de archivos.
                 recordsToWrite.add(temporal);
            }
        }
        return recordsToWrite;
    }
    
    
    
    
    
    private String getCCTAvailableToAdd(Map<String, List<CsvFileBean>> groupedByCCT, Set<String> cctAvailables, int capacityLeft) {
            for (String cct : cctAvailables) {
                if(groupedByCCT.get(cct).size()<=capacityLeft) return cct;
            }
            return FALSE;
    }

    
    
    public static List<CsvFileBean> importFromCsvFile(File f) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
  
        Reader reader = new FileReader(f); // use buffReader instead of creating a new one
        ValueProcessorProvider provider = new ValueProcessorProvider();
        CSVEntryParser<CsvFileBean> entryParser = new AnnotationEntryParser(CsvFileBean.class, provider);
        CSVStrategy myStrategy= new CSVStrategy(',','\'', '#', true, true);
        CSVReader<CsvFileBean> csvFileReader= new CSVReaderBuilder<CsvFileBean>(reader).strategy(myStrategy).entryParser( entryParser).build();
       return csvFileReader.readAll();
    
    }
   
    
    
    public static void main(String args[]) throws Exception{
       MasivoService service=new MasivoService();
        
       List<CsvFileBean> csvRecords= importFromCsvFile(new File("C:\\Aldo\\OE2\\BBVA_SortMasivos\\javaProjectos\\1_MASIVOS_ESTADOS_1058.csv"));
       
       Map<String,List<CsvFileBean>> groupedByRemesa=service.groupByRemesa(csvRecords);
       Map<String,List<CsvFileBean>> groupedByEstadoTemp= new HashMap<String, List<CsvFileBean>>();
       List<Estado> estados=new ArrayList<Estado>();
       List<Remesa> remesas=new ArrayList<Remesa>();
       
       for (String remesa : groupedByRemesa.keySet()) {
        
              groupedByEstadoTemp= service.groupByEstado(groupedByRemesa.get(remesa));
              for (String estado: groupedByEstadoTemp.keySet()) estados.add(new Estado(estado,groupedByEstadoTemp.get(estado)));
              for (Estado estado : estados) Collections.sort( estado.getRegistros(), new CsvComparator());
              for (Estado estado : estados) { 
                  Map<String,List<CsvFileBean>> groupedByCCT= service.groupByCCT(estado.getRegistros());
                  for(String cct: groupedByCCT.keySet()){
                      System.out.println("ESTADO:"+estado.getEstado()+"CCT:"+cct+"REMESA"+remesa+"QTY:"+groupedByCCT.get(cct).size());
                      
                  }
                  
                }
              remesas.add(new Remesa(remesa, estados));
       }
              
}

    
    //This records in the parameter are grouped by Estado.
    private Map<String, List<CsvFileBean>> groupByCCT(List<CsvFileBean> registros) {
        Map<String,List<CsvFileBean>> groupedByCCT=new HashMap<String, List<CsvFileBean>>();
        
        for (CsvFileBean csvFileBean : registros) {
              if(groupedByCCT.containsKey(csvFileBean.getCct())) groupedByCCT.get(csvFileBean.getCct()).add(csvFileBean);
              else{
                  List<CsvFileBean> listaTemp=new ArrayList<CsvFileBean>();
                  listaTemp.add(csvFileBean);
                  groupedByCCT.put(csvFileBean.getCct(), listaTemp);
              }
        }
        
        return groupedByCCT;
    }

    private String getRemesaFromMap(Map<String, List<CsvFileBean>> groupedByCCT) {
         for (String key : groupedByCCT.keySet()) {
            if(groupedByCCT.get(key).size()!=0) return groupedByCCT.get(key).get(0).getRemesa();
            else { logger.info("Error al obtener la información de REMESA CCT: "+key);  }        
        }
         return "";
    }

    private String getEstadoFromMap(Map<String, List<CsvFileBean>> groupedByCCT) {
        for (String key : groupedByCCT.keySet()) {
            if(groupedByCCT.get(key).size()!=0) return groupedByCCT.get(key).get(0).getEstado();
            else { logger.info("Error al obtener la información de ESTADO CCT: "+key);  }        
        }
         return "";
    }

    
    
     
    public static class CsvComparator implements Comparator<CsvFileBean>{
       @Override
        public int compare(CsvFileBean o1, CsvFileBean o2) {
            return (o1.getLocalidad()+o1.getCct()).compareTo(o2.getLocalidad()+o2.getCct());
        }
    }
    
    
    }
    
    

