/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mvdit.framework.services;

import com.mvdit.framework.core.MvditRuntimeException;
import com.mvdit.framework.core.MvditUtils;
import com.mvdit.framework.database.IDBEntity;
import java.math.BigInteger;

/**
 *
 * @author Pablo Ramírez
 */
public abstract class GenericCRUDServiceJDBC<T extends IDBEntity, K> extends GenericCRUDService<T, K>{
    @Override
    protected boolean validateForCreate(T entity) {
        super.validateForCreate(entity);

        if (MvditUtils.stringEmpty(entity.getPrimaryKeyName()) || MvditUtils.stringEmpty(entity.getTableName())) {
            throw new MvditRuntimeException("The element " + entity.getClass().getSimpleName() + " not is a valid MvditEntity");
        }

        return true;
    }

    @Override
    protected boolean validateForUpdate(T entity) {
        super.validateForUpdate(entity);

        if (MvditUtils.stringEmpty(entity.getPrimaryKeyName()) || MvditUtils.stringEmpty(entity.getTableName())) {
            throw new MvditRuntimeException("The element " + entity.getClass().getSimpleName() + " not is a valid MvditEntity");
        }

        if (entity.getId() == null || (MvditUtils.isNumeric(entity.getId().toString()) && (BigInteger.valueOf(Long.valueOf(entity.getId().toString())).compareTo(BigInteger.ZERO) <= 0))
                || (entity.getId() instanceof String && MvditUtils.stringEmpty(entity.getId().toString()))) {
            throw new MvditRuntimeException("The element " + entity.getClass().getSimpleName() + " has not a valid id");
        }

        return true;
    }

    @Override
    protected boolean validateForDelete(T entity) {
        super.validateForDelete(entity);

        if (MvditUtils.stringEmpty(entity.getPrimaryKeyName()) || MvditUtils.stringEmpty(entity.getTableName())) {
            throw new MvditRuntimeException("The element " + entity.getClass().getSimpleName() + " not is a valid MvditEntity");
        }

         if (entity.getId() == null || (MvditUtils.isNumeric(entity.getId().toString()) && (BigInteger.valueOf(Long.valueOf(entity.getId().toString())).compareTo(BigInteger.ZERO) <= 0))
                || (entity.getId() instanceof String && MvditUtils.stringEmpty(entity.getId().toString()))) {
            throw new MvditRuntimeException("The element " + entity.getClass().getSimpleName() + " has not a valid id");
        }


        return true;
    }
}
