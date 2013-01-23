package com.sras.datamodel;

import com.sras.datamodel.exceptions.DataModelException;
import com.sras.datamodel.exceptions.FixedValueException;
import com.sras.datamodel.exceptions.InvalidIdDataModelException;

/**
 * A class that implements the Id.
 */
public abstract class WithId implements DataModel, HasId, ChangeNotifiable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3770785332609656732L;

	private long id;

	private boolean loaded;

	public boolean isLoaded()
	{
		return loaded;
	}

	public void setLoaded(boolean loaded)
	{
		this.loaded = loaded;
	}

	public WithId()
	{
	}

	public WithId(long id)
	{
		this.id = id;
	}

	public void validateForCreate() throws DataModelException
	{
		if (id != 0)
			throw new InvalidIdDataModelException(this);
	}

	public void validateForUpdate() throws DataModelException
	{
		if (id == 0)
			throw new InvalidIdDataModelException("ID cannot be 0 for update", this);
	}

	public void validateForDelete() throws DataModelException
	{
		if (id == 0)
			throw new InvalidIdDataModelException("ID cannot be 0 for delete", this);
	}

	public final long getId()
	{
		return id;
	}

	public final void setId(long id) throws FixedValueException
	{
		this.id = id;
	}

	public String toString()
	{
		String str = "";

		str += "\nID:" + id;
		return str;
	}

	public boolean equals(Object o)
	{
		if (this == o)
			return true;

		if (!(o instanceof WithId))
			return false;

		if (!o.getClass().equals(getClass()))
			return false;

		return ((WithId) o).id == id;
	}

	public int hashCode()
	{
		return (int) id;
	}
}
