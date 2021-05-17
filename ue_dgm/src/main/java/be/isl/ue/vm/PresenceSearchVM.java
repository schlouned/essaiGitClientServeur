/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.vm;

import be.isl.ue.entity.Level;
import be.isl.ue.entity.Section;
import java.util.Date;

/**
 *
 * @author gaels
 */
public class PresenceSearchVM {
       

    private Level level;
    private Section section;
    private String schoolYear;
    private Date dateSeanceSelected;

    
    public PresenceSearchVM() {
    }

    
    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Date getDateSeanceSelected() {
        return dateSeanceSelected;
    }

    public void setDateSeanceSelected(Date dateSeanceSelected) {
        this.dateSeanceSelected = dateSeanceSelected;
    }


}
