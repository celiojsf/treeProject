package com.celio.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet(urlPatterns = "/startup", loadOnStartup = 1)
public class TreeEntityManagerFactory extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	private static EntityManagerFactory entityManagerFactory;

	private static EntityManagerFactory buildFactory() {
		return Persistence.createEntityManagerFactory("org.hibernate.tree");
	}
	
	public static EntityManager getEntityManager() {
		return entityManagerFactory.createEntityManager();
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		TreeEntityManagerFactory.entityManagerFactory = TreeEntityManagerFactory.buildFactory();
	}
		
}
