package com.dong.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Menu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long menuId;
	/** ��Դurl **/
	private String url;
	/** Ȩ��˵�� **/
	private String description;
	// �Ӳ˵�
	private List<Menu> childs = new ArrayList<Menu>();
	// ���ڵ�
	private long pId;
	// �ڵ�߶�
	private int height;

	public Menu(Permission p) {
		this.menuId = p.getPermissionId();
		this.url = p.getUrl();
		this.description = p.getDescription();
		this.pId = p.getpId();
		this.height = p.getHeight();
	}

	public long getMenuId() {
		return menuId;
	}

	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Menu> getChilds() {
		return childs;
	}

	public void setChilds(List<Menu> childs) {
		this.childs = childs;
	}

	public long getpId() {
		return pId;
	}

	public void setpId(long pId) {
		this.pId = pId;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
