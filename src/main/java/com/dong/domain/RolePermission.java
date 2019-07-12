package com.dong.domain;

import java.io.Serializable;

public class RolePermission implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long rpId;
	private long roldId;
	private long permissionId;

	public long getRpId() {
		return rpId;
	}

	public void setRpId(long rpId) {
		this.rpId = rpId;
	}

	public long getRoldId() {
		return roldId;
	}

	public void setRoldId(long roldId) {
		this.roldId = roldId;
	}

	public long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(long permissionId) {
		this.permissionId = permissionId;
	}

}
