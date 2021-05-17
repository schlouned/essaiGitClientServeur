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
public class SectionSearchVM {
    //*************************************
    //members
    //*************************************
    private String name;
    private String description;
    private String personLastName;
    private String personFirstName;
    private String sectionId;
    
    //*************************************
    //constructor
    //*************************************
    //default constructor
    public SectionSearchVM() {
    }
    
    //parametrable constructor
    public SectionSearchVM(String name, String personLastName, String personFirstName, String sectionId, String description) {
        this.name = name;
        this.personLastName = personLastName;
        this.personFirstName = personFirstName;
        this.sectionId = sectionId;
        this.description = description;
    }
    
    //********************************************
    //get
    //********************************************
    public String getName() {
        return name;
    }

    public String getPersonLastName() {
        return personLastName;
    }

    public String getPersonFirstName() {
        return personFirstName;
    }

    public String getSectionId() {
        return sectionId;
    }

    public String getDescription() {
        return description;
    }
    
    
    //********************************************
    //set
    //********************************************
    public void setName(String name) {
        this.name = name;
    }

    public void setPersonLastName(String personLastName) {
        this.personLastName = personLastName;
    }

    public void setPersonFirstName(String personFirstName) {
        this.personFirstName = personFirstName;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    

   
 
    
    
    
    
    
}
