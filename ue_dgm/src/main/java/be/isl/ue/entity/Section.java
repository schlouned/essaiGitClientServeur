/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/***********************************************
 * Class linked to the section table
 * 
 * @author Schloune Denis
 ***********************************************/
@Entity
@Table(name = "section")
@NamedQueries({
    @NamedQuery(name = "Section.findAll", query = "SELECT s FROM Section s"),
    @NamedQuery(name = "Section.findBySectionId", query = "SELECT s FROM Section s WHERE s.sectionId = :sectionId"),
    @NamedQuery(name = "Section.findByDescription", query = "SELECT s FROM Section s WHERE s.description = :description"),
    @NamedQuery(name = "Section.findByName", query = "SELECT s FROM Section s WHERE s.name = :name")})

public class Section extends AbstractEntity implements Serializable ,Comparable<Section> {
    //********************************************
    //members
    //********************************************
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "section_id")
    private Integer sectionId;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    @ManyToOne
    private Person person;
    @OneToMany(mappedBy = "section")
    private Collection<Ue> ues;

    //**********************************************
    //constructors
    //**********************************************   
    //default constructor
    public Section() {
    }

    public Section(Integer sectionId) {
        this.sectionId = sectionId;
    }
    
    //parametrable constructor
    public Section(Integer sectionId, String name, String description, Person person) {
        this.sectionId = sectionId;
        this.name = name;
        this.description = description;
        this.person = person;
    }

     //Method from abstract class AbstractEntity
    @Override
    public Integer getId() {
        return sectionId;
    }
    //*****************************
    //getter and setter
    //*****************************
    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    @JsonbTransient
    public Collection<Ue> getUes() {
        return ues;
    }

    public void setUes(Collection<Ue> ues) {
        this.ues = ues;
    }

    //************************************************
    //methods
    //************************************************
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sectionId != null ? sectionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Section)) {
            return false;
        }
        Section other = (Section) object;
        if ((this.sectionId == null && other.sectionId != null) || (this.sectionId != null && !this.sectionId.equals(other.sectionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
    
    //compareTo: I decided to use name and description to
    //discriminate a section
    public int compareTo(Section s){
        Boolean nameBool = false;
        
        
        if(this.name != null && this.name.length() > 0 && s.getName() != null && s.getName().length() > 0){
            nameBool = true;
         
        }
        
      
       if(!nameBool){
//            return this.sectionId.compareTo(s.getSectionId());
            return 0;
        }
       
        return this.name.toLowerCase().compareTo(s.name.toLowerCase()); 
        
     
    } 
    
}
