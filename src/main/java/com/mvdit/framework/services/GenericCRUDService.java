/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.services;

import com.mvdit.framework.core.MvditApp;
import com.mvdit.framework.core.MvditRuntimeException;
import com.mvdit.framework.core.MvditUtils;
import com.mvdit.framework.core.MvditValidatorException;
import com.mvdit.framework.dao.IGenericDAO;
import com.mvdit.framework.data.IFilter;
import com.mvdit.framework.data.IPageResult;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Path;

/**
 *
 * @author Pablo Ramírez
 * @param <T>
 * @param <K>
 */
public abstract class GenericCRUDService<T, K> implements IGenericCRUDService<T, K> {

    protected abstract IGenericDAO<T, K> getDAOInstance();

    protected boolean validateForCreate(T entity) {
        if (entity == null) {
            throw new MvditRuntimeException("The element cannot be null");
        }

        Set<ConstraintViolation<T>> violations = MvditUtils.getValidator().validate(entity);

        if (violations.size() > 0) {
            MvditApp.getInstance().getLogger().error(entity.getClass().getSimpleName() + entity + " [CREATE] tiene error de datos " +  violations);
            MvditValidatorException mvditValidatorException = new MvditValidatorException();
            for (ConstraintViolation<T> constraint : violations) {
               
                mvditValidatorException.addViolation(constraint);
            }
            throw mvditValidatorException;
        }

        return true;
    }

    protected boolean validateForUpdate(T entity) {
        if (entity == null) {
            throw new MvditRuntimeException("The element cannot be null");
        }

        Set<ConstraintViolation<T>> violations = MvditUtils.getValidator().validate(entity);

        if (violations.size() > 0) {
            MvditApp.getInstance().getLogger().error(entity.getClass().getSimpleName() + entity + " [UPDATE] tiene error de datos " +  violations);
            MvditValidatorException mvditValidatorException = new MvditValidatorException();
            for (ConstraintViolation<T> constraint : violations) {
                mvditValidatorException.addViolation(constraint);
            }
            throw mvditValidatorException;
        }
        return true;
    }

    protected boolean validateForDelete(T entity) {
        if (entity == null) {
            throw new MvditRuntimeException("The element cannot be null");
        }

        /*if (MvditUtils.stringEmpty(entity.getPrimaryKeyName()) || MvditUtils.stringEmpty(entity.getTableName())) {
         throw new MvditRuntimeException("The element " + entity.getClass().getSimpleName() + " not is a valid MvditEntity");
         }

         if (entity.getId() == null || (MvditUtils.isNumeric(entity.getId().toString()) && (BigInteger.valueOf(Long.valueOf(entity.getId().toString())).compareTo(BigInteger.ZERO) <= 0))
         || (entity.getId() instanceof String && MvditUtils.stringEmpty(entity.getId().toString()))) {
         throw new MvditRuntimeException("The element " + entity.getClass().getSimpleName() + " has not a valid id");
         }*/
        return true;
    }

    @Override
    public T create(T entity) {
        if (this.validateForCreate(entity)) {
            return this.getDAOInstance().create(entity);
        } else {
            throw new MvditRuntimeException("The class " + entity.getClass().getSimpleName() + " is not valid for create");
        }
    }

    @Override
    public T update(T entity) {
        if (this.validateForUpdate(entity)) {
            return this.getDAOInstance().update(entity);
        } else {
            throw new MvditRuntimeException("The class " + entity.getClass().getSimpleName() + " is not valid for update");
        }
    }

    @Override
    public int delete(T entity) {
        if (this.validateForDelete(entity)) {
            return this.getDAOInstance().delete(entity);
        } else {
            throw new MvditRuntimeException("The class " + entity.getClass().getSimpleName() + " is not valid for delete");
        }
    }

    @Override
    public IPageResult list(IFilter filter) {
        if (filter != null) {
            return this.getDAOInstance().list(filter);
        } else {
            throw new MvditRuntimeException("The filter cannot be null");
        }
    }

    @Override
    public T getById(K id) {
        return this.getDAOInstance().getById(id);
    }

    @Override
    public int count(IFilter filter) {
        return this.getDAOInstance().count(filter);
    }

}
