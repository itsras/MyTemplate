package com.sras.dao;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.sras.datamodel.AddressData;
import com.sras.datamodel.DataModel;
import com.sras.datamodel.UserData;

public class ModelFactory
{

	@SuppressWarnings("rawtypes")
	private static Map<Class, Class> modelToDao;

	static
	{
		if (modelToDao == null || modelToDao.size() == 0)
		{
			modelToDao = new HashMap<Class, Class>();
			modelToDao.put(UserData.class, UserDao.class);
			modelToDao.put(AddressData.class, AddressDao.class);
		}
	}

	public static BaseDao getImplementation(DataModel dataModel) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException
	{
		if (dataModel == null)
		{
			return null;
		}
		Class modelClass = dataModel.getClass();
		Class daoClass = modelToDao.get(modelClass);
		Constructor<BaseDao> c = daoClass.getDeclaredConstructor(DataModel.class);
		return c.newInstance(dataModel);
	}
}
