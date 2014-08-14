/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mvdit.framework.database;

/**
 *
 * @author Pablo Ramírez
 */
public interface ITransaction {
    /**
     * Inicializa una transaccion
     * @throws TransactionException 
     */
    public void initTransaction() throws TransactionException;
    /**
     * Commitea una trasnsaccion
     * @throws TransactionException 
     */
    public void commitTransaction() throws TransactionException;
    /**
     * Hace un rollback a una transaccion
     * @throws TransactionException 
     */
    public void rollbackTransaction() throws TransactionException;
    /**
     * Indica si la transacción está activa
     * @return 
     */
    public boolean isTransactionActive();
}
