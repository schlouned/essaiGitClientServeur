/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.vm;

/**
 *
 * @author Schloune Denis
 */
public class LevelSearchVM {

    //*************************************
    //members
    //*************************************
    private String organizedUeId;
    private String organizedUeName;

    //*************************************
    //constructor
    //*************************************
    public LevelSearchVM(String organizedUeId, String organizedUeName) {
        this.organizedUeId = organizedUeId;
        this.organizedUeName = organizedUeName;
    }

    //default constructor
    public LevelSearchVM() {
    }

    //********************************************
    //get
    //********************************************
    public String getOrganizedUeName() {    
        return organizedUeName;
    }

    public String getOrganizedUeId() {
        return organizedUeId;
    }

    //********************************************
    //set
    //********************************************
    public void setOrganizedUeName(String OrganizedUeName) {
        this.organizedUeName = OrganizedUeName;
    }

    public void setOrganizedUeId(String organizedUeId) {
        this.organizedUeId = organizedUeId;
    }


}
