/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gemalto.split.conf;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *
 * @author aldmendo
 */
@XStreamAlias("config")
public class Config implements IConfig {
    
    @XStreamAlias("directory")
    private Directory directory;

    public Directory getDirectory() {
        return directory;
    }

    public void setDirectory(Directory directory) {
        this.directory = directory;
    }

    @Override
    public String toString() {
        return "Config{" + "directory=" + directory.toString() + '}';
    }
    
    
    
    
}
