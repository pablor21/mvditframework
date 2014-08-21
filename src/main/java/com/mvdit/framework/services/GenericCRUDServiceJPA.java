/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mvdit.framework.services;

import com.mvdit.framework.core.MvditUtils;
import com.mvdit.framework.core.MvditValidatorException;
import java.util.Set;
import javax.validation.ConstraintViolation;

/**
 *
 * @author Pablo Ramírez
 */
public abstract class GenericCRUDServiceJPA<T, K> extends GenericCRUDService<T, K> {

    @Override
    protected boolean validateForCreate(T entity) {
        super.validateForCreate(entity);

        

        return true;
    }

    @Override
    protected boolean validateForUpdate(T entity) {
        super.validateForUpdate(entity);
        Set<ConstraintViolation<T>> violations = MvditUtils.getValidator().validate(entity);

        if (violations.size() > 0) {
            MvditValidatorException mvditValidatorException = new MvditValidatorException();
            for (ConstraintViolation<T> constraint : violations) {
                mvditValidatorException.addViolation(constraint);
            }
            throw mvditValidatorException;
        }

        return true;
    }

    @Override
    protected boolean validateForDelete(T entity) {
        super.validateForDelete(entity);

        return true;
    }
}
