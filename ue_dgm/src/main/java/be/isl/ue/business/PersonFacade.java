/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.business;

import be.isl.ue.entity.Person;
import be.isl.ue.vm.PersonSearchVM;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/******************************************************
 *Classe used to convert information from DB to object
 * and from object to DB
 * for person entity
 * 
 * @author Schloune Denis
 ******************************************************/
@Stateless
public class PersonFacade extends AbstractFacade<Person, PersonSearchVM> {
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
    public PersonFacade() {
        super(Person.class);
    }
    
    //************************************************
    //Method search with filter from Object Search VM
    //************************************************
    public List<Person> findVM(PersonSearchVM s){
        String jpql = "SELECT p FROM Person p ";
        Boolean lastNameParam = s.getLastName() != null && !s.getLastName().trim().equals("");
        Boolean firstNameParam = s.getFirstName() != null && !s.getFirstName().trim().equals("");
        Boolean cityParam = s.getCity() != null && !s.getCity().trim().equals("");
        Boolean dateOfBirthParam = s.getDateOfBirth() != null && !s.getDateOfBirth().trim().equals("");
        Boolean emailParam = s.getEmail() != null && !s.getEmail().trim().equals("");
        Boolean whereSet = false;
        
        //************************************************************
        //Possibles where clauses 
        //************************************************************
        if(lastNameParam){
            jpql = jpql + "WHERE " + "UPPER(p.name) LIKE UPPER(:lastName) ";
            whereSet = true;
        }
        if(firstNameParam){
            if(whereSet){
                jpql += "AND ";
            }
            else{
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "UPPER(p.firstName) LIKE UPPER(:firstName) ";
        }
        if(cityParam){
            if(whereSet){
                jpql += "AND ";
            }
            else{
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "UPPER(p.city) LIKE UPPER(:city) ";
        }
        if(dateOfBirthParam){
            if(whereSet){
                jpql += "AND ";
            }
            else{
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "p.dateOfBirth BETWEEN :dateOfBirthMinusOne AND :dateOfBirthPlusOne ";
        }
        if(emailParam){
            if(whereSet){
                jpql += "AND ";
            }
            else{
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "LOWER(p.email) LIKE LOWER(:email) ";
        }
        
        //*******************************
        //Order by
        //*******************************
        jpql += "ORDER BY p.name";
        
        //*********************************
        //Query
        //*********************************
        Query query = em.createQuery(jpql);
        
        //************************************************************
        //Set parameter to the query 
        //************************************************************        
        if(lastNameParam){
            query.setParameter("lastName", "%" + s.getLastName() + "%");
        }
        if(firstNameParam){
            query.setParameter("firstName", "%" + s.getFirstName() + "%");
        }
        if(cityParam){
            query.setParameter("city", "%" + s.getCity() + "%");
        }
        if(dateOfBirthParam){
            try {
                Date date = new java.sql.Date(new SimpleDateFormat("dd-MM-yyyy").parse(s.getDateOfBirth()).getTime());
                //******************
                //operation on date
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, -1);
                Date dateMinusOne = c.getTime();
                c.add(Calendar.DATE, 2);
                Date datePlusOne = c.getTime();
                //******************
                query.setParameter("dateOfBirthMinusOne", dateMinusOne );
                query.setParameter("dateOfBirthPlusOne", datePlusOne );
            } catch (ParseException ex) {
                Logger.getLogger(PersonFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(emailParam){
            query.setParameter("email", "%" + s.getEmail() + "%");
        }
        
        //*******************************
        //Return results
        //*******************************
        return query.getResultList();
    }
    
    //***********************
    // add getTeachers
    public List<Person> findTeachers(){
        //
        String jpql = "SELECT p FROM Person p WHERE p.isTeacher = TRUE ORDER BY p.name";
        
        //*********************************
        //Query
        //*********************************
        Query query = em.createQuery(jpql);
        
        //*******************************
        //Return results
        //*******************************
        return query.getResultList();

    }
    //***********************
    
}
