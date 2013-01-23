package com.sras.datamodel.exceptions;

import com.sras.datamodel.DataModel;

public class DataModelException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5032606612325526496L;
	private DataModel data;

	public DataModelException(String str)
	{
		super(str);
	}

	public DataModelException(String msg, Throwable cause)
	{
		super(msg, cause);
	}

	public DataModelException(String str, DataModel d)
	{
		super(str);
		data = d;
	}

	public DataModelException(String msg, DataModel d, Throwable cause)
	{
		super(msg, cause);
		data = d;
	}

	public DataModel getDataModel()
	{
		return (data);
	}
}
