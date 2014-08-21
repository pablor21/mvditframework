/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mvdit.framework.core;

import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintViolation;

/**
 *
 * @author Pablo Ramírez
 */
public class MvditValidatorException extends MvditRuntimeException{
    Set<ConstraintViolation> violations;
    public MvditValidatorException(Exception ex) {
        super(ex);
    }
    
    public MvditValidatorException(){
        super("El elemento enviado no es válido.");
        this.violations= new HashSet<>();
    }
    
    public MvditValidatorException(Set<ConstraintViolation> violations){
        super("El elemento enviado no es válido.");
        this.violations= violations;
    }
    
    public void setViolations(Set<ConstraintViolation> violations){
        this.violations= violations;
    }
    
    public Set<ConstraintViolation> getViolations(){
        return this.violations;
    }
    
    public void addViolation(ConstraintViolation violation){
        this.violations.add(violation);
    }
}
