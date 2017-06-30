package com.celio.entity;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class TreeNode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String code;
	
	@Column(nullable = false)
	private String description;

	@Column
	private String observation;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private TreeNode parent;
	
	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "parent")
	private Collection<TreeNode> children;

	public TreeNode() {
		super();
	}

	public TreeNode(Long id, String code, String description, String observation, TreeNode parent,
			Collection<TreeNode> children) {
		super();
		this.id = id;
		this.code = code;
		this.description = description;
		this.observation = observation;
		this.parent = parent;
		this.children = children;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public Collection<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(Collection<TreeNode> children) {
		this.children = children;
	}
	
}
