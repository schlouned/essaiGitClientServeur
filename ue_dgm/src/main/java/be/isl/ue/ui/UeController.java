/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.ui;

import be.isl.ue.business.CapacityFacade;
import be.isl.ue.business.UeFacade;
import be.isl.ue.entity.Capacity;
import be.isl.ue.entity.Ue;
import be.isl.ue.ui.util.JsfUtil;
import be.isl.ue.vm.OrganizedUeSearchVM;
import be.isl.ue.vm.UeSearchVM;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

/**
 *
 * @author Gael
 */
@Named(value = "ueController")
@SessionScoped
public class UeController
        extends AbstractController<Ue, UeFacade, UeSearchVM>
        implements Serializable {

    @Inject
    private UeFacade ejbFacade;
    @Inject
    private CapacityFacade capacityFacade;

    public UeController() {
    }
    
    @PostConstruct
    public void init() {
        setSearch(new UeSearchVM());
    }

    @Override
    public UeFacade getFacade() {
        return ejbFacade;
    }

    @Override
    public Ue create() {
        return new Ue();
    }

    @Override
    public String doUpdate() {
        try {
            if (getSelected().getId() == null) {
                getFacade().create(getSelected());
                doSearch();
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UeCreated"));
            } else {
                for (Capacity cap : ((Ue) getSelected()).getCapacities()) {
                    if (cap.getCapacityId() == null) {
                        capacityFacade.create(cap);
                    } else {
                        capacityFacade.edit(cap);
                    }
                }
                getFacade().edit(getSelected());
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UeUpdated"));
            }
            return FacesContext.getCurrentInstance().getViewRoot().getViewId();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String doCapNew() {
        Capacity cap = new Capacity();
        cap.setUe(getSelected());
        ((Ue) getSelected()).getCapacities().add(cap);
        return FacesContext.getCurrentInstance().getViewRoot().getViewId();
    }

    public String doCapRemove(Capacity cap) {
        ((Ue) getSelected()).getCapacities().remove(cap);
        ejbFacade.edit(getSelected());
        capacityFacade.remove(cap);
        return FacesContext.getCurrentInstance().getViewRoot().getViewId();
    }

    public Ue getUe(java.lang.Integer id) {
        return ejbFacade.find(id);
    }
    //**********
    public SelectItem[] getItemsAvailableSelectOneUe(OrganizedUeSearchVM s) {
        System.out.println("\n\n*-*-*-*-*-*-*\n getItemsAvailableSelectOneUE/*/*/() appele "+ "s=" +s);
        //OrganizedUeSearchVM s = new OrganizedUeSearchVM();
        //s.setSectionName(getSearch().getSectionName());
        List listTemp = getFacade().findAvailableUeForOrganizedUe(s);
        Collections.sort(listTemp);
        System.out.println("*-*-*-*-*-*-*\n");
        return JsfUtil.getSelectItems(listTemp, true);
    }
    //********

    @FacesConverter(forClass = Ue.class)
    public static class UeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UeController controller = (UeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "ueController");
            return controller.getUe(getKey(value));
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
            if (object instanceof Ue) {
                Ue o = (Ue) object;
                return getStringKey(o.getUeId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Ue.class.getName());
            }
        }

    }
}
