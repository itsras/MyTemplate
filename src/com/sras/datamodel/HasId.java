package com.sras.datamodel;

import com.sras.datamodel.exceptions.FixedValueException;

public interface HasId
{
	public long getId();
	public void setId(long id) throws FixedValueException;
}