/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/***********************************************
 * Class linked to the organizedUe table
 * 
 * @author Schloune Denis
 ***********************************************/
@Entity
@Table(name = "organized_ue")
@NamedQueries({
    @NamedQuery(name = "OrganizedUe.findAll", query = "SELECT o FROM OrganizedUe o"),
    @NamedQuery(name = "OrganizedUe.findByOrganizedUeId", query = "SELECT o FROM OrganizedUe o WHERE o.organizedUeId = :organizedUeId"),
    @NamedQuery(name = "OrganizedUe.findByEndDate", query = "SELECT o FROM OrganizedUe o WHERE o.endDate = :endDate"),
    @NamedQuery(name = "OrganizedUe.findByName", query = "SELECT o FROM OrganizedUe o WHERE o.name = :name"),
    @NamedQuery(name = "OrganizedUe.findByStartDate", query = "SELECT o FROM OrganizedUe o WHERE o.startDate = :startDate")})

public class OrganizedUe extends AbstractEntity implements Serializable, Comparable<OrganizedUe> {
    //********************************************
    //members
    //********************************************
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "organized_ue_id")
    private Integer organizedUeId;
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @JoinTable(name = "teacher_organized_ue", joinColumns = {
        @JoinColumn(name = "organized_ue_id", referencedColumnName = "organized_ue_id")}, inverseJoinColumns = {
        @JoinColumn(name = "person_id", referencedColumnName = "person_id")})
    @ManyToMany
    private Collection<Person> persons;
    @OneToMany(mappedBy = "organizedUeId")
    private Collection<Indicator> indicators;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organizedUe")
    private Collection<StudentOrganizedUe> studentOrganizedUes;
    @OneToMany(mappedBy = "organizedUe")
    private Collection<Planning> plannings;
    @JoinColumn(name = "level_id", referencedColumnName = "level_id")
    @ManyToOne
    private Level level;
    @JoinColumn(name = "ue_id", referencedColumnName = "ue_id")
    @ManyToOne
    private Ue ue;

    //**********************************************
    //constructors
    //********************************************** 
    //default constructor
    public OrganizedUe() {
    }

    public OrganizedUe(Integer organizedUeId) {
        this.organizedUeId = organizedUeId;
    }
    
    //parametrable constructor
    public OrganizedUe(Integer organizedUeId, Date startDate, Date endDate, Level level, Ue ue, String name) {
        this.organizedUeId = organizedUeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.level = level;
        this.ue = ue;
        this.name = name;
    }
    
    //parametrable constructor with a startDate and a endDate parameter in String
     public OrganizedUe(Integer organizedUeId, String startDate, String endDate, Level level, Ue ue, String name) {
        this.organizedUeId = organizedUeId;
        this.startDate = null;
        this.endDate = null;
        this.level = level;
        this.ue = ue;
        this.name = name;
        
        this.setStartDate(startDate);
        this.setEndDate(endDate);
    }

     //Method from abstract class AbstractEntity
    @Override
    public Integer getId() {
        return organizedUeId;
    }
    
    //*****************************
    //getter and setter
    //*****************************
    public Integer getOrganizedUeId() {
        return organizedUeId;
    }

    public void setOrganizedUeId(Integer organizedUeId) {
        this.organizedUeId = organizedUeId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    //setEndDate from a string -> conversion
       public void setEndDate(String date){
         try { 
           SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date convertedDate = dateFormat.parse(date);
            this.endDate = convertedDate;
        } catch (ParseException ex) {
            Logger.getLogger(OrganizedUe.class.getName()).log(Level.SEVERE, null, ex);
             this.endDate = null;
        }    
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    //setStartDate from a string -> conversion
    public void setStartDate(String date){
         try { 
           SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date convertedDate = dateFormat.parse(date);
            this.startDate = convertedDate;
        } catch (ParseException ex) {
            Logger.getLogger(OrganizedUe.class.getName()).log(Level.SEVERE, null, ex);
             this.startDate = null;
        }    
    }

    public Collection<Person> getPersons() {
        return persons;
    }

    public void setPersons(Collection<Person> persons) {
        this.persons = persons;
    }

    public Collection<Indicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(Collection<Indicator> indicators) {
        this.indicators = indicators;
    }

    public Collection<StudentOrganizedUe> getStudentOrganizedUes() {
        return studentOrganizedUes;
    }

    public void setStudentOrganizedUes(Collection<StudentOrganizedUe> studentOrganizedUes) {
        this.studentOrganizedUes = studentOrganizedUes;
    }

    public Collection<Planning> getPlannings() {
        return plannings;
    }

    public void setPlannings(Collection<Planning> plannings) {
        this.plannings = plannings;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Ue getUe() {
        return ue;
    }

    public void setUe(Ue ue) {
        this.ue = ue;
    }

    //************************************************
    //methods
    //************************************************
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (organizedUeId != null ? organizedUeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrganizedUe)) {
            return false;
        }
        OrganizedUe other = (OrganizedUe) object;
        if ((this.organizedUeId == null && other.organizedUeId != null) || (this.organizedUeId != null && !this.organizedUeId.equals(other.organizedUeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
    
    //compareTo: I decided to use the id and the name to
    //discriminate an organized ue
    public int compareTo(OrganizedUe o){
        //******************************************************************************************
        //mandatory to only use id because of primeface component p:datatable on organizedUe.xhtml
        //******************************************************************************************
        //Boolean nameBool = false;
        
       //if(this.name != null && this.name.length() > 0 && o.getName() != null && o.getName().length() > 0){
            //nameBool = true;
        //}
        //if(!nameBool){
            return this.organizedUeId.compareTo(o.getOrganizedUeId());
           // return 0;
        //}
        //return this.name.toLowerCase().compareTo(o.name.toLowerCase());
    }
    
}
