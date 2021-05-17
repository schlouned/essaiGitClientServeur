/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.vm;


/****************************************
 * Parameters use to search a section
 * every parameter is a String
 * 
 * @author Schloune Denis
 ***************************************/
public class FalseSearchVM {
    //*************************************
    //members
    //*************************************
    private String name;
    
    //*************************************
    //constructor
    //*************************************
    //default constructor
    public FalseSearchVM() {
    }
    
    //parametrable constructor
    public FalseSearchVM(String name) {
        this.name = name; 
    }
    
    //********************************************
    //get
    //********************************************
    public String getName() {
        return name;
    }
       
    //********************************************
    //set
    //********************************************
    public void setName(String name) {
        this.name = name;
    }


    

   
 
    
    
    
    
    
}
