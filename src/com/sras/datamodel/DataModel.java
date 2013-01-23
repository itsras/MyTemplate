package com.sras.datamodel;

import java.io.Serializable;

public interface DataModel extends Serializable
{
	public boolean isLoaded();

	public void setLoaded(boolean loaded);

	public void validateForCreate() throws Exception;

	public void validateForUpdate() throws Exception;

	public void validateForDelete() throws Exception;
}