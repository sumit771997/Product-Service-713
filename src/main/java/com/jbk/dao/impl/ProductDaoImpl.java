package com.jbk.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaInsertSelect;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.Projection;
import org.springframework.stereotype.Repository;
import com.jbk.dao.ProductDao;
import com.jbk.entity.ProductEntity;
import com.jbk.exception.ResourceAlreadyExistsException;
import com.jbk.exception.ResourceNotExistsException;
import com.jbk.exception.SomethingWentWrongException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.RollbackException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;



@Repository
public class ProductDaoImpl implements ProductDao 
{

	@Autowired
	private SessionFactory sessionFactory;
	
	 @Autowired
	    private EntityManager entityManager;

	@Override
	public boolean addProduct(ProductEntity productEntity) {
		boolean isAdded=false;
		
		try(Session session = sessionFactory.openSession()) {
			Transaction transaction;
			
			// check its exist or not
			//ProductEntity dbEntity = getProductByName(productEntity.getProductName());
			ProductEntity dbEntity = getProductById(productEntity.getProductId());
			
			if(dbEntity==null){
				session.save(productEntity);
				transaction = session.beginTransaction();
				transaction.commit();
				isAdded= true;
			}
			else {
				throw new ResourceAlreadyExistsException("Product already exist with ID= "+productEntity.getProductId());
				
			}			
			
		} catch (RollbackException e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something went wrong in during add product, check unique fields");
		}
		
		return isAdded;
	}

	@Override
	public ProductEntity getProductById(long productId) {
		ProductEntity productEntity=null;
		try {
			
			Session session = sessionFactory.openSession();
			
			productEntity = session.get(ProductEntity.class, productId);
			
						
		} catch (HibernateException e) {
			throw new SomethingWentWrongException("Connection Failure");
		}
		return productEntity;
	}

	@Override
	public boolean deleteProductById(long productId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateProduct(ProductEntity productEntity) {

		boolean isUpdated=false;
		
		try {
			Session session = sessionFactory.openSession();
			
			ProductEntity dbProduct = getProductById(productEntity.getProductId());
			
			if(dbProduct!=null)
			{
				session.update(productEntity);
				session.beginTransaction().commit();
				isUpdated= true;
			}else {
				isUpdated=false;
//				throw new ResourceNotExistsException("Product not exist with ID= "+productEntity.getProductId());
			}
			
		} catch (Exception e) {
			throw new SomethingWentWrongException("Something went wrong during update product");
			
		}
		return isUpdated;
	}

	@Override
	public List<ProductEntity> getAllProducts() {
		List<ProductEntity> list;
		
		try {
			Session session = sessionFactory.openSession();
			// criteria
			
			 JpaCriteriaQuery<ProductEntity> query = session.getCriteriaBuilder().createQuery(ProductEntity.class);
			 query.from(ProductEntity.class);
			 list = session.createQuery(query).getResultList();
			 			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something went wrong during retrieve all products");
		}
		return list;
	}

	@Override
	public List<ProductEntity> sortProduct(String orderType, String property) {
		List<ProductEntity> list = null;
		try {
			Session session = sessionFactory.openSession();
			CriteriaBuilder criteria = session.getCriteriaBuilder();
			CriteriaQuery<ProductEntity> query = criteria.createQuery(ProductEntity.class);
			Root<ProductEntity> root = query.from(ProductEntity.class);

			Order orderBy;
			if (orderType.equalsIgnoreCase("desc")) {
				orderBy = criteria.desc(root.get(property));
			} else {
				orderBy = criteria.asc(root.get(property));
			}
			query.orderBy(orderBy);
			list = entityManager.createQuery(query).getResultList();
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	@Override
	public double getMaxProductPrice() {
		double maxPrice=0;
		
		try {
			Session session = sessionFactory.openSession();

			CriteriaBuilder criteria = session.getCriteriaBuilder();
			
			CriteriaQuery<Double> criteriaQuery = criteria.createQuery(Double.class);
			
			Root<ProductEntity> root = criteriaQuery.from(ProductEntity.class);
			
			criteriaQuery.select(criteria.max(root.get("productPrice")));
			
			Query<Double> query = session.createQuery(criteriaQuery);
			
			maxPrice = query.getSingleResult();

			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotExistsException("Product not found");
		}
		return maxPrice;
	}
	
	@Override
	public List<ProductEntity> getMaxPriceProduct() {
		double maxProductPrice=getMaxProductPrice();
		List<ProductEntity> list=null;
		
		if(maxProductPrice>0)
		{
			Session session = sessionFactory.openSession();
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<ProductEntity> criteriaQuery = criteriaBuilder.createQuery(ProductEntity.class);
			Root<ProductEntity> root = criteriaQuery.from(ProductEntity.class);
			CriteriaQuery<ProductEntity> query = criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("productPrice"), maxProductPrice));
			list = session.createQuery(query).getResultList();
		}
		else
		{
			throw new ResourceNotExistsException("Product not exist");
		}
		
		return list ;
	}

	@Override
	public ProductEntity getProductByName(String productName) {
		List<ProductEntity> list=null;
		ProductEntity productEntity=null;

		try(Session session = sessionFactory.openSession()) {
			
			Query<ProductEntity> query = session.createQuery("FROM ProductEntity WHERE productName=:name");
			query.setParameter("name", productName);
			list = query.list();
			
			if(!list.isEmpty())
			{
				productEntity = list.get(0);
			}else
			{
				return null;

			}
		} catch (Exception e) {
			e.printStackTrace();
			 throw new ResourceNotExistsException("Product Not Exists");

		}
		return productEntity;
	}

	@Override
	public List<ProductEntity> getAllProducts(double low, double high) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductEntity> getProductStartWith(String expression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double productPriceAverage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double countOfTotalProducts() {
		// TODO Auto-generated method stub
		return 0;
	}
	


}
