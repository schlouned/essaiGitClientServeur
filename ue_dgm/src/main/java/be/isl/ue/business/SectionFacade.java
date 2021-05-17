/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.business;

import be.isl.ue.entity.Section;
import be.isl.ue.vm.SectionSearchVM;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/******************************************************
 *Classe used to convert information from DB to object
 * and from object to DB
 * for section entity
 * 
 * @author Schloune Denis
 ******************************************************/
@Stateless
public class SectionFacade extends AbstractFacade<Section, SectionSearchVM> {
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
    public SectionFacade() {
        super(Section.class);
    }
    
    //************************************************
    //Method search with filter from Object Search VM
    //************************************************
    public List<Section> findVM(SectionSearchVM s){
        String jpql = "SELECT s FROM Section s ";
        Boolean nameParam = s.getName() != null && !s.getName().trim().equals("");
        Boolean personFirstNameParam = s.getPersonFirstName()!= null && !s.getPersonFirstName().trim().equals("");
        Boolean personLastNameParam = s.getPersonLastName()!= null && !s.getPersonLastName().trim().equals("");
        Boolean descriptionParam = s.getDescription() != null && !s.getDescription().trim().equals("");
        Boolean whereSet = false;
        
        //************************************************************
        //Possibles where clauses 
        //************************************************************
        if(nameParam){
            jpql = jpql + "WHERE " + "UPPER(s.name) LIKE UPPER(:name) ";
            whereSet = true;
        }
        if(personFirstNameParam){
            if(whereSet){
                jpql += "AND ";
            }
            else{
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "UPPER(s.person.firstName) LIKE UPPER(:personFirstName) ";
        }
         if(personLastNameParam){
            if(whereSet){
                jpql += "AND ";
            }
            else{
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "UPPER(s.person.name) LIKE UPPER(:personLastName) ";
        }
         if(descriptionParam){
            if(whereSet){
                jpql += "AND ";
            }
            else{
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "UPPER(s.description) LIKE UPPER(:description) ";
        }
        
        //*******************************
        //Order by
        //*******************************
        jpql += "ORDER BY s.name";
        
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
        if(personFirstNameParam){
            query.setParameter("personFirstName", "%" + s.getPersonFirstName() + "%");
        }
        if(personLastNameParam){
            query.setParameter("personLastName", "%" + s.getPersonLastName() + "%");
        }
        if(descriptionParam){
            query.setParameter("description", "%" + s.getDescription()+ "%");
        }
        
        //*******************************
        //Return results
        //*******************************
        return query.getResultList();
        
    }
    
}
