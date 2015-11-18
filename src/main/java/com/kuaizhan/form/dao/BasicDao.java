package com.kuaizhan.form.dao;

import java.util.List;

import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import com.kuaizhan.form.view.ListPage;
import com.kuaizhan.form.view.QueryParameters;
import com.kuaizhan.form.view.QueryParameters.Order;

public class BasicDao<T, K> extends BasicDAO<T, K> {

	public BasicDao(Class<T> entityClass) {
		super(entityClass, DaoHelper.getDatastore());
	}

	public ListPage<T> queryListPage(Query<T> query, QueryParameters queryParams) {
		
    	if (query == null || queryParams == null) {
    		throw new IllegalArgumentException();
    	}
		
    	StringBuffer orderString = new StringBuffer();
        for(int i=0; i<queryParams.getOrders().size(); i++){
        	Order order = queryParams.getOrders().get(i);
        	orderString.append(order.getType().equals(Order.ASC) ? "" : "-");
        	orderString.append(order.getFieldName());
        	if(i<(queryParams.getOrders().size() -1)){
        		orderString.append(",");
        	}
        }
        if(!queryParams.getOrders().isEmpty()){
        	query.order(orderString.toString());
        }
        
		
        long totalCount = query.countAll();
        List<T> data = query.offset(queryParams.getOffset()).limit(queryParams.getPageSize()).asList();
        
    	ListPage<T> listPage = new ListPage<T>();
      	listPage.setTotalCount(totalCount);
      	listPage.setDataList(data);
        listPage.setCurrentPageNo(queryParams.getPageNo());
      	listPage.setCurrentPageSize(queryParams.getPageSize());
        return listPage;
    }
}
