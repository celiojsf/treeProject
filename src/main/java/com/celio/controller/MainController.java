package com.celio.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.celio.database.TreeEntityManagerFactory;
import com.celio.entity.TreeNode;

@Path("/treeNode")
public class MainController {
	
	@GET
	@Path("/items")
	@Produces(MediaType.APPLICATION_JSON)
	@SuppressWarnings("unchecked")
	public List<TreeNode> loadItems() {
		EntityManager em = TreeEntityManagerFactory.getEntityManager();
		Query query = em.createQuery("from TreeNode WHERE parent is null");
		List<TreeNode> items = query.getResultList();
		em.close();
		clearParentFromItems(items);
		return items;
	}
	
	private void clearParentFromItems(List<TreeNode> items) {
		for (TreeNode treeNode : items) {
			treeNode.setParent(null);
			if (treeNode.getChildren() != null) {
				clearParentFromItems((List<TreeNode>) treeNode.getChildren());
			}
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Boolean saveItem(TreeNode item) {
		return persistItem(item, TreeEntityManagerFactory.getEntityManager());
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Boolean updateItem(TreeNode item) {
		EntityManager em = TreeEntityManagerFactory.getEntityManager();
		Query query = em.createQuery("from TreeNode WHERE id = :id");
		query.setParameter("id", item.getId());
		TreeNode dbItem = (TreeNode) query.getSingleResult();
		dbItem.setCode(item.getCode());
		dbItem.setDescription(item.getDescription());
		dbItem.setObservation(item.getObservation());
		return persistItem(dbItem, em);
	}

	private Boolean persistItem(TreeNode item, EntityManager em) {
		try {
			em.getTransaction().begin();
			em.persist(item);
			em.getTransaction().commit();
			em.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@DELETE
	@Path("/{itemId}")
	public Boolean removeItem(@PathParam("itemId") Long itemId) {
		EntityManager em = TreeEntityManagerFactory.getEntityManager();
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("DELETE FROM TreeNode WHERE id = :itemId");
			query.setParameter("itemId", itemId);
			query.executeUpdate();
			Query queryChilds = em.createQuery("DELETE FROM TreeNode WHERE parent.id = :itemId");
			queryChilds.setParameter("itemId", itemId);
			queryChilds.executeUpdate();
			em.getTransaction().commit();
			em.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}
