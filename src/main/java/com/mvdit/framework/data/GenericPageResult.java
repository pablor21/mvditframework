/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.data;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Pablo Ramírez
 * @param <T>
 */
public class GenericPageResult<T> implements IPageResult<T> {

   
    protected List<T> elements;
    protected GenericFilter requestFilter;
    protected int pageSize;
    protected int pageCount;
    protected int totalItemCount;
    protected int currentPageNumber;
    protected List<OrderParam> orderParams;
    protected Map<String, QueryCondition> conditions;
    
    /**
     * ATENCIÓN: No usar este constructor, sólo es usado para parsear por los ws
     */
    public GenericPageResult(){
        
    }

    public GenericPageResult(List<T> elements, IFilter filter, Map<String,QueryCondition> conditions, List<OrderParam> orderParams, int totalItemCount) {
        this.elements = elements;
        this.orderParams= orderParams;
        this.conditions= conditions;
        this.requestFilter = (GenericFilter) filter;
        this.pageSize = filter.getPageSize();
        this.totalItemCount = totalItemCount;
        this.currentPageNumber = filter.getPageNumber();
        if (this.currentPageNumber < 1 || this.currentPageNumber > getPageCount()) {
            //throw new MvditRuntimeException(new IndexOutOfBoundsException("The current page number is invalid"));
            if(this.currentPageNumber<1){
                this.currentPageNumber=1;
            }else{
                this.currentPageNumber=getPageCount();
            }
        }
    }

    @Override
    public int getPageCount() {
        if (this.totalItemCount > -1 && this.pageSize > 0) {
            return (int) Math.ceil(this.totalItemCount / (double) this.pageSize);
        }
        return 1;
    }

    @Override
    public int getTotalItemCount() {
        return this.totalItemCount;
    }

    @Override
    public int getCurrentPageNumber() {
        return this.currentPageNumber;
    }

    @Override
    public int getPageSize() {
        return this.pageSize;
    }

    @Override
    public boolean isLastPage() {
        return this.pageSize >= this.getPageCount();
    }

    @Override
    public boolean isFirstPage() {
        return this.currentPageNumber <= 1;
    }

    @Override
    public T getFirstElement() {
        if (this.elements != null && this.elements.size() > 0) {
            return this.elements.get(0);
        }
        return null;
    }

    @Override
    public T getLastElement() {
        if (this.elements != null && this.elements.size() > 0) {
            return this.elements.get(this.elements.size() - 1);
        }
        return null;
    }

    @Override
    public T getElement(int index) {
        if (this.elements != null && this.elements.size() > index && index >= 0) {
            return this.elements.get(this.elements.size() - 1);
        }
        return null;
    }

    @Override
    public List<T> getElements() {
        return this.elements;
    }

    @Override
    public IFilter getRequestFilter() {
        return this.requestFilter;
    }

    @Override
    public int getCurrentItemsCount() {
        if (this.elements != null) {
            return this.elements.size();
        }
        return 0;
    }

    @Override
    public int getFromElement() {
        if (this.elements != null) {
            return (this.getCurrentPageNumber() - 1) * this.pageSize;
        }
        return 0;
    }

    @Override
    public int getToElement() {
        if (this.elements != null) {
            return (this.getFromElement() + this.elements.size() - 1);
        }
        return 0;
    }

    @Override
    public List<OrderParam> getOrderParams() {
        return this.orderParams;
    }

    @Override
    public Map<String,QueryCondition> getConditions() {
        return this.conditions;
    }

}
