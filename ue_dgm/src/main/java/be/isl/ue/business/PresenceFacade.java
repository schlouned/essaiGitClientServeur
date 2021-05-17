/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.business;

import be.isl.ue.entity.Level;
import be.isl.ue.entity.OrganizedUe;
import be.isl.ue.vm.PresenceSearchVM;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.TreeSet;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.logging.Logger;


@Stateless
public class PresenceFacade extends AbstractFacade<OrganizedUe, PresenceSearchVM> {

    @PersistenceContext(unitName = "ue_pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PresenceFacade() {
        super(OrganizedUe.class);
    }
    

    @Override
    public List<OrganizedUe> findVM(PresenceSearchVM s) {
        String jpql = "SELECT o FROM OrganizedUe o ";
        Boolean schoolYearParam = s.getSchoolYear() != null; 
        Boolean sectionParam = s.getSection() != null;
        Boolean levelParam = s.getLevel() != null;
        Date startSchoolYear = null;
        Date endSchoolYear = null;
        
        Boolean whereSet = false;

        //************************************************************
        //Possibles where clauses 
        //************************************************************
        if (schoolYearParam) {
            if (whereSet) {
                jpql += "AND ";
            } else {
                jpql += "WHERE ";
                whereSet = true;
            }
                      
            jpql += "o.startDate BETWEEN :startSchoolYear AND :endSchoolYear ";
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
        if (schoolYearParam) {

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            
            String[] dateSplit = s.getSchoolYear().split(" - ");   
            
            try {
                startSchoolYear = df.parse(dateSplit[0] + "-08-31");
            } catch (ParseException ex) {
                Logger.getLogger(PresenceFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            try {
                endSchoolYear= df.parse(dateSplit[1] + "-07-01");
            } catch (ParseException ex) {
                Logger.getLogger(PresenceFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
               
            query.setParameter("startSchoolYear", startSchoolYear );
            query.setParameter("endSchoolYear", endSchoolYear);

        }
        if (sectionParam) {
            query.setParameter("sectionId", s.getSection().getSectionId());
        }
        if (levelParam) {
            query.setParameter("levelId", s.getLevel().getLevelId());
        }

        //*******************************
        //Return results
        //*******************************
        return query.getResultList();
    }


     public List<Level> findLevelOue(PresenceSearchVM s) {

        String jpql = "SELECT o.level FROM OrganizedUe o ";
        List<Level> levelList = new ArrayList<Level>();
        Boolean sectionParam = false;
        Boolean schoolYearParam = false; 
        Boolean whereSet = false;
        
        Date startSchoolYear = null;
        Date endSchoolYear = null;
        
        if (s != null) {
            sectionParam = s.getSection() != null;
            schoolYearParam = s.getSchoolYear()!= null;
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
        if (schoolYearParam) {
            if (whereSet) {
                jpql += "AND ";
            } else {
                jpql += "WHERE ";
                whereSet = true;
            }
                      
            jpql += "o.startDate BETWEEN :startSchoolYear AND :endSchoolYear ";
        }

        Query query = em.createQuery(jpql);

        if (sectionParam) {
            query.setParameter("sectionId", s.getSection().getSectionId());
        }
        if (schoolYearParam) {

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            
            String[] dateSplit = s.getSchoolYear().split(" - ");   
            
            try {
                startSchoolYear = df.parse(dateSplit[0] + "-08-31");
            } catch (ParseException ex) {
                Logger.getLogger(PresenceFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            try {
                endSchoolYear= df.parse(dateSplit[1] + "-07-01");
            } catch (ParseException ex) {
                Logger.getLogger(PresenceFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
               
            query.setParameter("startSchoolYear", startSchoolYear );
            query.setParameter("endSchoolYear", endSchoolYear);

        }
        
        jpql += "ORDER BY o.level.name";

        List<Level> levels = query.getResultList();

        TreeSet<Level> levelsSet = new TreeSet<Level>();
        ListIterator<Level> it = levels.listIterator();
        while (it.hasNext()) {
            Level l = it.next();
            if (l == null) {
                continue;
            }
            levelsSet.add(l);
        }

        levelList = new ArrayList<Level>(levelsSet);

        return levelList;
    }
    
}
