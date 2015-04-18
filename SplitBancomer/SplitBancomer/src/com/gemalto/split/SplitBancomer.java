/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gemalto.split;

import com.gemalto.split.conf.Config;
import com.gemalto.split.conf.ConfigDebitPerso;
import com.gemalto.split.conf.ConfigFinanziaVivePerso;
import com.gemalto.split.conf.IConfig;

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 *
 * @author aldmendo
 */
public class SplitBancomer {

    /**
     * @param args the command line arguments
     */
    
    private static Logger logger=Logger.getLogger(SplitBancomer.class);
    private static boolean  isDevelopement;
    
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        String enviroment= System.getProperty("env", "test");
        if(enviroment.compareToIgnoreCase("test")==0) {
            isDevelopement=true; 
            logger.info("Ambiente Desarrollo");
        }
        else isDevelopement=false;

        
        if(args.length==0){
            JOptionPane.showMessageDialog(null, "El programa no ha iniciado correctamente, ingresar argumento.", "Argumento necesario.", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        logger.info("Configuration to load:"+args[0]);
        
        //1.- Get Base Configuration 
        IConfig baseConfig= getConfiguration("Config");
        System.out.println("Config:"+baseConfig.toString());
        //2.- Get SplitConfiguration via args
        IConfig splitConfig= getConfiguration(args[0]);
        AbstractSplit split;
        
        if(splitConfig instanceof ConfigDebitPerso){
             split = new DebitPersoSplit((Config)baseConfig, (ConfigDebitPerso)splitConfig );
             split.doAction();
        }else
            if(splitConfig instanceof ConfigFinanziaVivePerso){
              split= new FinanziaVivePersoSplit((Config)baseConfig, (ConfigFinanziaVivePerso)splitConfig);
              split.doAction();
            }
   
    }
    
    
    public static IConfig getConfiguration(String type){
        
        if(type.compareTo("Config")==0){
            Config configuration=null;
            logger.info("Cargando configuracion Base.");
            XStream  xstream=buildConfiguration(Config.class);
            if(isDevelopement)    configuration= (Config)xstream.fromXML(new File("src/config/config.xml"));
            else  configuration= (Config)xstream.fromXML(new File("config/config.xml"));
            
            return configuration;  
        }
        
        if(type.compareTo("DebitPerso")==0){ 
            ConfigDebitPerso configuration=null;
            logger.info("Cargando configuración de Debito Perso");
            XStream  xstream=buildConfiguration(ConfigDebitPerso.class);
            if(isDevelopement) configuration= (ConfigDebitPerso)xstream.fromXML(new File("src/config/config-debitPerso.xml"));
            else configuration= (ConfigDebitPerso)xstream.fromXML(new File("config/config-debitPerso.xml"));
            return configuration;
        }
        if(type.compareTo("FinanziaVivePerso")==0){ 
            ConfigFinanziaVivePerso configuration=null;
            logger.info("Cargando configuración de Finanzia Perso");
            XStream  xstream=buildConfiguration(ConfigFinanziaVivePerso.class);
            if(isDevelopement) configuration= (ConfigFinanziaVivePerso)xstream.fromXML(new File("src/config/config-finanziaVivePerso.xml"));
            else configuration= (ConfigFinanziaVivePerso)xstream.fromXML(new File("config/config-finanziaVivePerso.xml"));
            return configuration;
        }
        return null;    
    }
    
    
    public static XStream buildConfiguration(Class type){
        XStream xstream = new XStream();
        xstream.autodetectAnnotations(true);
        xstream.processAnnotations(type);
        return xstream;
    }

    
    
}
