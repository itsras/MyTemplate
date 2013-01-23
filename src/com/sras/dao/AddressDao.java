package com.sras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sras.datamodel.AddressData;
import com.sras.datamodel.DataModel;
import com.sras.datamodel.exceptions.DataModelException;
import com.sras.datamodel.exceptions.TMException;

public class AddressDao extends BaseDao
{
	public AddressDao(DataModel datamodel)
	{
		super(datamodel);
		// TODO Auto-generated constructor stub
	}

	private static Logger logger = Logger.getLogger(UserDao.class.getName());

	@Override
	public long create() throws DataModelException, TMException, SQLException
	{
		AddressData address = (AddressData) datamodel;
		if (address == null)
			throw new TMException("NULL_ADDRESS", "Provided address instance is null");

		Connection connection = getConnection();
		AddressData existingAddress = (AddressData) read();
		if (existingAddress != null)
		{
			return existingAddress.getId();
		}

		PreparedStatement pStmt = null;
		ResultSet rst = null;
		long addressId;
		try
		{
			pStmt = connection.prepareStatement(CREATE_USER_ADDRESS,
					Statement.RETURN_GENERATED_KEYS);
			/*
			 * if(!RulesHelper.getInstance().validateAddressRules(address)) //
			 * this assumes all the required fields are set in Address instance.
			 * throw new TMException("RULE_FAIL",
			 * "Either one or more user rules failed");
			 */
			prepareStatementAttribs(pStmt, address);
			logger.debug("QUERY - Creating Address :" + pStmt.toString());
			pStmt.execute();
			rst = pStmt.getGeneratedKeys();
			if (rst.next())
				addressId = rst.getLong(1);
			else
				throw new TMException("INSERT_FAIL", "Address creation failed");
		}
		catch (SQLException sql)
		{
			logger.error("SQL-Exception", sql);
			throw new TMException("SQL-Exception", sql.getLocalizedMessage());
		}
		finally
		{
			close(pStmt, rst);
			closeConnection(connection);
		}
		return addressId;
	}

	@Override
	public long update() throws DataModelException, TMException, SQLException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<DataModel> enumerate() throws DataModelException, TMException, SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete() throws DataModelException, TMException, SQLException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DataModel read() throws DataModelException, TMException, SQLException
	{
		AddressData address = (AddressData) datamodel;
		Map<String, String> attribs = convertToMap(address);
		long addressId = -1l;
		if (attribs == null)
			throw new TMException("EMPTY_ATTRIBS", "parameters to check are not present");
		if (attribs.isEmpty())
		{
			logger.warn("No Records found to update");
			return null;
		}
		Connection connection = getConnection();
		PreparedStatement pStmt = null;
		ResultSet rst = null;
		try
		{
			pStmt = connection.prepareStatement(LOAD_USER_ADDRESS);
			pStmt.setLong(1, address.getId());
			logger.debug("QUERY - Loading Address :" + pStmt.toString());
			rst = pStmt.executeQuery();

			while (rst.next())
			{
				address.setId(rst.getLong(ID));
				address.setBldName(rst.getString(BLD_NAME));
				address.setBldNo(rst.getString(BLD_NUM));
				address.setLandmark(rst.getString(LAND_MARK));
				address.setLocality(rst.getString(LOCALITY_NAME));
				address.setCity(rst.getString(CITY));
				address.setState(rst.getString(STATE));
				address.setCountry(rst.getString(COUNTRY));
				address.setPostalcode(rst.getString(POSTAL_CODE));
				address.setMobileNum(rst.getString(MOBILE_NUM));
				address.setLandNum(rst.getString(LANDLINE_NUM));
				address.setAreaCode(rst.getString(AREA_CODE));
			}
		}
		catch (SQLException sql)
		{
			logger.error("SQL-Exception", sql);
			throw new TMException("SQL-Exception", sql.getLocalizedMessage());
		}
		finally
		{
			close(pStmt, rst);
			closeConnection(connection);
		}
		return address;
	}

	@Override
	public boolean validateRules() throws TMException
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 
	 * @param pstmt
	 * @param address
	 * @throws SQLException
	 */
	public void prepareStatementAttribs(PreparedStatement pstmt, AddressData address)
			throws SQLException
	{
		int incr = 1;
		prepareStatmentAttribute(pstmt, java.sql.Types.VARCHAR, address.getBldName(), incr++);
		prepareStatmentAttribute(pstmt, java.sql.Types.VARCHAR, address.getBldNo(), incr++);
		prepareStatmentAttribute(pstmt, java.sql.Types.VARCHAR, address.getStreet(), incr++);
		prepareStatmentAttribute(pstmt, java.sql.Types.VARCHAR, address.getLocality(), incr++);
		prepareStatmentAttribute(pstmt, java.sql.Types.VARCHAR, address.getLandmark(), incr++);
		prepareStatmentAttribute(pstmt, java.sql.Types.VARCHAR, address.getCity(), incr++);
		prepareStatmentAttribute(pstmt, java.sql.Types.VARCHAR, address.getState(), incr++);
		// prepareStatmentAttribute(pstmt, java.sql.Types.INTEGER,
		// String.valueOf(address.getPostalcode()), incr++);
		prepareStatmentAttribute(pstmt, java.sql.Types.VARCHAR, address.getPostalcode(), incr++);
		prepareStatmentAttribute(pstmt, java.sql.Types.VARCHAR, address.getCountry(), incr++);
		// prepareStatmentAttribute(pstmt, java.sql.Types.INTEGER,
		// String.valueOf(address.getMobileNum()), incr++);
		prepareStatmentAttribute(pstmt, java.sql.Types.VARCHAR, address.getMobileNum(), incr++);
		// prepareStatmentAttribute(pstmt, java.sql.Types.INTEGER,
		// String.valueOf(address.getLandNum()), incr++);
		prepareStatmentAttribute(pstmt, java.sql.Types.VARCHAR, address.getLandNum(), incr++);
		// prepareStatmentAttribute(pstmt, java.sql.Types.INTEGER,
		// String.valueOf(address.getAreaCode()), incr++);
		prepareStatmentAttribute(pstmt, java.sql.Types.VARCHAR, address.getAreaCode(), incr);

	}

	public static Map<String, String> convertToMap(AddressData address)
	{
		Map<String, String> addressMap = new HashMap<String, String>();
		if (address.getBldName() != null)
			addressMap.put(BLD_NAME, address.getBldName());

		if (address.getBldNo() != null)
			addressMap.put(BLD_NUM, address.getBldNo());

		if (address.getCity() != null)
			addressMap.put(CITY, address.getCity());

		if (address.getCountry() != null)
			addressMap.put(COUNTRY, address.getCountry());

		if (address.getLandmark() != null)
			addressMap.put(LAND_MARK, address.getLandmark());

		if (String.valueOf(address.getLandNum()).length() > 0)
			addressMap.put(LANDLINE_NUM, String.valueOf(address.getLandNum()));

		if (address.getLocality() != null)
			addressMap.put(LOCALITY_NAME, address.getLocality());

		if (String.valueOf(address.getMobileNum()).length() > 0)
			addressMap.put(MOBILE_NUM, String.valueOf(address.getMobileNum()));

		if (String.valueOf(address.getPostalcode()).length() > 0)
			addressMap.put(POSTAL_CODE, String.valueOf(address.getPostalcode()));

		if (address.getState() != null)
			addressMap.put(STATE, address.getState());

		if (address.getStreet() != null)
			addressMap.put(STREET, address.getStreet());

		if (String.valueOf(address.getAreaCode()).length() > 0)
			addressMap.put(AREA_CODE, String.valueOf(address.getAreaCode()));

		return addressMap;
	}

	public static String prepareConstraintParams(Map<String, String> attributes)
	{
		StringBuilder sbd = new StringBuilder();
		for (Map.Entry each : attributes.entrySet())
			sbd.append("`").append(each.getKey()).append("` = '").append(each.getValue())
					.append("' AND ");

		return sbd.substring(0, sbd.lastIndexOf(" AND "));
	}

	public static final String ID = "ID";
	public static final String BLD_NAME = "BLD_NAME";
	public static final String BLD_NUM = "BLD_NUM";
	public static final String STREET = "STREET";
	public static final String LOCALITY_NAME = "LOCALITY_NAME";
	public static final String CITY = "CITY";
	public static final String STATE = "STATE";
	public static final String POSTAL_CODE = "POSTAL_CODE";
	public static final String COUNTRY = "COUNTRY";
	public static final String MOBILE_NUM = "MOBILE_NUMBER";
	public static final String LANDLINE_NUM = "LAND_LINE_NUMBER";
	public static final String AREA_CODE = "AREA_CODE";
	public static final String LAND_MARK = "LAND_MARK";

	public static final String LOAD_USER_ADDRESS = "SELECT * FROM `ADDRESS` WHERE `ID` = ?";
	public static final String CREATE_USER_ADDRESS = "INSERT INTO `ADDRESS` (`BLD_NAME`, `BLD_NUM`, `STREET`, `LOCALITY_NAME`, `LAND_MARK`, `CITY`, `STATE`, `POSTAL_CODE`, `COUNTRY`, `MOBILE_NUMBER`, `LAND_LINE_NUMBER`, AREA_CODE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String IS_ADDRESS_EXISTS = "SELECT COUNT(*), `ID` FROM `ADDRESS` WHERE ";
	public static final String IS_ADDRESS_OBJ_EXISTS = "SELECT COUNT(*), `ID`  FROM `ADDRESS` WHERE `BLD_NAME` = ? AND `BLD_NUM` = ? AND `STREET` = ? AND `LOCALITY_NAME` = ? AND `LAND_MARK` = ? AND `CITY` = ? AND `STATE` = ? AND `POSTAL_CODE` = ? AND `COUNTRY` = ? AND `MOBILE_NUMBER` = ? AND `LAND_LINE_NUMBER` = ? AND `AREA_CODE`  = ? ";

}
