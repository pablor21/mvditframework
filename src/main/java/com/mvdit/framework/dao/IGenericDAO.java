/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.dao;

import com.mvdit.framework.data.IFilter;
import com.mvdit.framework.data.IPageResult;
import com.mvdit.framework.database.ITransaction;

/**
 *
 * @author Pablo Ramírez
 * @param <T>
 * @param <K>
 */
public interface IGenericDAO<T, K> extends ITransaction{


    /**
     * Crea un nuevo elemento
     * @param entity
     * @return El elemento creado
     */
    T create(T entity);

    /**
     * Actualiza un elemento
     * @param entity
     * @return El elemento modificado
     */
    T update(T entity);

    /**
     * Elimina un elemento
     * @param entity
     * @return la cantidad de elementos eliminados
     */
    int delete(T entity);

    /**
     * Lista elementos
     * @param filter
     * @return 
     */
    IPageResult list(IFilter filter);

    /**
     * Busca un elemento por id
     * @param id
     * @return 
     */
    T getById(K id);

    /**
     * 
     * @param filter
     * @return 
     */
    int count(IFilter filter);
}
