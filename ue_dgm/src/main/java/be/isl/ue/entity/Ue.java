/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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

/**
 * *********************************************
 * Class linked to the ue table
 *
 * @author Schloune Denis
 **********************************************
 */
@Entity
@Table(name = "ue")
@NamedQueries({
    @NamedQuery(name = "Ue.findAll", query = "SELECT u FROM Ue u"),
    @NamedQuery(name = "Ue.findByUeId", query = "SELECT u FROM Ue u WHERE u.ueId = :ueId"),
    @NamedQuery(name = "Ue.findByCode", query = "SELECT u FROM Ue u WHERE u.code = :code"),
    @NamedQuery(name = "Ue.findByDescription", query = "SELECT u FROM Ue u WHERE u.description = :description"),
    @NamedQuery(name = "Ue.findByIsDecisive", query = "SELECT u FROM Ue u WHERE u.isDecisive = :isDecisive"),
    @NamedQuery(name = "Ue.findByName", query = "SELECT u FROM Ue u WHERE u.name = :name"),
    @NamedQuery(name = "Ue.findByNumOfPeriods", query = "SELECT u FROM Ue u WHERE u.numOfPeriods = :numOfPeriods")})

public class Ue extends AbstractEntity implements Serializable, Comparable <Ue>{

    //********************************************
    //members
    //********************************************
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ue_id")
    private Integer ueId;
    @Size(max = 255)
    @Column(name = "code")
    private String code;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Column(name = "is_decisive")
    private Boolean isDecisive;
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    @Column(name = "num_of_periods")
    private Integer numOfPeriods;
    @OneToMany(mappedBy = "ue", cascade = CascadeType.ALL)
    private Collection<Capacity> capacities;
    @JoinColumn(name = "section_id", referencedColumnName = "section_id")
    @ManyToOne
    private Section section;
    @OneToMany(mappedBy = "ue")
    private Collection<OrganizedUe> organizedUes;

    //**********************************************
    //constructors
    //**********************************************   
    //default constructor
    public Ue() {
    }

    public Ue(Integer ueId) {
        this.ueId = ueId;
    }

    //parametrable constructor
    public Ue(Integer ueId, String name, String description, Integer numOfPeriods, Boolean isDecisive, Section section, String code) {
        this.ueId = ueId;
        this.name = name;
        this.description = description;
        this.numOfPeriods = numOfPeriods;
        this.isDecisive = isDecisive;
        this.section = section;
        this.code = code;
    }

    //Method from abstract class AbstractEntity
    @Override
    public Integer getId() {
        return ueId;
    }
    
    //*****************************
    //getter and setter
    //*****************************
    public Integer getUeId() {
        return ueId;
    }

    public void setUeId(Integer ueId) {
        this.ueId = ueId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsDecisive() {
        return isDecisive;
    }

    public void setIsDecisive(Boolean isDecisive) {
        this.isDecisive = isDecisive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumOfPeriods() {
        return numOfPeriods;
    }

    public void setNumOfPeriods(Integer numOfPeriods) {
        this.numOfPeriods = numOfPeriods;
    }

    public Collection<Capacity> getCapacities() {
        return capacities;
    }

    public void setCapacities(Collection<Capacity> capacities) {
        this.capacities = capacities;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }
    
    @JsonbTransient 
    public Collection<OrganizedUe> getOrganizedUes() {
        return organizedUes;
    }

    public void setOrganizedUes(Collection<OrganizedUe> organizedUes) {
        this.organizedUes = organizedUes;
    }

    //************************************************
    //methods
    //************************************************
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ueId != null ? ueId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ue)) {
            return false;
        }
        Ue other = (Ue) object;
        if ((this.ueId == null && other.ueId != null) || (this.ueId != null && !this.ueId.equals(other.ueId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    //compareTo: I decided to use name and sectionName to
    //discriminate an ue
    public int compareTo(Ue u) {
        Boolean nameBool = false;
        Boolean sectionNameBool = false;
        String strThis = "";
        String strP = "";
        if (this.name != null && this.name.length() > 0 && u.getName() != null && u.getName().length() > 0) {
            nameBool = true;
            strThis += this.name;
            strP += u.getName();
        }
        if (this.section != null && u.section != null) {
            if (this.section.getName() != null && this.section.getName().length() > 0 && u.getSection().getName() != null && u.getSection().getName().length() > 0) {
                sectionNameBool = true;
                strThis += this.section.getName();
                strP += u.getSection().getName();
            }
        }
        if (!(nameBool || sectionNameBool)) {
//            return this.ueId.compareTo(u.getUeId());
            return 0;
            
        }
        
        strThis = strThis.toLowerCase();
        strP = strP.toLowerCase();
        return strThis.compareTo(strP);
    }

}
