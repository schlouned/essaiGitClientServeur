/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.ui;

import be.isl.ue.business.PresenceFacade;
import be.isl.ue.entity.Level;
import be.isl.ue.entity.OrganizedUe;
import be.isl.ue.entity.Planning;
import be.isl.ue.entity.Presence;
import be.isl.ue.entity.StudentOrganizedUe;
import be.isl.ue.ui.util.JsfUtil;
import be.isl.ue.vm.OrganizedUeSearchVM;
import be.isl.ue.vm.PresenceSearchVM;
import be.isl.ue.vm.PresencePersonVM;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;


/**
 *
 * @author gaels
 */


@Named(value = "presenceController")
@SessionScoped
public class PresenceController extends AbstractController<OrganizedUe, PresenceFacade, PresenceSearchVM>
        implements Serializable {


    @Inject
    PresenceFacade ejbFacade;
//    @Inject
//    OrganizedUe ejbFacade;
    
     public PresenceController() {
    }

     
    /****************************************************************************/
    /****************************************************************************/
    /****************************************************************************/
  
    private List<PresencePersonVM> presencePersonVMs;

    public List<PresencePersonVM> getPresencePersonVMs() {
        return presencePersonVMs;
    }

    public void setPresencePersonVMs(List<PresencePersonVM> presencePersonVMs) {      
        this.presencePersonVMs = presencePersonVMs;
    }
     
    private List<Planning> plannings;

    public List<Planning> getPlannings() {
        return plannings;
    }

    public void setPlannings(List<Planning> plannings) {
        this.plannings = plannings;
    }
     
     
    
    /****************************************************************************/
    /****************************************************************************/
    /****************************************************************************/


    public List<Level> getLevels(PresenceSearchVM s) {
        List levels = new ArrayList(ejbFacade.findLevelOue(s));
        return levels;
    }
     

    /****************************************************************************/
    /****************************************************************************/
    /****************************************************************************/

        
    public List<String> findSchoolYear(PresenceSearchVM s) {
        List<OrganizedUe> oues = new ArrayList();
        List<String> dateList = new ArrayList() ;
        int startYear = 0;
        int endYear = 0;

        Level levelTemp = s.getLevel();
        oues = ejbFacade.findVM(s);
        
        for (OrganizedUe oue : oues)
        {
            if(oue.getStartDate().getMonth() >= 1 && oue.getStartDate().getMonth() < 7)
            {
                startYear = oue.getStartDate().getYear() - 1 + 1900;
                endYear = oue.getStartDate().getYear() + 1900;
            }
             else if (oue.getStartDate().getMonth() > 7 && oue.getStartDate().getMonth() <= 12)
                    {
                        startYear = oue.getStartDate().getYear() + 1900;
                        endYear = oue.getStartDate().getYear() + 1 + 1900;
                    }
            dateList.add(String.valueOf(startYear) + " - " + String.valueOf(endYear));
        }
        
        s.setLevel(levelTemp);        
        Set<String> mySet = new HashSet<String>(dateList);
        dateList.clear();
        dateList.addAll(mySet);
        Collections.sort(dateList);
        
        return dateList;
    }
    
    /****************************************************************************/
    /****************************************************************************/
    /****************************************************************************/
 
    
    private Date selectedDate;

    public Date getSelectedDate ()
    {
        if(this.selectedDate == null)
        {
            this.selectedDate = new Date();
        }
        return this.selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }


    /****************************************************************************/
    /****************************************************************************/
    /****************************************************************************/


//    public void searchOueDatePresences2 (OrganizedUe oue, Date sdate) {

//        setSelected(oue);
//        presencePersonVMs.clear();
//        plannings.clear();
//        int i = 0;
    
//        if (sdate == null)
//        {
//            sdate = new Date();
//        }
//        
//        for (StudentOrganizedUe soue : this.getSelected().getStudentOrganizedUes())
//        {
//            PresencePersonVM pPVm = new PresencePersonVM();
//            pPVm.setPerson(soue.getPerson());            
////            pPVm.setPresences((List)soue.getPerson().getPresences());           
//
//            i = 0;            
//            while ( i < soue.getPerson().getPresences().size() )
//            {
//                List<Presence> presencesL = new ArrayList(soue.getPerson().getPresences());
//
//                if(presencesL.get(i).getPlanning().getOrganizedUe().getName().equals(getSelected().getName()))
//                {    
//                    presences.add(presencesL.get(i));
//                }
//              i++;  
//            }
//        }
//        this.setPresencePersonVMs(presencePersonVMs);
//    }
//    
   
    /****************************************************************************/
    /****************************************************************************/
    /****************************************************************************/
    
     public  List<PresencePersonVM> searchOueDatePresences (OrganizedUe oue)
     {
        List<Presence> presences = new ArrayList<>();
        setSelected(oue);
        presencePersonVMs.clear();
        plannings.clear();
               
        for (StudentOrganizedUe soue : getSelected().getStudentOrganizedUes())
        {
            presences.clear();
            PresencePersonVM pPVm = new PresencePersonVM();
            pPVm.setPerson(soue.getPerson());            
            
            for (Presence p : soue.getPerson().getPresences())
            {
                if(p.getPlanning().getOrganizedUe().getName().equals(getSelected().getName()))
                {
                    presences.add(p);
                }
            }
            pPVm.setPresences(presences);
            presencePersonVMs.add(pPVm);
        }
        
        for (Presence p : presences)
        {
            plannings.add(p.getPlanning());
        }
        
        return presencePersonVMs;
    }

    /****************************************************************************/
    /****************************************************************************/
    /****************************************************************************/
     
     
    @PostConstruct
    public void init() {
        setSearch(new PresenceSearchVM());
        plannings = new ArrayList<>();
        presencePersonVMs = new ArrayList<>();
        List<PresencePersonVM> pPVms = new ArrayList();
    }

    @Override
    public PresenceFacade getFacade() {
        return ejbFacade;
    }

    @Override
    public OrganizedUe create() {
        return new OrganizedUe();
    }


    public OrganizedUe getPresence(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Presence.class)
    public static class PresenceControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PersonController controller = (PersonController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "presenceController");
            return controller.getPerson(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Presence) {
                Presence o = (Presence) object;
                return getStringKey(o.getPresenceId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Presence.class.getName());
            }
        }

    } 
    
}