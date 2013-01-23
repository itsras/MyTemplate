package com.sras.client.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.sras.datamodel.exceptions.TMException;

public class CurrencyHelper
{

	private static Logger logger = Logger.getLogger(CurrencyHelper.class.getName());

	private static CurrencyHelper instance;
	private Map<Integer, String> currencies = new HashMap<Integer, String>(8);

	private CurrencyHelper()
	{

	}

	public static CurrencyHelper getCurrencyHelper() throws TMException
	{
		if (instance == null)
			instance = new CurrencyHelper();

		return instance;
	}

	public void initialize() throws TMException
	{
		logger.debug("loding the currency codes");
		// currencies = CommonsDAO.loadCurrency();
	}

	public Collection<String> getCurrencyCodes()
	{
		return currencies.values();
	}

	public String getCurencyCode(int id)
	{
		return currencies.get(id);
	}

	public void setup()
	{
		Random rand = new Random(Integer.MAX_VALUE);
		List<Integer> main = new ArrayList<Integer>(14);
		for (int n = 1; n < 15; n++)
			main.add(n);
		int no = 0;
		List<Integer> number = new ArrayList<Integer>(14);
		for (int max = 0; max < 10000; max++)
		{
			for (int m = 0; m < 14; m++)
			{
				no = rand.nextInt();
				if (no < 0)
					no = -no;
				number.add(main.remove(no % main.size()));
				if (main.isEmpty())
				{
					for (int n = 1; n < 15; n++)
						main.add(n);
				}
			}
		}
		for (int iter = 0; iter < number.size(); iter++)
		{
			if (iter % 14 == 0)
				System.out.println();
			System.out.print(number.get(iter) + ", ");

		}
	}

}
