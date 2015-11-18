/**
 * 
 */
package com.kuaizhan.form.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对web的表单查询参数的简单封装，包函查询条件、排序条件、翻页页码、页容量
 */
@Data
public class QueryParameters {
	public static final int ALL_PAGE_SIZE = 0;//查询所有，不翻页
	private Map<String, Object> parameters = new HashMap<String,Object>();//查询条件
	private List<Order> orders = new ArrayList<Order>();//排序条件
	private int pageNo = 1;//页码
	private int pageSize = -1;//页容量
	
	
	public int getOffset(){
		if(pageSize  == -1){
			return 0;
		}
		return  (pageNo - 1) * pageSize;
	}
	

	/**
	 * 设置查询条件
	 * @param parameters 封装查询条件的Map
	 */
	protected void setParameters(Map<String, Object> parameters) {
		if (parameters != null) {
			this.parameters = new HashMap<String,Object>(parameters);
		} else {
			this.parameters  = new HashMap<String,Object>();
		}
	}


	/**
	 * 添加查询条件参数
	 * @param name 名称，表单字段名称
	 * @param value 值，表单字段值
	 */
	public void addParameter(String name, Object value) {
		parameters.put(name, value);
	}
	
	public void removeParameter(String name) {
		parameters.remove(name);
	}
	
	public Object getParameter(String name) {
		return parameters.get(name);
	}
	
	/**
	 * 转成前缀匹配
	 * @param name
	 */
	public void toPrefixParameter(String name) {
		Object o = getParameter(name);
		if (o == null) {
			return;
		}
		if (o instanceof String) {
			addParameter(name, (String)o + "%");
		} else {
			throw new UnsupportedOperationException();
		}
	}
	
	/**
	 * 转成后缀匹配
	 * @param name
	 */
	public void toPostfixParameter(String name) {
		Object o = getParameter(name);
		if (o == null) {
			return;
		}
		if (o instanceof String) {
			addParameter(name, "%" + (String)o);
		} else {
			throw new UnsupportedOperationException();
		}
	}
	
	/**
	 * 转成前后缀匹配
	 * @param name
	 */
	public void toArountParameter(String name) {
		Object o = getParameter(name);
		if (o == null) {
			return;
		}
		if (o instanceof String) {
			addParameter(name, "%" + (String)o + "%");
		} else {
			throw new UnsupportedOperationException();
		}
	}
	
	/**
	 * 添加排序条件参数
	 * @param fieldName 字段名，通常要与Hibernate Bean属性名一致
	 * @param ascend  true 升序 : false 降序
	 */
	public void addOrder(String fieldName, boolean ascend) {
		if(fieldName == null){
			throw new IllegalArgumentException();
		}
		if(ascend){
			orders.add(new Order(fieldName, Order.ASC));
		}else{
			orders.add(new Order(fieldName, Order.DESC));
		}
	}
	

	
	/**
	 * 排序对象，描述数据库字段的排序
	 */
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public class Order {
		public static final String ASC = "asc";//升序排列
		public static final String DESC = "desc";//降序排列
		
		private String fieldName;
		private String type;

		Order(String fileName) {
			this(fileName, ASC);
		}

	}
}
