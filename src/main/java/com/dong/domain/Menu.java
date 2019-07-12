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
	/** 资源url **/
	private String url;
	/** 权限说明 **/
	private String description;
	// 子菜单
	private List<Menu> childs = new ArrayList<Menu>();
	// 父节点
	private long pId;
	// 节点高度
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
