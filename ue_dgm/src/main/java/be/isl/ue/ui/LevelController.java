/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.ui;

import be.isl.ue.business.LevelFacade;
import be.isl.ue.business.SectionFacade;
import be.isl.ue.entity.Level;
import be.isl.ue.entity.Section;
import be.isl.ue.vm.LevelSearchVM;
import be.isl.ue.vm.SectionSearchVM;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 *
 * @author molka
 */
@Named(value = "levelController")
@SessionScoped

public class LevelController  extends AbstractController<Level, LevelFacade, LevelSearchVM>
        implements Serializable{
    
     @Inject
    private LevelFacade ejbFacade;

    /**
     * Creates a new instance of SectionController
     */
    public LevelController() {
    }
    
    @PostConstruct
    public void init() {
        setSearch(new LevelSearchVM());
    }

    @Override
    public LevelFacade getFacade() {
        return ejbFacade;
    }
    
    @Override
    public Level create() {
        return new Level();
    }

    public Level getLevel(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Level.class)
    public static class LevelControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            LevelController controller = (LevelController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "levelController");
            return controller.getLevel(getKey(value));
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
            if (object instanceof Level) {
                Level o = (Level) object;
                return getStringKey(o.getLevelId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Level.class.getName());
            }
        }
    }
    
}
