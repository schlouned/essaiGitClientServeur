/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.ui;

import be.isl.ue.business.PersonFacade;
import be.isl.ue.entity.Person;
import be.isl.ue.vm.PersonSearchVM;
import java.io.Serializable;
import java.sql.Array;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;

/**
 *
 * @author Schloune Denis
 */
@Named(value = "personController")
@SessionScoped
public class PersonController extends AbstractController<Person, PersonFacade, PersonSearchVM>
        implements Serializable {

    @Inject
    private PersonFacade ejbFacade;

    public PersonController() {
    }

    @PostConstruct
    public void init() {
        setSearch(new PersonSearchVM());
    }

    @Override
    public PersonFacade getFacade() {
        return ejbFacade;
    }

    @Override
    public Person create() {
        return new Person();
    }

    public Person getPerson(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    //********************************************
    //teachers: list of every teacher from person
    //********************************************
    private List<Person> teachers = null;
    
    //recover the teacher list thanks to jpql request in ejb
    public List getTeachers() {
        List listTemp = getFacade().findTeachers();
        teachers = listTemp;
        return teachers;
    }
    
    //we wont use this one, because we won't add teachers...
    public void setTeachers(List<Person> teachers) {
        this.teachers = teachers;
    }
    
    //**************************************************************************
    //GetPersons Methods create for the section page to select a list of person
    //the SelectItem method create a problem for this use
    //**************************************************************************7
    public List<Person> getPersons(){
        return getFacade().findAll();
    }
    


    

    //************************************
    @FacesConverter(forClass = Person.class, value="personConverter")
    public static class PersonControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PersonController controller = (PersonController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "personController");
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
            if (object instanceof Person) {
                Person o = (Person) object;
                return getStringKey(o.getPersonId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Person.class.getName());
            }
        }

    }
}
