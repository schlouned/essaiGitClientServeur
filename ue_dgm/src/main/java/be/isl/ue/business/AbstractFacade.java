/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.business;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/******************************************************
 *Auto genereted abstract class for Facades
 * 
 * @author Schloune Denis
 ******************************************************/
public abstract class AbstractFacade<T, U> {
    //***********************************
    //member
    //***********************************
    private Class<T> entityClass;
    
    //***********************************
    //constructor
    //***********************************
    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    //***********************************
    //get entity manager
    //***********************************
    protected abstract EntityManager getEntityManager();

    //***********************************
    //findVM method
    //***********************************
     public abstract List<T> findVM(U s);
     
    //***********************************
    //Create method
    //***********************************
    public void create(T entity) {
        getEntityManager().persist(entity);
    }
    
    //***********************************
    //Edit method
    //***********************************
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    //***********************************
    //Remove method
    //***********************************
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }
    
    //***********************************
    //Find by id method
    //***********************************
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    //***********************************
    //FindAll method
    //***********************************
    public List<T> findAll2() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }
    // new FindAll method
    public List<T> findAll() {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> from = criteriaQuery.from(entityClass);
        CriteriaQuery<T> select = criteriaQuery.select(from);
        criteriaQuery.orderBy(criteriaBuilder.asc(from.get("name")));
        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }
    
    //***********************************
    //Findrange method
    //***********************************
    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    //***********************************
    //Count method
    //***********************************
    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    //***********************************
    //Refresh method
    //***********************************
    public void refresh(T entity){
        getEntityManager().refresh(entity);
    }
    
}
