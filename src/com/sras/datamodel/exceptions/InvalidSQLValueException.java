/*
 *   	Copyright 2002, AppIQ, Inc.,  All Rights Reserved
 */
package com.sras.datamodel.exceptions;

public class InvalidSQLValueException extends RuntimeException
{
	/**
	 * Create a new exception.
	 * 
	 * @param msg
	 */
	public InvalidSQLValueException(String msg)
	{
		super(msg);
	}
}
