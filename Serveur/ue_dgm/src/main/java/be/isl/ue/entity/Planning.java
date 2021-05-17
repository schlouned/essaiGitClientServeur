/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author Schloune Denis
 */
@Entity
@Table(name = "planning")
@NamedQueries({
    @NamedQuery(name = "Planning.findAll", query = "SELECT p FROM Planning p"),
    @NamedQuery(name = "Planning.findByPlanningId", query = "SELECT p FROM Planning p WHERE p.planningId = :planningId"),
    @NamedQuery(name = "Planning.findByRoom", query = "SELECT p FROM Planning p WHERE p.room = :room"),
    @NamedQuery(name = "Planning.findBySeanceDate", query = "SELECT p FROM Planning p WHERE p.seanceDate = :seanceDate"),
    @NamedQuery(name = "Planning.findByStartHour", query = "SELECT p FROM Planning p WHERE p.startHour = :startHour")})
public class Planning extends AbstractEntity implements Serializable, Comparable<Planning> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "planning_id")
    private Integer planningId;
    @Size(max = 255)
    @Column(name = "room")
    private String room;
    @Column(name = "seance_date")
    @Temporal(TemporalType.DATE)
    private Date seanceDate;
    @Column(name = "start_hour")
    @Temporal(TemporalType.TIME)
    private Date startHour;
    @JoinColumn(name = "organized_ue_id", referencedColumnName = "organized_ue_id")
    @ManyToOne
    private OrganizedUe organizedUe;
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    @ManyToOne
    private Person person;
    @OneToMany(mappedBy = "planning")
    private Collection<Presence> presences;

    //Method from abstract class AbstractEntity
    @Override
    public Integer getId() {
        return planningId;
    }

    public Planning() {
    }

    public Planning(Integer planningId) {
        this.planningId = planningId;
    }

    public Integer getPlanningId() {
        return planningId;
    }

    public void setPlanningId(Integer planningId) {
        this.planningId = planningId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Date getSeanceDate() {
        return seanceDate;
    }

    public void setSeanceDate(Date seanceDate) {
        this.seanceDate = seanceDate;
    }

    public Date getStartHour() {
        return startHour;
    }

    public void setStartHour(Date startHour) {
        this.startHour = startHour;
    }

    public OrganizedUe getOrganizedUe() {
        return organizedUe;
    }

    public void setOrganizedUe(OrganizedUe organizedUe) {
        this.organizedUe = organizedUe;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Collection<Presence> getPresences() {
        return presences;
    }

    public void setPresences(Collection<Presence> presences) {
        this.presences = presences;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (planningId != null ? planningId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Planning)) {
            return false;
        }
        Planning other = (Planning) object;
        if ((this.planningId == null && other.planningId != null) || (this.planningId != null && !this.planningId.equals(other.planningId))) {
            return false;
        }
        return true;
    }

    /*  @Override
    public String toString() {
        return " Ue: " + organizedUe.getName() + " Date: " + seanceDate;
    }*/
    @Override
    public String toString() {
        return organizedUe.getName();
    }

    public Date getPlanningDate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setPlanningDate(Date startDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int compareTo(Planning o){
        return this.planningId.compareTo(o.getPlanningId());
    }
}
