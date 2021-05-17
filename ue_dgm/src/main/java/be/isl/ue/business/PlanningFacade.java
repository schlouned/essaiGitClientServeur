/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.business;

import be.isl.ue.entity.OrganizedUe;
import be.isl.ue.entity.Person;
import be.isl.ue.entity.Planning;
import be.isl.ue.entity.Ue;
import be.isl.ue.vm.OrganizedUeSearchVM;

import be.isl.ue.vm.PlanningSearchVM;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.TreeSet;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Schloune Denis
 */
@Stateless
public class PlanningFacade extends AbstractFacade<Planning, PlanningSearchVM> {

    @PersistenceContext(unitName = "ue_pu")
    private EntityManager em;

    @Inject
    OrganizedUeFacade organizedUe;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PlanningFacade() {
        super(Planning.class);
    }

    public List<Planning> findVM(PlanningSearchVM s) {
        String jpql = "SELECT p FROM Planning p ";
        Boolean academicYearParam = s.getAcademicYear() != null;
        Boolean sectionParam = s.getSection() != null;
        Boolean organizedUeParam = s.getOrganizedUe() != null;
        Boolean personParam = s.getPerson() != null;
        Boolean presencePram = s.getPresence() != null;

        Boolean whereSet = false;

        /**
         * ***Where clauses***
         */
        if (academicYearParam) {
            if (whereSet) {
                jpql += "AND ";
            } else {
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "p.seance_date :seanceDate ";
        }

        if (sectionParam) {
            if (whereSet) {
                jpql += "AND ";
            } else {
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "p.person.section.sectionId = :sectionId";
        }

        if (organizedUeParam) {
            if (whereSet) {
                jpql += "AND ";
            } else {
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "p.o.ue.name = :organizedUe ";
        }

        if (personParam) {
            if (whereSet) {
                jpql += "AND ";
            } else {
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "p.person.personId = :personId ";
        }

        if (presencePram) {
            if (whereSet) {
                jpql += "AND ";
            } else {
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "p.presence.presenceId = :presenceId ";
        }

        /**
         * * Query**
         */
        Query query = em.createQuery(jpql);

        /**
         * ***Parameter Query***
         */
        if (sectionParam) {
            query.setParameter("section", s.getSection().getSectionId());
        }
        if (organizedUeParam) {
            query.setParameter("organizedUe", s.getOrganizedUe().getOrganizedUeId());
        }

        if (personParam) {
            query.setParameter("person", s.getPerson().getPersonId());
        }
        if (presencePram) {
            query.setParameter("presence", s.getPresence().getPresenceId());
        }

        return query.getResultList();

    }

    /**
     * **************************************************************************
     */
    //insert Planning method
    /**
     * **************************************************************************
     */

    public List<Planning> insertOUeIntoPlanning(PlanningSearchVM s) {
        //create a list and a list iteratore to stock results
        List<Planning> plannings = new ArrayList<Planning>();
        ListIterator<Planning> it = plannings.listIterator();

        //recover the start date of the ue
        Date date = s.getOrganizedUe().getStartDate();

        //loop to plan the coures of the ue along the academic year
        while (date.before(s.getOrganizedUe().getEndDate())) {
            //create a new planning entity and set it
            Planning planning = new Planning();
            planning.setOrganizedUe(s.getOrganizedUe());
            //planning.setPerson(s.getOrganizedUe().getPersons().get(0));
            planning.setRoom("");

            //set the date in the planning
            planning.setSeanceDate(date);

            //persist the planning
            this.create(planning);

            //add it in a list
            it.add(planning);

            //Increment the date from 7 days
            date = this.dateDayIncrement(date, 7);
        }

        //return the string for test in the servlet
        return plannings;
    }
    

    public Date dateDayIncrement(Date date, int nbDays) {
        //Increment the date from 7 days
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, nbDays);
        date = c.getTime();
        return date;
    }

    // Générer le planning
    public List<String> generateSchedule(PlanningSearchVM s) {
        return null;
        // 

    }

    /**
     * ***********************************************************************
     */
    public List<String> SelectAcademicYears(PlanningSearchVM s) {
        //start the request
        String jpql = "SELECT o FROM OrganizedUe o ";

        //check if there is the parameter
        Boolean sectionParam = s.getSection() != null && s.getSection().getName().length() > 0;
        Boolean whereSet = false;

        if (sectionParam) {
            jpql = jpql + "WHERE " + "o.ue.section.name = :section";
            whereSet = true;
        }

        //Query
        Query query = em.createQuery(jpql);

        //Set parameter to the query        
        if (sectionParam) {
            query.setParameter("section", s.getSection().getName());
        }

        //Order by
       // jpql += "ORDER BY o.startDate";

        //execute the query
        List<OrganizedUe> oUes = query.getResultList();

        //chech the presents academic years and add it in the collection
        //I use a tree Set in order to keep only one instance of an academicYear
        TreeSet<String> academicYearsSet = new TreeSet<String>();
        ListIterator<OrganizedUe> it = oUes.listIterator();
        while (it.hasNext()) {
            academicYearsSet.add(this.findAcademicYear(it.next().getStartDate()));
        }

        //convert the collection to list
        List<String> academicYearList = new ArrayList<String>(academicYearsSet);

        //return
        return academicYearList;
    }

    public String findAcademicYear(Date startDate) {
      
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        String academicYear = "";

        //recover the month
        int startMonth = c.get(Calendar.MONTH);

        //built the academic year
        if (startMonth >= 8 && startMonth <= 11) {
            academicYear = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.YEAR) + 1);
        } else if (startMonth >= 0 && startMonth <= 5) {
            academicYear = (c.get(Calendar.YEAR) - 1) + "-" + c.get(Calendar.YEAR);
        }

        //return result
        return academicYear;
    }

}


