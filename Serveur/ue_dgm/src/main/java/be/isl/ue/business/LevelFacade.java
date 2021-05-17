/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.business;


import be.isl.ue.entity.Level;
import be.isl.ue.vm.FalseSearchVM;
import be.isl.ue.vm.LevelSearchVM;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Schloune Denis
 */
@Stateless
public class LevelFacade extends AbstractFacade<Level, LevelSearchVM> {

    @PersistenceContext(unitName = "ue_pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LevelFacade() {
        super(Level.class);
    }
    
    //findVM method
    public List <Level> findVM(LevelSearchVM s){
        String jpql = "SELECT l FROM Level l ORDER BY l.name";
        Query query = em.createQuery(jpql);
        return query.getResultList();
    }
    
}
