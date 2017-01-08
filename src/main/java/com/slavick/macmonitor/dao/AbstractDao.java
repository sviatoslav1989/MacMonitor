package com.slavick.macmonitor.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractDao<PK extends Serializable, T> {

	public static int BATCH_SIZE=100;
	
	private final Class<T> persistentClass;
	
	@SuppressWarnings("unchecked")
	public AbstractDao(){
		this.persistentClass =(Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}
	
	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession(){
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public T getById(PK id) {
		return (T) getSession().get(persistentClass, id);
	}



	public void persist(T entity) {
		getSession().persist(entity);
	}

	public void persistBatch(Collection<T> entities){
		Session session = getSession();

		int i=0;
		Iterator<T> iterator = entities.iterator();

		while(iterator.hasNext()) {
			session.persist(iterator.next());
			i++;
			if (i % BATCH_SIZE == 0) {
				session.flush();
				session.clear();
			}
		}

	}

	public void updateBatch(Collection<T> entities){
		Session session = getSession();

		int i=0;
		Iterator<T> iterator = entities.iterator();


		while(iterator.hasNext()) {
			session.update(iterator.next());
			i++;
			if (i % BATCH_SIZE == 0) {
				session.flush();
				session.clear();
			}
		}

	}

	public void update(T entity) {

		getSession().update(entity);
	}

	public void delete(T entity) {
		getSession().delete(entity);
	}

	public void deleteById(PK id) {
		T entity = getById(id);
		if (entity != null)
			delete(entity);
	}

	public List<T> getAll(){
		Criteria crit = createEntityCriteria();
		return crit.list();
	}

	public List<T> findByCriteria(Criteria criteria){
		return criteria.list();
	}

	protected Criteria createEntityCriteria(){
		return getSession().createCriteria(persistentClass);
	}

}
