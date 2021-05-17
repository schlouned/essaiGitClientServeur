/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.business;

import be.isl.ue.entity.Capacity;
import be.isl.ue.vm.CapacitySearchVM;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/******************************************************
 *Classe used to convert information from DB to object
 * and from object to DB
 * for capacity entity
 * 
 * @author Schloune Denis
 ******************************************************/
@Stateless
public class CapacityFacade extends AbstractFacade<Capacity, CapacitySearchVM> {
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
    public CapacityFacade() {
        super(Capacity.class);
    }
    
    //************************************************
    //Method search with filter from Object Search VM
    //************************************************
    public List <Capacity> findVM(CapacitySearchVM s){
        String jpql = "SELECT c FROM Capacity c ";
        Boolean nameParam = s.getName() != null && !s.getName().trim().equals("");
        Boolean ueNameParam = s.getUeName() != null && !s.getUeName().trim().equals("");
        Boolean whereSet = false;
        
        //************************************************************
        //Possibles where clauses 
        //************************************************************
        if(nameParam){
            jpql = jpql + "WHERE " + "UPPER(c.name) LIKE UPPER(:name) ";
            whereSet = true;
        }
        if(ueNameParam){
            if(whereSet){
                jpql += "AND ";
            }
            else{
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "UPPER(c.ue.name) LIKE UPPER(:ueName) ";
        }
       
        //*******************************
        //Order by
        //*******************************
        jpql += "ORDER BY c.name";
        
        //*********************************
        //Query
        //*********************************
        Query query = em.createQuery(jpql);
        
        //************************************************************
        //Set parameter to the query 
        //************************************************************ 
        if(nameParam){
            query.setParameter("name", "%" + s.getName() + "%");
        }
        if(ueNameParam){
            query.setParameter("ueName", "%" + s.getUeName()+ "%");
        }
        
        //*******************************
        //Return results
        //*******************************
        return query.getResultList();              
    }
}
