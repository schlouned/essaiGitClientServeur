/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.vm;

import be.isl.ue.entity.Person;
import be.isl.ue.entity.Presence;
import java.util.List;

/**
 *
 * @author gaels
 */
public class PresencePersonVM {
    
    public Person person;
    public List <Presence> presences;

    public PresencePersonVM() {
    }
    
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<Presence> getPresences() {
        return presences;
    }

    public void setPresences(List<Presence> presences) {
        this.presences = presences;
    }
    
    
    
}
