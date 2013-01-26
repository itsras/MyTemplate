/******************************************************************************
 * Copyright (c) 2010. Electronic Arts                                        *
 ******************************************************************************/

package com.sras.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.sras.datamodel.exceptions.TMException;

/**
 * User: dsonti
 */
public interface ConnectionManager {

	public void initialize() throws TMException;

	/**
	 * cleaning up the connections that created by the BasicDataSource pool
	 */
	public void shutdownDriver() throws SQLException;

	public Connection getConnection() throws TMException;

}
