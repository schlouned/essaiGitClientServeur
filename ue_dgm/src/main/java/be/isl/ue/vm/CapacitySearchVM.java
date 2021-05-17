/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.vm;

/****************************************
 * Parameters use to search a capacity
 * every parameter is a String
 * 
 * @author Schloune Denis
 ***************************************/
public class CapacitySearchVM {
    //*************************************
    //members
    //*************************************
    private String name;
    private String ueName;
    
    //*************************************
    //constructor
    //*************************************
    //default constructor
    public CapacitySearchVM() {
    }

    public CapacitySearchVM(String name, String sectionName) {
        this.name = name;
        this.ueName = sectionName;
    }
    
    //********************************************
    //get
    //********************************************
    public String getName() {
        return name;
    }

    public String getUeName() {
        return ueName;
    }
    
    //********************************************
    //set
    //********************************************
    public void setName(String name) {
        this.name = name;
    }

    public void setUeName(String ueName) {
        this.ueName = ueName;
    }
    
}
