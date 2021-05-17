/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.business;

import be.isl.ue.entity.Level;
import be.isl.ue.entity.OrganizedUe;
import be.isl.ue.entity.Person;
import be.isl.ue.entity.Planning;
import be.isl.ue.entity.StudentOrganizedUe;
import be.isl.ue.entity.StudentOrganizedUePK;
import be.isl.ue.ui.util.JsfUtil;
import be.isl.ue.vm.OrganizedUeSearchVM;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.TreeSet;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * ****************************************************
 * Classe used to convert information from DB to object and from object to DB
 * for organized ue entity
 *
 * @author Schloune Denis ****************************************************
 */
@Stateless
public class OrganizedUeFacade extends AbstractFacade<OrganizedUe, OrganizedUeSearchVM> {

    //*********************************
    //Describe persistence contexte
    //*********************************
    @PersistenceContext(unitName = "ue_pu")
    private EntityManager em;

    @Inject
    StudentOrganizedUeFacade sOUeFacade;

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
    public OrganizedUeFacade() {
        super(OrganizedUe.class);
    }

    //************************************************
    //Method search with filter from Object Search VM
    //************************************************
    public List<OrganizedUe> findVM(OrganizedUeSearchVM s) {
        String jpql = "SELECT o FROM OrganizedUe o ";
        Boolean academicYearParam = s.getAcademicYear() != null; // && !s.getAcademicYear().trim().equals("---")
        Boolean sectionParam = s.getSection() != null;
        Boolean personParam = s.getPerson() != null;
        Boolean levelParam = s.getLevel() != null;
        Boolean whereSet = false;

        //************************************************************
        //Possibles where clauses 
        //************************************************************
        if (academicYearParam) {
            if (whereSet) {
                jpql += "AND ";
            } else {
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "o.startDate BETWEEN :startDateBegin AND :startDateEnd ";
        }
        if (sectionParam) {
            if (whereSet) {
                jpql += "AND ";
            } else {
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "o.ue.section.sectionId = :sectionId ";
        }
        if (personParam) {
            if (whereSet) {
                jpql += "AND ";
            } else {
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "o.person.personId = :personId ";
        }
        if (levelParam) {
            if (whereSet) {
                jpql += "AND ";
            } else {
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "o.level.levelId = :levelId ";
        }

        //*******************************
        //Order by
        //*******************************
        jpql += "ORDER BY o.organizedUeId";

        //*********************************
        //Query
        //*********************************
        Query query = em.createQuery(jpql);

        //************************************************************
        //Set parameter to the query 
        //************************************************************ 
        if (academicYearParam) {
            //convert academicYear in two dates
            ArrayList<Date> dateList;
            try {
                dateList = JsfUtil.convertAcademicYearToStartAndEndDate(s.getAcademicYear());
                query.setParameter("startDateBegin", dateList.get(0));
                query.setParameter("startDateEnd", dateList.get(1));
            } catch (ParseException ex) {
                Logger.getLogger(OrganizedUeFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }

        }
        if (sectionParam) {
            query.setParameter("sectionId", s.getSection().getSectionId());
        }
        if (personParam) {
            query.setParameter("personId", s.getPerson().getPersonId());
        }
        if (levelParam) {
            query.setParameter("levelId", s.getLevel().getLevelId());
        }

        //*******************************
        //Return results
        //*******************************
        return query.getResultList();
    }

    //************************************************+++++++++++++++++++++
    //Method search with filter from Object Search VM FOR STUDENT ORGA UE
    //************************************************++++++++++++++++++++++
    public List<OrganizedUe> findStudentOUeVM(OrganizedUeSearchVM s) {
        String jpql = "SELECT o.organizedUe FROM StudentOrganizedUe o ";

        Boolean personParam = s.getPerson() != null;
        Boolean whereSet = false;

        //************************************************************
        //Possibles where clauses 
        //************************************************************
        if (personParam) {
            if (whereSet) {
                jpql += "AND ";
            } else {
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "o.person.personId = :personId ";
        }

        //*********************************
        //Query
        //*********************************
        Query query = em.createQuery(jpql);

        //************************************************************
        //Set parameter to the query 
        //************************************************************ 
        if (personParam) {
            query.setParameter("personId", s.getPerson().getPersonId());
        }

        //*******************************
        //Return results
        //*******************************
        return query.getResultList();
    }

    //copy Organized Ue from organized ue for the next academic year
    //***************************************************************
    public List<OrganizedUe> copyOrganizedUes(OrganizedUeSearchVM s) throws ParseException {
        //------------------------------------
        //there is two conditions to copy an oUe
        // 1. there is a section
        // 2. there is an academic year
        //------------------------------------
        List<OrganizedUe> oUes;
        List<OrganizedUe> newOUes = new ArrayList();
        Boolean academicYearParam = s.getAcademicYear() != null && s.getAcademicYear().length() > 0;
        Boolean sectionParam = s.getSection() != null;

        if (academicYearParam && sectionParam) {
            //recover results
            oUes = this.findVM(s);

            //create organizedUes iterators for every ue of the list
            ListIterator<OrganizedUe> it = oUes.listIterator();
            ListIterator<OrganizedUe> itNew = newOUes.listIterator();

            //*******************************************************************************************************
            //In case if we want to recopy on an existing organization
            //we have to delete the organization before doing the copy again
            Boolean recopyFlag;
            Boolean organizationSuppressed = true;
            String nextAcademicYearString;

            if (s.getAcademicYearCopyTo() == null) {
                recopyFlag = false;
                nextAcademicYearString = s.getAcademicYear();
            } else {
                recopyFlag = true;
                nextAcademicYearString = s.getAcademicYearCopyTo();

                //check if we can delete the organisation (not current year + not planned)
                //first save the academcic Year of the searchVM
                String academicYearFromSearchVM = s.getAcademicYear();
                //change the academic Year parameter in the searc VM for the next methode to use
                s.setAcademicYear(s.getAcademicYearCopyTo());
                //use the delete methode
                organizationSuppressed = this.suppressOrganization(s);
                //reput the start parameter in academicYear SearchVM
                s.setAcademicYear(academicYearFromSearchVM);
            }

            //*******************************************************************************************************
            if (organizationSuppressed) {
                //***********************************************************
                //check if we can copy this organisation for the next academic year
                //we consider an academic year from 01-09 to 31-08.
                //we copy the organisation on the first free year.
                //***********************************************************
                //check wich academic year we want to copy
                //convert academicYear in two dates
                ArrayList<Date> dateList;
                dateList = JsfUtil.convertAcademicYearToStartAndEndDate(s.getAcademicYear());
                Date startAcademicYearToCopyDate = dateList.get(0);

                //create list
                List<OrganizedUe> oUeList = new ArrayList();
                Date finalStartDate = startAcademicYearToCopyDate;
                int d = 0;
                OrganizedUeSearchVM o = new OrganizedUeSearchVM();
                o.setSection(s.getSection());

                if (recopyFlag == false) {

                    //-----------------------------------------------------------------------------------------
                    //Loop: do while to find the next free academic year
                    //the next free academic year is the one where there is no organizedUe for this section
                    do {
                        oUeList.clear();
                        nextAcademicYearString = JsfUtil.nextAcademicYear(nextAcademicYearString);
                        o.setAcademicYear(nextAcademicYearString);
                        oUeList = this.findVM(o);

                    } while (!oUeList.isEmpty());
                    //-----------------------------------------------------------------------------------------
                }
                //At this moment we are in the first free academicYear
                //calculate nb of days to add
                Calendar c2 = Calendar.getInstance();
                c2.setTime(startAcademicYearToCopyDate);
                Integer firstYear = c2.get(Calendar.YEAR);
                //***
                ArrayList<Date> dateList2;
                dateList2 = JsfUtil.convertAcademicYearToStartAndEndDate(nextAcademicYearString);
                Date nextAcademicYear = dateList2.get(0);
                //***
                c2.setTime(nextAcademicYear);
                Integer lastYear = c2.get(Calendar.YEAR);
                Integer diff = lastYear - firstYear;

                //after this loop we know the number of day to add
                for (int i = 0; i < diff; i++) {
                    int dAdd = JsfUtil.calculateDaysToAddOneMoreYear(finalStartDate);
                    d += dAdd;
                    finalStartDate = JsfUtil.dateDaysIncrement(finalStartDate, dAdd);
                }

                //create the news organizedUes
                while (it.hasNext()) {
                    //iterator
                    OrganizedUe oUe = it.next();

                    //recover the two dates and add one year at each of them
                    //if we choose to copy every organized ue from this academic year to the next free one
                    //we have to increment the dates from one the number of day calculate upper in the code
                    Date startDateInDate = JsfUtil.dateDaysIncrement(oUe.getStartDate(), d);
                    Date endDateInDate = JsfUtil.dateDaysIncrement(oUe.getEndDate(), d);

                    //create
                    OrganizedUe organizedUe = new OrganizedUe(null, startDateInDate, endDateInDate, oUe.getLevel(), oUe.getUe(), oUe.getUe().getName() + " " + nextAcademicYearString);
                    this.create(organizedUe);
                    itNew.add(organizedUe);
                }
                //Success message
                System.out.println("***SuccessMessage: L'organisation a ete copiee avec succes!");
                JsfUtil.addSuccessMessage("***SuccessMessage: L'organisation a ete copiee avec succes!");
                
            }
            else{
                //error message
                System.out.println("***ErrorMessage: Vous ne pouvez-pas copier l'organisation sur l'annee scolaire souhaitee!");
                JsfUtil.addErrorMessage("***ErrorMessage: Vous ne pouvez-pas copier l'organisation sur l'annee scolaire souhaitee!");
            }
        }
        //return organizedUe list
        return newOUes;
    }

    //*********************************************************************
    //select the academic year when an organizedUe was organized
    //for the select one box warning: I cannot use findVM there 
    //because I need to only use sectionName as parameter in the search !!!
    //*********************************************************************
    public List<String> SelectAcademicYears(OrganizedUeSearchVM s) {
        // recover every value in the searchVM object
        Person person = s.getPerson();
        Level level = s.getLevel();
        String academicYear = s.getAcademicYear();

        //put all of this value to null because I only need to do a search with the section parameter
        s.setAcademicYear(null);
        s.setLevel(null);
        s.setPerson(null);

        //call the findVM method
        List<OrganizedUe> oUes = this.findVM(s);

        //chech the presents academic years and add it in the collection
        //I use a tree Set in order to keep only one instance of an academicYear
        TreeSet<String> academicYearsSet = new TreeSet<String>();
        ListIterator<OrganizedUe> it = oUes.listIterator();
        while (it.hasNext()) {
            academicYearsSet.add(JsfUtil.findAcademicYear(it.next().getStartDate()));
        }

        //convert the collection to list
        List<String> academicYearList = new ArrayList<String>(academicYearsSet);

        //reput every parameter in the searchVM object
        s.setAcademicYear(academicYear);
        s.setLevel(level);
        s.setPerson(person);

        //return
        return academicYearList;
    }

    //***************************************************************************************
    //Override the create methode:
    //in order to respect several rules:
    //According to the start date and the number of period, the end date should be realistic
    //***************************************************************************************
    @Override
    public void create(OrganizedUe oUe) {
        //calculate the end date
        //according to the fact that one session is 4 hours
        //the sessions are every weeks
        Integer numOfSessions = oUe.getUe().getNumOfPeriods() / 4;

        //if the start date become august add 7 days to push to the next week in september
        Calendar c = Calendar.getInstance();
        c.setTime(oUe.getStartDate());
        int startMonth = c.get(Calendar.MONTH);
        if (startMonth == 7) {
            oUe.setStartDate(JsfUtil.dateDaysIncrement(oUe.getStartDate(), 7));
        }

        //increment
        oUe.setEndDate(oUe.getStartDate());
        for (int i = 0; i < numOfSessions; i++) {           
            //carreful if the end date is over june
            c.setTime(JsfUtil.dateDaysIncrement(oUe.getEndDate(), 7));
            int endMonth = c.get(Calendar.MONTH);
            if (endMonth > 5 && endMonth < 8) {
                break;
            }
            oUe.setEndDate(JsfUtil.dateDaysIncrement(oUe.getEndDate(), 7));
        }

        //persist
        getEntityManager().persist(oUe);
    }

    //*******************************************
    //Override the Remove method:
    //Don't remove an OUe who is already planned
    //*******************************************
    @Override
    public void remove(OrganizedUe o) {
        try {
            //check if it's not the current year
            Date current = JsfUtil.getCurrentDate();
            String academicYearStr = JsfUtil.findAcademicYear(o.getStartDate());
            String currentAcademicYear = JsfUtil.findAcademicYear(current);

            //check if there is a planning on the oUe
            //start request
            String jpql = "SELECT p FROM Planning p WHERE p.organizedUe = :oUe";
            Query query = em.createQuery(jpql);
            query.setParameter("oUe", o);
            List<Planning> planning = query.getResultList();

            //add parameter
            if (!academicYearStr.equals(currentAcademicYear)) {
                if (planning.isEmpty()) {
                    getEntityManager().remove(getEntityManager().merge(o));
                }
            }
            
            //message
            JsfUtil.addSuccessMessage("L'UE organisé a été supprimé avec succès!");
        } catch (ParseException ex) {
            Logger.getLogger(OrganizedUeFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    //********************************************************
    //Method to find the levels linked to an organizedUe
    //********************************************************
    public List<Level> findLevelByOrganizedUe(OrganizedUeSearchVM s) {
        //start the request
        String jpql = "SELECT o.level FROM OrganizedUe o ";
        List<Level> levelList = new ArrayList<Level>();
        Boolean sectionParam = false;
        Boolean academicYearParam = false;
        Boolean whereSet = false;
        //check if there is the parameter
        if (s != null) {
            sectionParam = s.getSection() != null;
            academicYearParam = s.getAcademicYear() != null;
        }
         if (sectionParam) {
            if (whereSet) {
                jpql += "AND ";
            } else {
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "o.ue.section.sectionId = :sectionId ";
        }
        if (academicYearParam) {
            if (whereSet) {
                jpql += "AND ";
            } else {
                jpql += "WHERE ";
                whereSet = true;
            }
            jpql += "o.startDate BETWEEN :startDateBegin AND :startDateEnd ";
        }

        //Query
        Query query = em.createQuery(jpql);

        //Set parameter to the query        
        if (sectionParam) {
            query.setParameter("sectionId", s.getSection().getSectionId());
        }
        if (academicYearParam) {
            //convert academicYear in two dates
            ArrayList<Date> dateList;
            try {
                dateList = JsfUtil.convertAcademicYearToStartAndEndDate(s.getAcademicYear());
                query.setParameter("startDateBegin", dateList.get(0));
                query.setParameter("startDateEnd", dateList.get(1));
            } catch (ParseException ex) {
                Logger.getLogger(OrganizedUeFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }

        }
        //Order by
        jpql += "ORDER BY o.level.name";

        //return
        //execute the query
        List<Level> levels = query.getResultList();

        //chech the presents levels and add it in the collection
        TreeSet<Level> levelsSet = new TreeSet<Level>();
        ListIterator<Level> it = levels.listIterator();
        while (it.hasNext()) {
            Level l = it.next();
            if (l == null) {
                continue;
            }
            levelsSet.add(l);
        }

        //convert the collection to list
        levelList = new ArrayList<Level>(levelsSet);

        return levelList;
    }

    public void registrationPersonInOrganizedUe(OrganizedUeSearchVM s, Person p){
        List<OrganizedUe> oUes = this.findVM(s);
        //for each oUe in the list create a line in the student_organized_ue table
        //with the person parameter
        for (OrganizedUe oUe : oUes) {
            //create the pk
            StudentOrganizedUePK pk = new StudentOrganizedUePK();
            pk.setOrganizedUeId(oUe.getId());
            pk.setPersonId(p.getId());
            //create the StudentOrganizedUe
            StudentOrganizedUe sOUe = new StudentOrganizedUe();
            sOUe.setStudentOrganizedUePK(pk);
            sOUe.setOrganizedUe(oUe);
            sOUe.setPerson(p);
            sOUe.setComment("dd");

            //create
            sOUeFacade.create(sOUe);
        }       
    }



    //check if we are on the current academicYear and if the organizedUe is already planned
    public Boolean CanISuppressOrganizedUe(OrganizedUe o) {
        Boolean allowed = false;
        try {
            //check if it's not the current year
            Date current = JsfUtil.getCurrentDate();
            String academicYearStr = JsfUtil.findAcademicYear(o.getStartDate());
            String currentAcademicYear = JsfUtil.findAcademicYear(current);

            //check if there is a planning on the oUe
            //start request
            String jpql = "SELECT p FROM Planning p WHERE p.organizedUe = :oUe";
            Query query = em.createQuery(jpql);
            query.setParameter("oUe", o);
            List<Planning> planning = query.getResultList();

            //add parameter
            if (!academicYearStr.equals(currentAcademicYear)) {
                if (planning.isEmpty()) {
                    allowed = true;
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(OrganizedUeFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        return allowed;
    }

    //suppress an organisation
    public Boolean suppressOrganization(OrganizedUeSearchVM s) {
        //check if I can suppress the orhganization.
        List<OrganizedUe> organizedUes = this.findVM(s);
        ListIterator<OrganizedUe> it = organizedUes.listIterator();

        //check if we there is a planning
        Boolean allowed = true;

        while (it.hasNext()) {
            allowed = this.CanISuppressOrganizedUe(it.next());
            if (allowed == false) {
                break;
            }
        }

        //reset the iterator
        it = organizedUes.listIterator();

        //if it's allowed we suppress
        if (allowed == true) {
            while (it.hasNext()) {
                OrganizedUe o = it.next();
                this.remove(o);
            }
            System.out.println("***SuccessMessage: L'organisation a été supprimée avec succès!");
            JsfUtil.addSuccessMessage("***SuccessMessage: L'organisation a été supprimée avec succès!");
            return true;
        } //else
        else {
            System.out.println("***ErrorMessage: Vous ne pouvez-pas supprimer cette organisation, elle est déjà plannifiée ou fait partie de l'année courante!");
            JsfUtil.addErrorMessage("***ErrorMessage: Vous ne pouvez-pas supprimer cette organisation, elle est déjà plannifiée ou fait partie de l'année courante!");
            return false;
        }

    }

}
