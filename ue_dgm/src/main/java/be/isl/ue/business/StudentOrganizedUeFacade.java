/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.business;

import be.isl.ue.entity.Presence;
import be.isl.ue.entity.StudentOrganizedUe;
import be.isl.ue.vm.FalseSearchVM;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Schloune Denis
 */
@Stateless
public class StudentOrganizedUeFacade extends AbstractFacade<StudentOrganizedUe, FalseSearchVM> {

    @PersistenceContext(unitName = "ue_pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StudentOrganizedUeFacade() {
        super(StudentOrganizedUe.class);
    }
    
    //findVM method
    public List <StudentOrganizedUe> findVM(FalseSearchVM s){
        List <StudentOrganizedUe> list = new ArrayList();
        return list;
    }
    //***********************************
    //Create method
    //***********************************
    public void create(StudentOrganizedUe entity) {
        getEntityManager().persist(entity);
    }
    
}
