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

@XStreamAlias("FinanziaVivePerso")
public class ConfigFinanziaVivePerso implements IConfig {
    
    @XStreamAlias("recordSize")
    private int recordSize;
    
    @XStreamAlias("headerKeyName")
    private String headerKeyName;
    
    @XStreamAlias("headerKeyStart")
    private int headerKeyStart;
    
    @XStreamAlias("headerKeyEnd")
    private int headerKeyEnd;
    
    @XStreamAlias("groupByStart_1Name")
    private String groupByStart_1Name;
    
    @XStreamAlias("groupByStart_1")
    private int groupByStart_1;
    
    @XStreamAlias("groupByEnd_1")
    private int groupByEnd_1;

    @XStreamAlias("recordLimit")
    private int recordLimit;
    
    @XStreamAlias("extraRecords")
    private int extraRecords;
    
    @XStreamAlias("headerKeyQtyStart")
    private int headerKeyQtyStart;
    
    @XStreamAlias("headerKeyQtyEnd")
    private int headerKeyQtyEnd;
    
    @XStreamAlias("splitAction")
    private int splitAction;

    @XStreamAlias("extraAction")
    private int extraAction;

    @XStreamAlias("exceptionFile")
    private String exceptionFile;
    
    @XStreamAlias("field_1Name")
    private String field_1Name;
    
    @XStreamAlias("field_1Start")
    private int field_1Start;
    
    @XStreamAlias("field_1End")
    private int field_1End;
       
    @XStreamAlias("outputFilePathWithoutExceptions")
    private String outputFilePathWithoutExceptions;

    @XStreamAlias("sortingAction")
    private int sortingAction;
            
    @XStreamAlias("groupByAndSortStart_1Name")
    private String groupByAndSortStart_1Name;
    
    @XStreamAlias("groupByAndSortStart_1")
    private int groupByAndSortStart_1;
    
    @XStreamAlias("groupByAndSortEnd_1")
    private int groupByAndSortEnd_1;

    public int getSortingAction() {
        return sortingAction;
    }

    public void setSortingAction(int sortingAction) {
        this.sortingAction = sortingAction;
    }

    
    
   
    public String getGroupByAndSortStart_1Name() {
        return groupByAndSortStart_1Name;
    }

    public void setGroupByAndSortStart_1Name(String groupByAndSortStart_1Name) {
        this.groupByAndSortStart_1Name = groupByAndSortStart_1Name;
    }

    public int getGroupByAndSortStart_1() {
        return groupByAndSortStart_1;
    }

    public void setGroupByAndSortStart_1(int groupByAndSortStart_1) {
        this.groupByAndSortStart_1 = groupByAndSortStart_1;
    }

    public int getGroupByAndSortEnd_1() {
        return groupByAndSortEnd_1;
    }

    public void setGroupByAndSortEnd_1(int groupByAndSortEnd_1) {
        this.groupByAndSortEnd_1 = groupByAndSortEnd_1;
    }
    
    
    public String getOutputFilePathWithoutExceptions() {
        return outputFilePathWithoutExceptions;
    }

    public void setOutputFilePathWithoutExceptions(String outputFilePathWithoutExceptions) {
        this.outputFilePathWithoutExceptions = outputFilePathWithoutExceptions;
    }
    
    public String getField_1Name() {
        return field_1Name;
    }

    public void setField_1Name(String field_1Name) {
        this.field_1Name = field_1Name;
    }

    public int getField_1Start() {
        return field_1Start;
    }

    public void setField_1Start(int field_1Start) {
        this.field_1Start = field_1Start;
    }

    public int getField_1End() {
        return field_1End;
    }

    public void setField_1End(int field_1End) {
        this.field_1End = field_1End;
    }
    

    public int getSplitAction() {
        return splitAction;
    }

    public void setSplitAction(int splitAction) {
        this.splitAction = splitAction;
    }

    public String getExceptionFile() {
        return exceptionFile;
    }

    public void setExceptionFile(String exceptionFile) {
        this.exceptionFile = exceptionFile;
    }

    
    public int getExtraAction() {
        return extraAction;
    }

    public void setExtraAction(int extraAction) {
        this.extraAction = extraAction;
    }
    

    public int getRecordSize() {
        return recordSize;
    }

    public void setRecordSize(int recordSize) {
        this.recordSize = recordSize;
    }

    public String getHeaderKeyName() {
        return headerKeyName;
    }

    public void setHeaderKeyName(String headerKeyName) {
        this.headerKeyName = headerKeyName;
    }

    public int getHeaderKeyStart() {
        return headerKeyStart;
    }

    public void setHeaderKeyStart(int headerKeyStart) {
        this.headerKeyStart = headerKeyStart;
    }

    public int getHeaderKeyEnd() {
        return headerKeyEnd;
    }

    public void setHeaderKeyEnd(int headerKeyEnd) {
        this.headerKeyEnd = headerKeyEnd;
    }

    public String getGroupByStart_1Name() {
        return groupByStart_1Name;
    }

    public void setGroupByStart_1Name(String groupByStart_1Name) {
        this.groupByStart_1Name = groupByStart_1Name;
    }

    public int getGroupByStart_1() {
        return groupByStart_1;
    }

    public void setGroupByStart_1(int groupByStart_1) {
        this.groupByStart_1 = groupByStart_1;
    }

    public int getGroupByEnd_1() {
        return groupByEnd_1;
    }

    public void setGroupByEnd_1(int groupByEnd_1) {
        this.groupByEnd_1 = groupByEnd_1;
    }

    public int getRecordLimit() {
        return recordLimit;
    }

    public void setRecordLimit(int recordLimit) {
        this.recordLimit = recordLimit;
    }

    public int getExtraRecords() {
        return extraRecords;
    }

    public void setExtraRecords(int extraRecords) {
        this.extraRecords = extraRecords;
    }

    public int getHeaderKeyQtyEnd() {
        return headerKeyQtyEnd;
    }

    public void setHeaderKeyQtyEnd(int headerKeyQtyEnd) {
        this.headerKeyQtyEnd = headerKeyQtyEnd;
    }

    public int getHeaderKeyQtyStart() {
        return headerKeyQtyStart;
    }

    public void setHeaderKeyQtyStart(int headerKeyQtyStart) {
        this.headerKeyQtyStart = headerKeyQtyStart;
    }

    

    
    
}
