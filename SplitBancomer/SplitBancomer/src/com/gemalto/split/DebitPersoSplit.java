/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gemalto.split;

import com.gemalto.split.beans.HeaderKey;
import com.gemalto.split.beans.Outputfile;
import com.gemalto.split.beans.Record;
import com.gemalto.split.conf.Config;
import com.gemalto.split.conf.ConfigDebitPerso;
import com.gemalto.split.util.Util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.log4j.Logger;
import sun.misc.Cleaner;
import sun.nio.ch.DirectBuffer;

/**
 *
 * @author Aldo
 */
public class DebitPersoSplit  extends AbstractSplit{
    
    private Config baseConfiguration;
    private ConfigDebitPerso splitConfiguration;
    
    private List<File> filesToSplit;
    private File fileTemp; 
    private static Logger logger=Logger.getLogger(DebitPersoSplit.class);
    
    public DebitPersoSplit(Config baseConfig, ConfigDebitPerso configDebitPerso ) throws IOException{
        logger.info("Building Debit Perso Split");
        this.baseConfiguration=baseConfig;
        this.splitConfiguration=configDebitPerso;
        filesToSplit=(List<File>) FileUtils.listFiles(new File(baseConfig.getDirectory().getInputFolder()), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        init();
    }
      
    private void init(){
        //Incia las posiciones donde se ecuentra la sucursal
        Record.setKeyPosition(splitConfiguration.getGroupByStart_1(), splitConfiguration.getGroupByEnd_1());
        HeaderKey.setQtyPosition(splitConfiguration.getHeaderKeyQtyStart(), splitConfiguration.getHeaderKeyQtyEnd());
     }
    
    @Override
    public void doSplits(){
        String extraStringInHeader; //este es el extraString que se le tienen qu eponer a todos los headers
        for(File file: filesToSplit){
            fileTemp=file;
            logger.info("Name of the file to Split:"+fileTemp.getName());
            logger.info("Getting first line of the file, so we can get the first header that is different.");
            extraStringInHeader=readFirstLine(file).substring(0, 12);
            HeaderKey.setExtraPart(extraStringInHeader);  
            logger.info("ExtraString in the header:"+extraStringInHeader);
            Map<HeaderKey, List<Record>> remesas=groupByRemesa();
            Map<HeaderKey,Map<String,List<Record>>> mapaPrincipal= groupByRemesaSucursal(remesas);
            List<Outputfile> archivosAEscribir=getOutputFiles(mapaPrincipal);
            logger.info("Archivos a escribir:"+archivosAEscribir.size());
            int filecounter=0;
            for(Outputfile outputfile : archivosAEscribir) {
                String filetw=Util.getFileNameWithPostInformation(outputfile.getFileName(), "_"+filecounter);
                outputfile.getHeaderKey().setQuantity(outputfile.getRecords().size());
                writeToAFile(filetw, outputfile);
                filecounter++;
            }   
        }
    }
    
    
    public List<Outputfile> getOutputFiles(Map<HeaderKey,Map<String,List<Record>>> mapaPrincipal ){
        List<Outputfile> files=new ArrayList<Outputfile>();
       
        
        int contadorDeSucursalesEnUnaRemesa=0;
         logger.info("Numero de remesas:"+mapaPrincipal.keySet().size());
        for(HeaderKey key: mapaPrincipal.keySet()){
              Outputfile temporal=new Outputfile(key);
               Map<String,List<Record>> sucursales= mapaPrincipal.get(key);
               logger.info("Numero de sucursales:"+sucursales.keySet().size());
               for(String sucursal: sucursales.keySet()){
                  
                   //Caso especial, cuando la sucursal es mayor que la cantidad mínima SIN agregarle el extra. Se parten en partes iguales.
                   //Si no se suman hasta tener minimo la cantidad minima + la cantidad extra
                   if(sucursales.get(sucursal).size()> splitConfiguration.getRecordLimit() + splitConfiguration.getExtraRecords()){
                       System.out.println("Caso Especial");
                     int noFiles= (int) Math.ceil((double) sucursales.get(sucursal).size() / splitConfiguration.getRecordLimit());
                     Outputfile tempEspecial=new Outputfile(key);
                     
                     
                          List<Record> records=sucursales.get(sucursal);
                          for(int i=0 ; i< noFiles; i ++){
                                 tempEspecial=new Outputfile(key);
                                 
                                 String newFileName=Util.getFileNameWithPostInformation(fileTemp.getName()," _"+key.getHeaderKey()+"_"+sucursal+"_"+i);
                                 tempEspecial.setFileName(baseConfiguration.getDirectory().getOutputFolder() +newFileName);
                                 if(i != (noFiles-1))
                                    {
                                        tempEspecial.setRecords( records.subList(i* splitConfiguration.getRecordLimit(), (i+1)*splitConfiguration.getRecordLimit()) );
                                        
                                        files.add(tempEspecial);
                                    }
                                 else 
                                 {

                                     tempEspecial.setRecords( records.subList(i* splitConfiguration.getRecordLimit() , records.size()) );
                                     
                                     files.add(tempEspecial);
                                 }
                          } 
                   }
                   //Caso normal. Cuando es menor y se puede llegar hasta el limite.
                   else{
                       int sobrante=(splitConfiguration.getRecordLimit() + splitConfiguration.getExtraRecords())-temporal.getRecords().size(); 
                        if(sobrante >= sucursales.get(sucursal).size() ){
                            System.out.println("Sobrante es mayor a loq eu hay en sucursales");
                            temporal.getRecords().addAll(sucursales.get(sucursal));
                        }
                        else{
                            String newFileName= Util.getFileNameWithPostInformation(fileTemp.getName(), "_"+key.getHeaderKey());
                            //temporal.setFileName(baseConfiguration.getDirectory().getOutputFolder() +fileTemp.getName()+"_"+key.getHeaderKey());
                            temporal.setFileName(baseConfiguration.getDirectory().getOutputFolder() +newFileName);
                            
                            files.add(temporal);
                            temporal=new Outputfile(key);
                            temporal.getRecords().addAll(sucursales.get(sucursal));
     
                        }
                   }
                   logger.info(" Cantidad:"+sucursales.get(sucursal).size());
                   contadorDeSucursalesEnUnaRemesa++;
                   logger.info("Contador de sucursales :"+contadorDeSucursalesEnUnaRemesa+ "de "+sucursales.keySet().size() );
                   if(contadorDeSucursalesEnUnaRemesa == sucursales.keySet().size())  
                   {    
                       String newFileName= Util.getFileNameWithPostInformation(fileTemp.getName(), "_"+key.getHeaderKey());
                       temporal.setFileName(baseConfiguration.getDirectory().getOutputFolder() +newFileName);
                       temporal.getHeaderKey().setQuantity(temporal.getRecords().size());
                       files.add(temporal);  
                       contadorDeSucursalesEnUnaRemesa=0;
                   } //si es la ultima sucursal.
               }
            }
        
        return files;
    }
    
    //Método que agrupara por cada remesa un registro de sucursales...
    public Map<HeaderKey, Map<String, List<Record>>> groupByRemesaSucursal(Map<HeaderKey,List<Record>> fileGroupByRemesas){
        Map<HeaderKey, Map<String,List<Record>>> mapaPrincipal=new HashMap<HeaderKey, Map<String, List<Record>>>();
        for(HeaderKey key: fileGroupByRemesas.keySet()){
                 List<Record> registrosPorRemesa= fileGroupByRemesas.get(key);
                 Map<String,List<Record>> mapaSucursalRegistros=new HashMap<String, List<Record>>();
                 mapaPrincipal.put(key, mapaSucursalRegistros);
                 for (Record record : registrosPorRemesa) {
                 String sucursal=record.getKeyGroup_one();
                    if(mapaSucursalRegistros.containsKey(sucursal)){
                        mapaSucursalRegistros.get(sucursal).add(record);  //agregamos el record a la lista con la sucursal chida.
                    }
                    else{
                        ArrayList<Record> listaSucursal=new ArrayList<Record>();
                        listaSucursal.add(record);
                        mapaSucursalRegistros.put(sucursal, listaSucursal);
                    }   
                     
                }
        }
        
        return mapaPrincipal;
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
             MappedByteBuffer buffer;
           int pos=0;
           long totalFileSize= fileTemp.getAbsoluteFile().length();
           System.out.println("Total file size:"+totalFileSize);
           int sizeBlock=59786000; 
           boolean  isNotCompleteRead=true;
           boolean isBufferEnough=true;
            //MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
            //El primer registro tiene cabecera.
           int contadorRemesas=0;
           buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, pos, sizeBlock);
          do{ 
            do{
                
                buffer.load();
                buffer.mark();
                byte header[]=new byte[4];  //obtenemos los 4 primeros bytes , chars
                buffer.get(header, 0, 4);   
                String firstFourChars=new String(header);
                
                if(!firstFourChars.equalsIgnoreCase("1020")){ //ES HEADER 
                    System.out.println("Its header");
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
                        System.out.println("Se le quitan los caracteres extra");
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
                    if( buffer.remaining()>=splitConfiguration.getRecordSize()){
                        byte registro[]=new byte[splitConfiguration.getRecordSize()]; //este es el record size
                       // System.out.println("Remaining:"+buffer.remaining());
                        buffer.get(registro, 0, splitConfiguration.getRecordSize());
                        recordTemp=new Record(registro);
                        mapRemesaRecords.get(headerkey).add(recordTemp); //Se agrega a el registro
                        
                        if(buffer.remaining()<splitConfiguration.getRecordSize()){
                            pos=pos + sizeBlock - buffer.remaining(); //pos donde estamos+ el tamaño del bloque que trabajamos - lo que sobro y no se pudo procesar.
                            isBufferEnough=false;
                            System.out.println("*POS:"+pos);
                        }
                        
                    }
                    
                }
            }while(buffer.hasRemaining() && isBufferEnough);
            
            System.out.println("Termianndo de ejecutar :"+sizeBlock +" Bytes");
            buffer.clear();
            clean(buffer);
            
            System.out.println("Pos now:"+pos);
            
             if( (totalFileSize-pos)>= sizeBlock  ) {//si queda espacio suficiente para cargar datos entonces cargamos otro bloque de 10milregistros
                 
                 System.out.println("Enough Space:"+"TOTAL FILE SIZE:"+totalFileSize+"; TOTAL FILE SIZE - POS:"+ (totalFileSize-pos));
                  buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, pos, sizeBlock);
                  isBufferEnough=true;
             }
             else if(totalFileSize>pos){
                    System.out.println("NOt enough space but remaining");
                    buffer= inChannel.map(FileChannel.MapMode.READ_ONLY, pos, totalFileSize-pos);              
                    sizeBlock=(int)totalFileSize-pos;
                    
                    isBufferEnough=true;
                }
                else if(totalFileSize==pos) { System.out.println("IS COMPLETED");isNotCompleteRead=false;}
          }while(isNotCompleteRead);
            buffer.clear();
            clean(buffer);
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

   
   public static void clean(ByteBuffer bb) {
    if(bb == null) return;
    Cleaner cleaner = ((DirectBuffer) bb).cleaner();
    if (cleaner != null) cleaner.clean();
}
   
    @Override
    public void doAction() {
        doSplits();
    }

   
   
   
 
       
    
 }

