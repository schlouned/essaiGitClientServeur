/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.business;

import be.isl.ue.entity.Capacity;
import be.isl.ue.entity.Ue;
import be.isl.ue.vm.OrganizedUeSearchVM;
import be.isl.ue.vm.UeSearchVM;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/******************************************************
 *Classe used to convert information from DB to object
 * and from object to DB
 * for ue entity
 * 
 * @author Schloune Denis
 ******************************************************/
@Stateless
public class UeFacade extends AbstractFacade<Ue, UeSearchVM> {
    //*********************************
    //Describe persistence contexte
    //*********************************
    @PersistenceContext(unitName = "ue_pu")
    private EntityManager em;

    //*********************************
    //get entity manager
    //*********************************
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    //*********************************
    //Constructor
    //*********************************
    public UeFacade() {
        super(Ue.class);
    }

    //************************************************
    //Method search with filter from Object Search VM
    //************************************************
    public List<Ue> findVM(UeSearchVM s) {
        String jpql = "SELECT u FROM Ue u ";
        Boolean nameParam = s.getName() != null && !s.getName().trim().equals("");
        Boolean descriptionParam = s.getDescription() != null && !s.getDescription().trim().equals("");
        Boolean sectionNameParam = s.getSectionName() != null && !s.getSectionName().trim().equals("");
        Boolean capacityNameParam = s.getCapacityName() != null && !s.getCapacityName().trim().equals("");
        Boolean codeParam = s.getCode() != null && !s.getCode().trim().equals("");
        Boolean whereSet = false;

        //************************************************************
        //Possibles where clauses 
        //************************************************************
        if (nameParam) {
            jpql = jpql + "WHERE " + "UPPER(u.name) LIKE UPPER(:name) ";
            whereSet = true;
        }
        if (descriptionParam) {
            if (whereSet) {
                jpql += "AND ";
            } else {
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "UPPER(u.description) LIKE UPPER(:description) ";
        }
        if (sectionNameParam) {
            if (whereSet) {
                jpql += "AND ";
            } else {
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "UPPER(u.section.name) LIKE UPPER(:sectionName) ";
        }
        if (codeParam) {
            if (whereSet) {
                jpql += "AND ";
            } else {
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "UPPER(u.code) LIKE UPPER(:code) ";
        }
        if (capacityNameParam) {
            if (whereSet) {
                jpql += "AND ";
            } else {
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "u IN ("
                    + "SELECT c.ue FROM Capacity c "
                    + "WHERE c.ue = u "
                    + "AND UPPER(c.name) LIKE UPPER(:capacityName))";
        }
        
        //*******************************
        //Order by
        //*******************************
        jpql += "ORDER BY u.name";

        //*********************************
        //Query
        //*********************************
        Query query = em.createQuery(jpql);

        //************************************************************
        //Set parameter to the query 
        //************************************************************
        if (nameParam) {
            query.setParameter("name", "%" + s.getName() + "%");
        }
        if (descriptionParam) {
            query.setParameter("description", "%" + s.getDescription() + "%");
        }
        if (sectionNameParam) {
            query.setParameter("sectionName", "%" + s.getSectionName() + "%");
        }
        if (codeParam) {
            query.setParameter("code", "%" + s.getCode() + "%");
        }
        if (capacityNameParam) {
            query.setParameter("capacityName", "%" + s.getCapacityName() + "%");
        }
        
        //*******************************
        //Return results
        //*******************************
        return query.getResultList();
    }

    //**************************************************
    //Method remove Ue -> !!! remove capacity before ue
    //**************************************************
    public void removeUe(Ue entity, CapacityFacade cDAO) {
        Collection<Capacity> capacities = entity.getCapacities();
        Iterator<Capacity> it = capacities.iterator();
        Capacity cap;
        //***************************************************************
        //first remove capacities linked with the ue you want to delete
        //***************************************************************
        while (it.hasNext()) {
            cap = it.next();
            cDAO.remove(cap);
            it.remove();
        }
        //***************************************************************
        //second delete the ue
        //***************************************************************
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    //**********************************************************************************
    //Method edit Ue -> if needed remove capacity from the DB if removed in the entity
    //**********************************************************************************
    public void editUe(Ue entity, CapacityFacade cDAO) {
        //***************************************************************
        //first save the ue
        //***************************************************************
        getEntityManager().merge(entity);

        //array and iterator for Ue and DB
        Collection<Capacity> capacitiesUe = entity.getCapacities();
        Iterator<Capacity> itUe = capacitiesUe.iterator();
        Collection<Capacity> capacitiesDB = cDAO.findAll();
        Iterator<Capacity> itDB = capacitiesDB.iterator();

        //***************************************************************
        //second remove capacity suppressed in ue
        //***************************************************************
        //loop for capacity in Ue
        while (itUe.hasNext()) {
            Capacity capUe = itUe.next();

            //loop for capacity in DB
            while (itDB.hasNext()) {
                Capacity capDB = itDB.next();

                //if capacity from ue and DB are from the same Ue
                if (capUe.getUe().equals(capDB.getUe())) {

                    //if a capacity from the DB is not in the Ue => remove this capacity from the DB
                    if (!capacitiesUe.contains(capDB)) {
                        cDAO.remove(capDB);
                    }
                }
            }
        }

    }
    
    //*********************
    //select ue available for section in organizedUe
    //*********************
    public List<Ue> findAvailableUeForOrganizedUe(OrganizedUeSearchVM s){
        String jpql = "SELECT u FROM Ue u ";
        
        Boolean sectionParam = s.getSection()!= null;
        Boolean whereSet = false;

        //************************************************************
        //Possibles where clauses 
        //************************************************************
        if (sectionParam) {
            jpql = jpql + "WHERE " + "u.section.name = :sectionName ";
            whereSet = true;
        }
        
        //*******************************
        //Order by
        //*******************************
        jpql += "ORDER BY u.name";

        //*********************************
        //Query
        //*********************************
        Query query = em.createQuery(jpql);

        //************************************************************
        //Set parameter to the query 
        //************************************************************
        if (sectionParam) {
            query.setParameter("sectionName", s.getSection().getName());
        }
        
        //*******************************
        //Return results
        //*******************************
        return query.getResultList();
        
    }
}
