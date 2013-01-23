package com.sras.datamodel.exceptions;

import com.sras.datamodel.DataModel;

public class InvalidIdDataModelException extends DataModelException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8629735279101757883L;

	public InvalidIdDataModelException(DataModel dataModel)
	{
		super("Can not create " + dataModel.getClass().getName() + ". ID must be 0", dataModel);
	}

	public InvalidIdDataModelException(String msg, DataModel dataModel)
	{
		super("Cannot use " + dataModel.getClass().getName() + "Invalid ID: " + msg);
	}

}
