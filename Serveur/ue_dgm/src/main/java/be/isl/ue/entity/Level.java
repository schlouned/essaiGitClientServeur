/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Schloune Denis
 */
@Entity
@Table(name = "level")
@NamedQueries({
    @NamedQuery(name = "Level.findAll", query = "SELECT l FROM Level l"),
    @NamedQuery(name = "Level.findByLevelId", query = "SELECT l FROM Level l WHERE l.levelId = :levelId"),
    @NamedQuery(name = "Level.findByName", query = "SELECT l FROM Level l WHERE l.name = :name")})
public class Level extends AbstractEntity implements Serializable, Comparable<Level> {

    private static final long serialVersionUID = 1L;
    static java.util.logging.Level SEVERE;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "level_id")
    private Integer levelId;
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "level")
    private Collection<OrganizedUe> organizedUes;

    //Method from abstract class AbstractEntity
    @Override
    public Integer getId() {
        return levelId;
    }
    
    public Level() {
    }

    public Level(Integer levelId) {
        this.levelId = levelId;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<OrganizedUe> getOrganizedUes() {
        return organizedUes;
    }

    public void setOrganizedUes(Collection<OrganizedUe> organizedUes) {
        this.organizedUes = organizedUes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (levelId != null ? levelId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Level)) {
            return false;
        }
        Level other = (Level) object;
        if ((this.levelId == null && other.levelId != null) || (this.levelId != null && !this.levelId.equals(other.levelId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
    
    public int compareTo(Level o){
    Boolean nameBool = false;
        
       if(this.name != null && this.name.length() > 0 && o.getName() != null && o.getName().length() > 0){
            nameBool = true;
        }
        if(!nameBool){
            //return this.levelId.compareTo(o.getLevelId());
           return 0;
        }
        return this.name.toLowerCase().compareTo(o.name.toLowerCase());
    }   
}
    

