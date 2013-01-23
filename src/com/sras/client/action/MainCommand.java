package com.sras.client.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.apache.velocity.context.Context;

import com.sras.client.utils.Utilities;

public class MainCommand extends Command
{
	private static String TEMPLATE_NAME = "index.vm";
	protected static Category log = Category.getInstance(MainCommand.class);
	
	public MainCommand(HttpServletRequest request, HttpServletResponse response, Context ctx)
	{
		super(request, response, ctx);
	}

	@Override
	public String execute()
	{

		if (isPost)
		{
			if (isAjax)
			{
				return "ajax_template.vm";
			}

		}
		else if (isGet)
		{
			if (isAjax)
			{
				ctx.put("ajax_response_data", "Ajax call successful!");
				return "ajax_template.vm";
			}
			String hostName = Utilities.getRemoteHostName(request);
			ctx.put("hostName", hostName);
		}
		// TODO Auto-generated method stub
		return TEMPLATE_NAME;
	}

}