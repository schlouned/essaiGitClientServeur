/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.business;

import be.isl.ue.entity.Evaluation;
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
public class EvaluationFacade extends AbstractFacade<Evaluation, FalseSearchVM> {

    @PersistenceContext(unitName = "ue_pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EvaluationFacade() {
        super(Evaluation.class);
    }
    
    //false findVM method
    public List <Evaluation> findVM(FalseSearchVM s){
        List <Evaluation> list = new ArrayList();
        return list;
    }
    
}
