package com.sras.dao;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.sras.datamodel.DataModel;
import com.sras.datamodel.UserData;
import com.sras.datamodel.exceptions.DataModelException;
import com.sras.datamodel.exceptions.FixedValueException;
import com.sras.datamodel.exceptions.TMException;

public class UserDao extends BaseDao {
	public UserDao(DataModel datamodel) {
		super(datamodel);
		// TODO Auto-generated constructor stub
	}

	private static Logger logger = Logger.getLogger(UserDao.class.getName());

	@Override
	public long create() throws DataModelException, TMException, SQLException {
		UserData user = (UserData) datamodel;
		if (user == null)
			throw new TMException("INVALID_VO",
					"provided User instance is null");

		if (isEmptyOrNull(user.getUserName()))
			throw new TMException("INVALID_KEY", "required key not found");

		if (read() != null)
			throw new SQLException("USER_EXISTS", "User Name already in use");

		try {
			String sql = CREATE_USER;
			Collection<SQLValue> bindVars = new ArrayList<SQLValue>();

			bindVars.add(SQLValue.String(user.getUserName()));
			bindVars.add(SQLValue.String(user.getFirstName()));
			bindVars.add(SQLValue.String(user.getMiddleName()));
			bindVars.add(SQLValue.String(user.getLastName()));
			bindVars.add(SQLValue.String(user.getMailId()));
			bindVars.add(SQLValue.String(user.getPassword()));
			bindVars.add(SQLValue.Blob((Blob) user.getImage()));
			bindVars.add(SQLValue.String(user.getDob()));
			bindVars.add(SQLValue.Long(user.getSex()));
			bindVars.add(SQLValue.Long(user.getAddressId()));
			bindVars.add(SQLValue.String(user.getMaritalStatus()));
			bindVars.add(SQLValue.String(user.getNationality()));

			logger.debug("QUERY - Loading Address :" + sql);
			return executeUpdate(sql, bindVars);
		} catch (SQLException sql) {
			logger.error("SQL-Exception", sql);
			throw new TMException("SQL-Exception", sql.getLocalizedMessage());
		}
	}

	@Override
	public long update() throws DataModelException, TMException, SQLException {
		UserData user = (UserData) datamodel;

		ResultSet rst = null;
		try {
			String sql = UPDATE_USER;
			Collection<SQLValue> bindVars = new ArrayList<SQLValue>();

			bindVars.add(SQLValue.String(user.getUserName()));
			bindVars.add(SQLValue.String(user.getFirstName()));
			bindVars.add(SQLValue.String(user.getMiddleName()));
			bindVars.add(SQLValue.String(user.getLastName()));
			bindVars.add(SQLValue.String(user.getMailId()));
			bindVars.add(SQLValue.String(user.getPassword()));
			bindVars.add(SQLValue.Blob((Blob) user.getImage()));
			bindVars.add(SQLValue.String(user.getDob()));
			bindVars.add(SQLValue.Long(user.getSex()));
			bindVars.add(SQLValue.Long(user.getAddressId()));
			bindVars.add(SQLValue.String(user.getMaritalStatus()));
			bindVars.add(SQLValue.String(user.getNationality()));
			bindVars.add(SQLValue.Long(user.getId()));

			logger.debug("QUERY - Loading Address :" + sql);
			return executeUpdate(sql, bindVars);
		} catch (SQLException sql) {
			logger.error("SQL-Exception", sql);
			throw new TMException("SQL-Exception", sql.getLocalizedMessage());
		} finally {
			close(null, rst);
		}
	}

	@Override
	public ArrayList<DataModel> enumerate() throws DataModelException,
			TMException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete() throws DataModelException, TMException, SQLException {
		UserData user = (UserData) datamodel;

		ResultSet rst = null;
		try {
			String sql = DELETE_USER;
			Collection<SQLValue> bindVars = new ArrayList<SQLValue>();
			if (user.getId() > 0) {
				sql += AND + "`ID` = ? ";
				bindVars.add(SQLValue.Long(user.getId()));
			}
			if (user.getUserName() != null) {
				sql += AND + "`USER_NAME` = ? ";
				bindVars.add(SQLValue.String(user.getUserName()));
			}
			logger.debug("QUERY - Loading Address :" + sql);
			return executeUpdate(sql, bindVars);
		} catch (SQLException sql) {
			logger.error("SQL-Exception", sql);
			throw new TMException("SQL-Exception", sql.getLocalizedMessage());
		} finally {
			close(null, rst);
		}
	}

	@Override
	public DataModel read() throws DataModelException, TMException,
			SQLException {
		UserData user = (UserData) datamodel;

		ResultSet rst = null;
		try {
			String sql = READ_USER;
			Collection<SQLValue> bindVars = new ArrayList<SQLValue>();
			if (user.getId() > 0) {
				sql += AND + "`ID` = ? ";
				bindVars.add(SQLValue.Long(user.getId()));
			}
			if (user.getUserName() != null) {
				sql += AND + "`USER_NAME` = ? ";
				bindVars.add(SQLValue.String(user.getUserName()));
			}
			if (user.getPassword() != null) {
				sql += AND + "`PASSWORD` = ? ";
				bindVars.add(SQLValue.String(user.getPassword()));
			}
			logger.debug("QUERY - Loading Address :" + sql);
			rst = executeQuery(sql, bindVars);

			return loadUserVO(user, rst);
		} catch (SQLException sql) {
			logger.error("SQL-Exception", sql);
			throw new TMException("SQL-Exception", sql.getLocalizedMessage());
		} finally {
			close(null, rst);
		}
	}

	@Override
	public boolean validateRules() throws TMException {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * 
	 * @param user
	 * @param rst
	 * @throws SQLException
	 */
	public DataModel loadUserVO(UserData user, ResultSet rst)
			throws TMException, SQLException {
		if (!rst.next()) {
			return null;
		}

		if (user == null) {
			user = new UserData();
		}
		try {
			user.setId(rst.getLong(ID));
		} catch (FixedValueException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
		user.setUserName(rst.getString(USER_NAME));
		user.setPassword(rst.getString(PASSWORD));
		user.setFirstName(rst.getString(FIRST_NAME));
		user.setMiddleName(rst.getString(MIDDLE_NAME));
		user.setLastName(rst.getString(LAST_NAME));
		user.setUserName(rst.getString(EMAIL_ID));
		user.setDob(rst.getString(DOB));
		user.setSex(rst.getInt(SEX));
		user.setAddressId(rst.getLong(ADDRESS_ID));
		user.setMaritalStatus(rst.getString(MARITAL_STATUS));
		user.setNationality(rst.getString(NATIONALITY));
		user.setImage(rst.getObject(IMAGE));
		return user;
	}

	public static final String READ_USER = "SELECT * FROM `USER` WHERE 1=1 ";
	public static final String DELETE_USER = "DELETE * FROM `USER` WHERE 1=1 ";
	public static final String UPDATE_USER = "UPDATE `USER` SET `USER_NAME` = ? ,`FIRST_NAME` = ? ,`MIDDLE_NAME` = ? ,`LAST_NAME` = ? ,`EMAIL_ID` = ? ,`PASSWORD` = ? ,`IMAGE` = ? ,`DOB` = ? ,`SEX` = ? ,`ADDRESS_ID` = ? ,`MARITAL_STATUS` = ? ,`NATIONALITY` = ? WHERE `ID` = ?";
	public static final String CREATE_USER = "INSERT INTO `USER` (`USER_NAME`,`FIRST_NAME`,`MIDDLE_NAME`,`LAST_NAME`,`EMAIL_ID`,`PASSWORD`,`IMAGE`,`DOB`,`SEX`,`ADDRESS_ID`,`MARITAL_STATUS`,`NATIONALITY`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public static final String USER_NAME = "USER_NAME";
	public static final String PASSWORD = "PASSWORD";
	public static final String FIRST_NAME = "FIRST_NAME";
	public static final String MIDDLE_NAME = "MIDDLE_NAME";
	public static final String LAST_NAME = "LAST_NAME";
	public static final String DOB = "DOB";
	public static final String SEX = "SEX";
	public static final String ADDRESS_ID = "ADDRESS_ID";
	public static final String FULL_REG = "IS_FULL_REG";
	public static final String IMAGE = "IMAGE";
	public static final String MARITAL_STATUS = "MARITAL_STATUS";
	public static final String EMAIL_ID = "EMAIL_ID";
	public static final String CURRENCY_CODE_ID = "CURRENCY_CODE_ID";
	public static final String NATIONALITY = "NATIONALITY";

}
