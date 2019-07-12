package com.dong.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义分页对象
 * 
 * @author xiedongxiao
 *
 * @param <T>
 */
public class MyPage<T> {

	private List<T> content = new ArrayList<T>();
	private int total;

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
