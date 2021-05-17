package be.isl.ue.vm;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/****************************************
 * Parameters use to search an ue
 * every parameter is a String
 * 
 * @author Schloune Denis
 ***************************************/
public class UeSearchVM {
    //*************************************
    //members
    //*************************************
    private String name;
    private String description;
    private String sectionName;
    private String capacityName;
    private String code;
    
    //*************************************
    //constructor
    //*************************************
    //default constructor
    public UeSearchVM() {
    }
    
    //parametrable constructor
    public UeSearchVM(String name, String description, String sectionName, String capacityName, String code) {
        this.name = name;
        this.description = description;
        this.sectionName = sectionName;
        this.capacityName = capacityName;
        this.code = code;
    }
    
    //********************************************
    //get
    //********************************************
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getCapacityName() {
        return capacityName;
    }

    public String getCode() {
        return code;
    }

    //********************************************
    //set
    //********************************************
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public void setCapacityName(String capacityname) {
        this.capacityName = capacityname;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    
    
}
