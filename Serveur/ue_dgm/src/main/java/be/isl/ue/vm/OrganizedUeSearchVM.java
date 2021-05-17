/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.vm;

import be.isl.ue.entity.Level;
import be.isl.ue.entity.OrganizedUe;
import be.isl.ue.entity.Person;
import be.isl.ue.entity.Section;

/**
 * **************************************
 * Parameters use to search an organized ue every parameter is a String
 *
 * @author Schloune Denis
 **************************************
 */
public class OrganizedUeSearchVM {

    //*************************************
    //members
    //*************************************
    private String name;
    private String startDateBegin;
    private String startDateEnd;
    private String academicYear;
    private String organizedUeId;
    //*****************************
    private Level level;
    private Section section;
    private Person person;
    private OrganizedUe organizedUe;
    private String academicYearCopyTo;

    //*************************************
    //constructor
    //*************************************
    //default constructor
    public OrganizedUeSearchVM() {
    }

    //********************************************
    //get
    //********************************************
    public String getName() {
        return name;
    }

    public String getStartDateBegin() {
        return startDateBegin;
    }

    public String getStartDateEnd() {
        return startDateEnd;
    }
  
    public String getOrganizedUeId() {
        return organizedUeId;
    }

    public String getAcademicYear() {
        return academicYear;
    }
   
    public Level getLevel() {
        return level;
    }

    public Section getSection() {
        return section;
    }

    public Person getPerson() {
        return person;
    }

    public OrganizedUe getOrganizedUe() {
        return organizedUe;
    }

    public String getAcademicYearCopyTo() {
        return academicYearCopyTo;
    }
    
    
    

    //********************************************
    //set
    //********************************************
    public void setName(String name) {
        this.name = name;
    }

    public void setStartDateBegin(String startDateBegin) {
        this.startDateBegin = startDateBegin;
    }

    public void setStartDateEnd(String startDateEnd) {
        this.startDateEnd = startDateEnd;
    }

    public void setOrganizedUeId(String organizedUeId) {
        this.organizedUeId = organizedUeId;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }
    
    public void setLevel(Level level) {
        this.level = level;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setOrganizedUe(OrganizedUe organizedUe) {
        this.organizedUe = organizedUe;
    }

    public void setAcademicYearCopyTo(String academicYearCopyTo) {
        this.academicYearCopyTo = academicYearCopyTo;
    }
    
}
