package com.sras.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.sras.datamodel.AddressData;
import com.sras.datamodel.DataModel;
import com.sras.datamodel.exceptions.DataModelException;
import com.sras.datamodel.exceptions.FixedValueException;
import com.sras.datamodel.exceptions.TMException;

public class AddressDao extends BaseDao {
	public AddressDao(DataModel datamodel) {
		super(datamodel);
		// TODO Auto-generated constructor stub
	}

	private static Logger logger = Logger.getLogger(AddressDao.class.getName());

	@Override
	public long create() throws DataModelException, TMException, SQLException {
		AddressData address = (AddressData) datamodel;
		if (address == null)
			throw new TMException("NULL_ADDRESS",
					"Provided address instance is null");

		AddressData existingAddress = (AddressData) read();
		if (existingAddress != null) {
			return existingAddress.getId();
		}

		try {
			String sql = CREATE_USER_ADDRESS;
			Collection<SQLValue> bindVars = new ArrayList<SQLValue>();

			bindVars.add(SQLValue.String(address.getBldName()));
			bindVars.add(SQLValue.String(address.getBldNo()));
			bindVars.add(SQLValue.String(address.getStreet()));
			bindVars.add(SQLValue.String(address.getLocality()));
			bindVars.add(SQLValue.String(address.getCity()));
			bindVars.add(SQLValue.String(address.getState()));
			bindVars.add(SQLValue.String(address.getPostalcode()));
			bindVars.add(SQLValue.String(address.getCountry()));
			bindVars.add(SQLValue.String(address.getMobileNum()));
			bindVars.add(SQLValue.String(address.getLandNum()));
			bindVars.add(SQLValue.String(address.getAreaCode()));
			bindVars.add(SQLValue.String(address.getLandmark()));

			logger.debug("QUERY - Creating Address :" + sql);
			return executeUpdate(sql, bindVars);
		} catch (SQLException sql) {
			logger.error("SQL-Exception", sql);
			throw new TMException("SQL-Exception", sql.getLocalizedMessage());
		}
	}

	@Override
	public long update() throws DataModelException, TMException, SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<DataModel> enumerate() throws DataModelException,
			TMException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete() throws DataModelException, TMException, SQLException {
		AddressData addressData = (AddressData) datamodel;
		try {
			String sql = DELETE_USER_ADDRESS;
			Collection<SQLValue> bindVars = new ArrayList<SQLValue>();
			if (addressData.getId() > 0) {
				sql += AND + "`ID` = ? ";
				bindVars.add(SQLValue.Long(addressData.getId()));
			}
			logger.debug("QUERY - Loading Address :" + sql);
			return executeUpdate(sql, bindVars);
		} catch (SQLException sql) {
			logger.error("SQL-Exception", sql);
			throw new TMException("SQL-Exception", sql.getLocalizedMessage());
		}
	}

	@Override
	public DataModel read() throws DataModelException, TMException,
			SQLException {
		AddressData addressData = (AddressData) datamodel;
		ResultSet rst = null;
		try {
			String sql = LOAD_USER_ADDRESS;
			Collection<SQLValue> bindVars = new ArrayList<SQLValue>();
			sql += setBindVariables(addressData, bindVars);
			logger.debug("QUERY - Loading Address :" + sql);
			rst = executeQuery(sql, bindVars);
			return loadAddressVO(addressData, rst);
		} catch (SQLException sql) {
			logger.error("SQL-Exception", sql);
			throw new TMException("SQL-Exception", sql.getLocalizedMessage());
		} finally {
			close(null, rst);
		}
	}

	private String setBindVariables(AddressData addressData,
			Collection<SQLValue> bindVars) {
		String sql = "";
		if (addressData.getId() > 0) {
			sql += AND + "`ID` = ? ";
			bindVars.add(SQLValue.Long(addressData.getId()));
		}
		if (addressData.getBldName() != null) {
			sql += AND + "`BLD_NAME` = ? ";
			bindVars.add(SQLValue.String(addressData.getBldName()));
		}
		if (addressData.getBldNo() != null) {
			sql += AND + "`BLD_NUM` = ? ";
			bindVars.add(SQLValue.String(addressData.getBldNo()));
		}
		if (addressData.getStreet() != null) {
			sql += AND + "`STREET` = ? ";
			bindVars.add(SQLValue.String(addressData.getStreet()));
		}
		if (addressData.getLocality() != null) {
			sql += AND + "`LOCALITY_NAME` = ? ";
			bindVars.add(SQLValue.String(addressData.getLocality()));
		}
		if (addressData.getCity() != null) {
			sql += AND + "`CITY` = ? ";
			bindVars.add(SQLValue.String(addressData.getCity()));
		}
		if (addressData.getState() != null) {
			sql += AND + "`STATE` = ? ";
			bindVars.add(SQLValue.String(addressData.getState()));
		}
		if (addressData.getPostalcode() != null) {
			sql += AND + "`POSTAL_CODE` = ? ";
			bindVars.add(SQLValue.String(addressData.getPostalcode()));
		}
		if (addressData.getCountry() != null) {
			sql += AND + "`COUNTRY` = ? ";
			bindVars.add(SQLValue.String(addressData.getCountry()));
		}
		if (addressData.getMobileNum() != null) {
			sql += AND + "`MOBILE_NUMBER` = ? ";
			bindVars.add(SQLValue.String(addressData.getMobileNum()));
		}
		if (addressData.getLandNum() != null) {
			sql += AND + "`LAND_LINE_NUMBER` = ? ";
			bindVars.add(SQLValue.String(addressData.getLandNum()));
		}
		if (addressData.getAreaCode() != null) {
			sql += AND + "`AREA_CODE` = ? ";
			bindVars.add(SQLValue.String(addressData.getAreaCode()));
		}
		if (addressData.getLandmark() != null) {
			sql += AND + "`LAND_MARK` = ? ";
			bindVars.add(SQLValue.String(addressData.getLandmark()));
		}
		return sql;
	}

	@Override
	public boolean validateRules() throws TMException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 
	 * @param addressData
	 * @param rst
	 * @throws SQLException
	 */
	public DataModel loadAddressVO(AddressData addressData, ResultSet rst)
			throws TMException, SQLException {
		if (!rst.next()) {
			return null;
		}

		if (addressData == null) {
			addressData = new AddressData();
		}
		try {
			addressData.setId(rst.getLong(ID));
		} catch (FixedValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SQLException(e);
		}
		addressData.setBldName(rst.getString(BLD_NAME));
		addressData.setBldNo(rst.getString(BLD_NUM));
		addressData.setStreet(rst.getString(STREET));
		addressData.setLocality(rst.getString(LOCALITY_NAME));
		addressData.setCity(rst.getString(CITY));
		addressData.setState(rst.getString(STATE));
		addressData.setPostalcode(rst.getString(POSTAL_CODE));
		addressData.setCountry(rst.getString(COUNTRY));
		addressData.setMobileNum(rst.getString(MOBILE_NUM));
		addressData.setLandNum(rst.getString(LANDLINE_NUM));
		addressData.setAreaCode(rst.getString(AREA_CODE));
		addressData.setLandmark(rst.getString(LAND_MARK));
		return addressData;
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

	public static final String DELETE_USER_ADDRESS = "DELETE * FROM `ADDRESS` WHERE 1=1 ";
	public static final String LOAD_USER_ADDRESS = "SELECT * FROM `ADDRESS` WHERE 1=1 ";
	public static final String CREATE_USER_ADDRESS = "INSERT INTO `ADDRESS` (`BLD_NAME`, `BLD_NUM`, `STREET`, `LOCALITY_NAME`, `LAND_MARK`, `CITY`, `STATE`, `POSTAL_CODE`, `COUNTRY`, `MOBILE_NUMBER`, `LAND_LINE_NUMBER`, AREA_CODE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String IS_ADDRESS_EXISTS = "SELECT COUNT(*), `ID` FROM `ADDRESS` WHERE ";
	public static final String IS_ADDRESS_OBJ_EXISTS = "SELECT COUNT(*), `ID`  FROM `ADDRESS` WHERE `BLD_NAME` = ? AND `BLD_NUM` = ? AND `STREET` = ? AND `LOCALITY_NAME` = ? AND `LAND_MARK` = ? AND `CITY` = ? AND `STATE` = ? AND `POSTAL_CODE` = ? AND `COUNTRY` = ? AND `MOBILE_NUMBER` = ? AND `LAND_LINE_NUMBER` = ? AND `AREA_CODE`  = ? ";

}
