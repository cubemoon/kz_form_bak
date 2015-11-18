/*
 * 创建日期 2005-12-29
 * 林良益 @caripower
 */
package com.kuaizhan.form.view;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.mongodb.morphia.annotations.Property;

/**
 * 
 * 基于List的数据分页对象
 */

@Data
@NoArgsConstructor
public class ListPage<T>{


	public static final ListPage<Object> EMPTY_PAGE = new ListPage<Object>();
	
    //当前页页码
	@Property("current_page_no")
    private int currentPageNo;
    //页面记录数，等于0时显示所有
	@Property("current_page_size")
    private int currentPageSize;
    //总记录数
	@Property("total_count")
    private long totalCount;
    //当前页记录列表
	@Property("data_list")
    private List<T> dataList;

    
    public long getTotalPageCount(){
    	if (currentPageSize == 0) {
    		return 1;
    	}
     	return (totalCount - 1) / currentPageSize + 1 ;
    }
    
    /**
     * 是否有下一页
     * @return 是否有下一页
     */
    public boolean hasNextPage() {
      return (currentPageNo < this.getTotalPageCount());
    }

    /**
     * 是否有上一页
     * @return  是否有上一页
     */
    public boolean hasPreviousPage() {
      return (currentPageNo > 1);
    }
    
    /**
     * 判断是否为空页
     * @return 是否为空页
     */
    public boolean isEmpty(){
        return totalCount == 0;
    }
    
}
