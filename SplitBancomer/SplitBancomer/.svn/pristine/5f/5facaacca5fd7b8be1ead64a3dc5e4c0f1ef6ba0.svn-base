/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gemalto.split;

import com.gemalto.split.additionalservices.ViveService_RemoveRecordsFromCsvFile;
import com.gemalto.split.additionalservices.ViveService_SortingByField;
import com.gemalto.split.beans.HeaderKey;
import com.gemalto.split.beans.Record;
import com.gemalto.split.conf.Config;
import com.gemalto.split.conf.ConfigFinanziaVivePerso;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.log4j.Logger;

/**
 *
 * @author Aldo
 */
public class FinanziaVivePersoSplit  extends AbstractSplit{
    private Config baseConfiguration;
    private ConfigFinanziaVivePerso splitConfiguration;
    
    private List<File> filesToSplit;
    private File fileTemp; 
    private static Logger logger=Logger.getLogger(FinanziaVivePersoSplit.class);
    
    public FinanziaVivePersoSplit(Config baseConfig, ConfigFinanziaVivePerso configDebitPerso ) throws IOException{
        logger.info("Building Finanzia Vive Perso Split");
        this.baseConfiguration=baseConfig;
        this.splitConfiguration=configDebitPerso;
        filesToSplit=(List<File>) FileUtils.listFiles(new File(baseConfig.getDirectory().getInputFolder()), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        init();
    }
      
    private void init(){
        //Incia las posiciones donde se ecuentra la sucursal
        Record.setKeyPosition(splitConfiguration.getGroupByStart_1(), splitConfiguration.getGroupByEnd_1());
        Record.setField1Position(splitConfiguration.getField_1Start(), splitConfiguration.getField_1End());
        Record.setSortingPosition(splitConfiguration.getGroupByAndSortStart_1(), splitConfiguration.getGroupByAndSortEnd_1());
        HeaderKey.setQtyPosition(splitConfiguration.getHeaderKeyQtyStart(), splitConfiguration.getHeaderKeyQtyEnd());
     }
    
    @Override
    public void doSplits(){
       //to be defined
    }
    
    
    
      
    
    public String readFirstLine(File file){
        FileReader fr;
        String linea=null;
        try {
            fr = new FileReader(file);
            LineNumberReader ln=new LineNumberReader(fr);
            linea=ln.readLine();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        
        return linea;
        
    }
      
   
   public Map<HeaderKey,List<Record>> groupByRemesa(){
       RandomAccessFile aFile;
       HeaderKey headerkey=new HeaderKey(); //KEY  de las remesas
       List<Record> recordListByHeader=null; //Lista que tendra los N registros de el código correspondiente.
       Record recordTemp; //Para almacenar un registro
       Map<HeaderKey,List<Record>> mapRemesaRecords=new HashMap<HeaderKey, List<Record>>();
       try {
            aFile = new RandomAccessFile(fileTemp, "r");
            FileChannel inChannel = aFile.getChannel();
            MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
            buffer.load();
               //El primer registro tiene cabecera.
           int contadorRemesas=0;
            do{
                buffer.mark();
                byte header[]=new byte[4];  //obtenemos los 4 primeros bytes , chars
                buffer.get(header, 0, 4);   
                String firstFourChars=new String(header);
                
                if(!firstFourChars.equalsIgnoreCase("1020")){ //ES HEADER 
                    recordListByHeader=new ArrayList<Record>(); //creamos un nuevo registro para este nuevo header
                    buffer.reset(); //nos regresamos para leer la cabecera desde el inicio.
                    headerkey=new HeaderKey(); //se crea una nueva cabecera
                    StringBuilder cabecera=new StringBuilder();
                    char temp;
                    while( (temp=(char) buffer.get()) !='\n'){
                        cabecera.append(temp);
                    }
                    cabecera.append('\n');
                    if(contadorRemesas==0) 
                    {
                       
                        String headertemp=cabecera.toString().substring(12, cabecera.length());
                        headerkey.setHeaderString(cabecera.toString().substring(12, cabecera.length()));
                        headerkey.setHeaderKey(headertemp.substring(splitConfiguration.getHeaderKeyStart(), splitConfiguration.getHeaderKeyEnd()).trim());
                    }
                    else{
                     headerkey.setHeaderString(cabecera.toString());
                     headerkey.setHeaderKey(cabecera.substring(splitConfiguration.getHeaderKeyStart(), splitConfiguration.getHeaderKeyEnd()).trim());// posicion donde se encuentra la remesa.
                    }
                    contadorRemesas++;
                    
                    logger.info("HEADER FOUND, STRING KEY:"+splitConfiguration.getHeaderKeyName() +":" +  headerkey.getHeaderKey());
                    mapRemesaRecords.put(headerkey, recordListByHeader);
                }
                else {
                    
                    buffer.reset(); //regresamos a donde marcamos al principio.
                    byte registro[]=new byte[splitConfiguration.getRecordSize()]; //este es el record size
                    buffer.get(registro, 0, splitConfiguration.getRecordSize());
                    recordTemp=new Record(registro);
                    mapRemesaRecords.get(headerkey).add(recordTemp); //Se agrega a el registro
                }
            }while(buffer.hasRemaining());

            buffer.clear();
            inChannel.close();
            aFile.close();
          
        }catch(FileNotFoundException fnfe){
         
            logger.error(fnfe.getMessage());
            JOptionPane.showMessageDialog(null, "No se encuentra el archivo en la carpeta:", "Error!", JOptionPane.ERROR_MESSAGE);
            
        }catch(IOException ioe){
           
            logger.error(ioe.getMessage(),ioe);
            JOptionPane.showMessageDialog(null, "Error:"+ioe.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
        }
       return mapRemesaRecords;
       
   }
   
   
public  Map<String,List<Record>>  groupBySortingField(HeaderKey headerKey,Map<HeaderKey,List<Record>> allRecords ){
      
      Map<String,List<Record>> groupBySortingField=new HashMap<String, List<Record>>();
  
            List<Record> recordsByRemesa= allRecords.get(headerKey);
            for (Record record : recordsByRemesa) {
                String sucursal=record.getSortField();
                if(groupBySortingField.get(sucursal)==null) {
                    List<Record> tmp=new ArrayList<Record>();
                     groupBySortingField.put(sucursal, tmp);
                    }
                groupBySortingField.get(sucursal).add(record);
           }

        return groupBySortingField;
       }
   
   

    @Override
    public void doAction() {
        if(splitConfiguration.getSplitAction()==1){
            doSplits();
        }
        
       if(splitConfiguration.getSortingAction()==1){
           doSorting();
       }
        
        if(splitConfiguration.getExtraAction()==1){
            doExtraAction();
        }
        
    }

   
   public void doExtraAction(){ //Remove records from exception FIle.
           ViveService_RemoveRecordsFromCsvFile.loadExceptionRecords(splitConfiguration.getExceptionFile()); //Store the exceptionfile
           ViveService_RemoveRecordsFromCsvFile service= new ViveService_RemoveRecordsFromCsvFile();
           String extraStringInHeader; //este es el extraString que se le tienen qu eponer a todos los headers
           for(File file: filesToSplit){
            fileTemp=file;
            logger.info("Name of the file to do ExtraAction:"+fileTemp.getName());
            extraStringInHeader=readFirstLine(file).substring(0, 12);
            HeaderKey.setExtraPart(extraStringInHeader);  
            logger.info("ExtraString in the header:"+extraStringInHeader);
            
            service.setRecordsMap(groupByRemesa());
            service.setInputFile(file);
            service.setOutputPath(splitConfiguration.getOutputFilePathWithoutExceptions());
            service.removeExceptionRecords();
            service.writeNewFileAndExceptionFile();
           }
           
   }

    public void doSorting() { 
        
        ViveService_SortingByField sortingService=new ViveService_SortingByField();
          String extraStringInHeader; 
        for(File file: filesToSplit)
        {
            fileTemp=file;
            logger.info("File to threat:"+fileTemp);
            logger.info("Getting first line of the file, so we can get the first header that is different.");
            extraStringInHeader=readFirstLine(file).substring(0, 12);
            HeaderKey.setExtraPart(extraStringInHeader);
            Map<HeaderKey,List<Record>> allRecords=groupByRemesa();
            Set<HeaderKey> headers= allRecords.keySet();
            for (HeaderKey headerKey : headers) {
                logger.info("Working with remesa:"+headerKey.getHeaderKey());
                sortingService.addAndSortRecords(headerKey, groupBySortingField(headerKey,allRecords));
                
            }
            sortingService.setInputFile(file);
            sortingService.setOutputPath(baseConfiguration.getDirectory().getOutputFolder());
            sortingService.writeNewFile();
        }
       
       
    }
   
    
 }

