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
 */
public interface IPageResult<T>{
    int getPageCount();
    int getTotalItemCount();
    int getCurrentItemsCount();
    int getCurrentPageNumber();
    int getPageSize();
    boolean isLastPage();
    boolean isFirstPage();
    T getFirstElement();
    T getLastElement();
    T getElement(int index);
    List<T> getElements();
    IFilter getRequestFilter();
    int getFromElement();
    int getToElement();
    List<OrderParam> getOrderParams();
    Map<String,QueryCondition> getConditions();
}
