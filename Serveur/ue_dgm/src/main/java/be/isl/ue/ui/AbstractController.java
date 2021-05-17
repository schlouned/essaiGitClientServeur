/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.ui;

import be.isl.ue.business.AbstractFacade;
import be.isl.ue.entity.AbstractEntity;
import be.isl.ue.ui.util.JsfUtil;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

/**
 *
 * @author esc
 * @param <T>
 * @param <U>
 * @param <V>
 */
public abstract class AbstractController<T extends AbstractEntity, U extends AbstractFacade, V> {

    private T current;
    private V search;
    private DataModel items = null;

    public abstract T create();

    public abstract U getFacade();

    public DataModel getItems() {
        if (items == null) {
            //*** Modification to sort the list ***//
            List listTemp = getFacade().findVM(getSearch());
            Collections.sort(listTemp);
            items = new ListDataModel(listTemp);
            //****
            if (items != null && items.getRowCount() > 0) {
                items.setRowIndex(0);
                setSelected((T) items.getRowData());
            } else {
                current = create();
            }
        }
        return items;
    }

    public void setItems(DataModel items) {
        this.items = items;
    }

    public V getSearch() {
        return search;
    }

    public void setSearch(V search) {
        this.search = search;
    }

    public T getSelected() {
        if (current == null) {
            current = create();
        }
        return current;
    }

    public void setSelected(T current) {
        this.current = current;
    }

    public void doDelete() {
        try {
            getFacade().remove(getSelected());
            doSearch();

            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ItemDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public String doUpdate() {
        try {
            if (getSelected().getId() == null) {
                getFacade().create(getSelected());
                doSearch();
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ItemCreated"));
            } else {
                getFacade().edit(getSelected());
                doSearch();
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ItemUpdated"));
            }
            System.out.println(FacesContext.getCurrentInstance().getViewRoot().getViewId());
            return FacesContext.getCurrentInstance().getViewRoot().getViewId();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public void doSearch() {
        //*** Modification to sort the list ***//
        List listTemp = getFacade().findVM(getSearch());
        Collections.sort(listTemp);
        items = new ListDataModel(listTemp);
        //****
        if (items != null && items.getRowCount() > 0) {
            items.setRowIndex(0);
            setSelected((T) items.getRowData());
        }
    }

    public String prepareCreate() {
        current = create();
        return FacesContext.getCurrentInstance().getViewRoot().getViewId();
    }

    public String prepareEdit(T a) {
        setSelected(a);
        return FacesContext.getCurrentInstance().getViewRoot().getViewId();
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getFacade().findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        List listTemp = getFacade().findAll();
        Collections.sort(listTemp);
        return JsfUtil.getSelectItems(listTemp, true);
    }

}
