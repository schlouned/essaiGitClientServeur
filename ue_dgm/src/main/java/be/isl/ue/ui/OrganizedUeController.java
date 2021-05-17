/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.ui;

import be.isl.ue.business.OrganizedUeFacade;
import be.isl.ue.entity.OrganizedUe;
import be.isl.ue.entity.Person;
import be.isl.ue.ui.util.JsfUtil;
import be.isl.ue.vm.OrganizedUeSearchVM;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author Schloune Denis
 */
@Named(value = "organizedUeController")
@SessionScoped
public class OrganizedUeController extends AbstractController<OrganizedUe, OrganizedUeFacade, OrganizedUeSearchVM>
        implements Serializable {

    /**
     * Creates a new instance of OrganizedUeController
     */
    //inject dependency
    @Inject
    OrganizedUeFacade ejbFacade;

    //********************************************************
    //add a second instance of searchVM object
    private OrganizedUeSearchVM searchOUePerson;

    public OrganizedUeSearchVM getSearchOUePerson() {
        return searchOUePerson;
    }

    public void setSearchOUePerson(OrganizedUeSearchVM searchOUePerson) {
        this.searchOUePerson = searchOUePerson;
    }

    public void doSearchOUePerson() {
        DataModel items = null;
        //*** Modification to sort the list ***//
        List listTemp = getFacade().findVM(getSearchOUePerson());
        Collections.sort(listTemp);
        items = new ListDataModel(listTemp);
        //****
        if (items != null && items.getRowCount() > 0) {
            items.setRowIndex(0);
            setSelected((OrganizedUe) items.getRowData());
        }
    }
    //*********************************************************

    //constructor
    public OrganizedUeController() {
    }

    //post construct
    @PostConstruct
    public void init() {
        setSearch(new OrganizedUeSearchVM());
        setSearchOUePerson(new OrganizedUeSearchVM());
    }

    @Override
    public OrganizedUeFacade getFacade() {
        return ejbFacade;
    }

    @Override
    public OrganizedUe create() {
        return new OrganizedUe();
    }

    public OrganizedUe getOrganizedUe(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    //*********************************************************************
    // doCopy: 
    //*********************************************************************
    //there is a try catch for a Parse Exception because of dates
    public void copyOue() {
        try {
            ejbFacade.copyOrganizedUes(this.getSearch());
        } catch (ParseException ex) {
            Logger.getLogger(OrganizedUeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //**********************************************
    // selectedTeachers:
    //**********************************************
    //To select teacher(s) who will organized the Ue
    private Collection<Person> selectedTeachers = null;

    public Collection<Person> getSelectedTeachers() {
        selectedTeachers = this.getSelected().getPersons();
        return selectedTeachers;
    }

    public void setSelectedTeachers(Collection<Person> selectedTeachers) {
        this.selectedTeachers = selectedTeachers;
        this.getSelected().setPersons(selectedTeachers);
    }

    //********************************************
    //select academicYears for the search request
    //********************************************
    //for the dynamic list of academic years
    private String academicOption;

    public String getAcademicOption() {
        return academicOption;
    }

    public void setAcademicOption(String academicOption) {
        this.academicOption = academicOption;
    }

    private List<String> academicOptions;

    public List<String> getAcademicOptions(OrganizedUeSearchVM s) {
        academicOptions = ejbFacade.SelectAcademicYears(s);
        return academicOptions;
    }

    public void setOptions(List<String> academicOptions) {
        this.academicOptions = academicOptions;
    }

    //********************************************
    //return the organizedUe for a student
    //********************************************
    public List<OrganizedUe> getOrganizedUeList(Person p) {
        List<OrganizedUe> l = new ArrayList<OrganizedUe>();
        OrganizedUeSearchVM o = new OrganizedUeSearchVM();
        try {
            o.setPerson(p);
            l = ejbFacade.findStudentOUeVM(o);
        } catch (java.lang.NullPointerException e) {
        }
        return l;
    }

    //*************************
    //return each levels
    //*************************
    public List<be.isl.ue.entity.Level> getLevels(OrganizedUeSearchVM s) {
        return ejbFacade.findLevelByOrganizedUe(s);
    }

    public SelectItem[] getItemsAvailableSelectOneLevel(OrganizedUeSearchVM s) {
        List listTemp = ejbFacade.findLevelByOrganizedUe(s);
        Collections.sort(listTemp);
        return JsfUtil.getSelectItems(listTemp, true);
    }

    //***************************************************
    //Registration
    //***************************************************
    public void registerStudentInOrganizedUe(OrganizedUeSearchVM o, Person p) throws ParseException {
        try {
            ejbFacade.registrationPersonInOrganizedUe(o, p);
            System.out.println("***SuccessMessage: L'inscription a ete faite avec succes!");
            JsfUtil.addSuccessMessage("***SuccessMessage: L'inscription a ete faite avec succes!");
        } 
        catch (javax.ejb.EJBException e) {
            System.out.println("***\n" + e.getClass() + " / " + e.getMessage() + "\n***");
            System.out.println("***ErrorMessage: L'eleve est deja inscrit à ce cours!");
            JsfUtil.addErrorMessage("***ErrorMessage: L'eleve est deja inscrit à ce cours!");
        }

    }

    //********************
    //Remove organisation
    //********************
    public void removeOrganisation() {
        ejbFacade.suppressOrganization(getSearch());
        doSearch();
    }

    //******************************************************************************************
    //converter
    @FacesConverter(forClass = OrganizedUe.class)
    public static class OrganizedUeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            OrganizedUeController controller = (OrganizedUeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "organizedUeController");
            return controller.getOrganizedUe(getKey(value));
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
            if (object instanceof OrganizedUe) {
                OrganizedUe o = (OrganizedUe) object;
                return getStringKey(o.getOrganizedUeId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + OrganizedUe.class.getName());
            }
        }

    }

}
