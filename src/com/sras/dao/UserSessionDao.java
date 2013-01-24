package com.sras.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.sras.datamodel.DataModel;
import com.sras.datamodel.UserSessionData;
import com.sras.datamodel.exceptions.DataModelException;
import com.sras.datamodel.exceptions.TMException;

public class UserSessionDao extends BaseDao {

	private static Logger logger = Logger.getLogger(UserSessionDao.class
			.getName());

	public UserSessionDao(DataModel datamodel) {
		super(datamodel);
		// TODO Auto-generated constructor stub
	}

	@Override
	public long create() throws DataModelException, TMException, SQLException {
		UserSessionData userSession = (UserSessionData) datamodel;
		if (userSession == null)
			throw new TMException("INVALID_VO",
					"provided User instance is null");

		if (isEmptyOrNull(userSession.getSessionId()))
			throw new TMException("INVALID_KEY",
					"required SESSION_ID key not found");

		if (userSession.getUserId() == 0) {
			throw new TMException("INVALID_KEY",
					"Required USER_ID key not found");
		}

		if (read() != null)
			throw new SQLException("USER_SESSION_EXISTS",
					"User Session is already in use");

		ResultSet rst = null;
		try {
			String sql = CREATE_USER_SESSION;
			Collection<SQLValue> bindVars = new ArrayList<SQLValue>();

			bindVars.add(SQLValue.String(userSession.getSessionId()));
			bindVars.add(SQLValue.Long(userSession.getUserId()));
			bindVars.add(SQLValue.Timestamp(userSession.getCreateTime()));
			bindVars.add(SQLValue.Timestamp(userSession.getLastUpdateTime()));
			bindVars.add(SQLValue.String(userSession.getLanguage()));
			bindVars.add(SQLValue.String(userSession.getEncryptionKey()));
			bindVars.add(SQLValue.Long(userSession.getTimeout()));
			bindVars.add(SQLValue.String(userSession.getIpAddress()));
			bindVars.add(SQLValue.String(userSession.getHostName()));
			bindVars.add(SQLValue.String(userSession.getBrowser()));
			bindVars.add(SQLValue.String(userSession.getCountry()));

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
	public long update() throws DataModelException, TMException, SQLException {
		UserSessionData userSession = (UserSessionData) datamodel;

		ResultSet rst = null;
		try {
			String sql = UPDATE_USER_SESSION;
			Collection<SQLValue> bindVars = new ArrayList<SQLValue>();

			bindVars.add(SQLValue.Long(userSession.getTimeout()));
			bindVars.add(SQLValue.String(userSession.getEncryptionKey()));
			bindVars.add(SQLValue.String(userSession.getSessionId()));
			bindVars.add(SQLValue.Long(userSession.getUserId()));

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
		UserSessionData userSession = (UserSessionData) datamodel;

		ResultSet rst = null;
		try {
			String sql = DELETE_USER_SESSION;
			Collection<SQLValue> bindVars = new ArrayList<SQLValue>();
			if (userSession.getUserId() > 0) {
				sql += AND + "`USER_ID` = ? ";
				bindVars.add(SQLValue.Long(userSession.getUserId()));
			}
			if (userSession.getSessionId() != null) {
				sql += AND + "`SESSION_ID` = ? ";
				bindVars.add(SQLValue.String(userSession.getSessionId()));
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
		UserSessionData userSession = (UserSessionData) datamodel;

		ResultSet rst = null;
		try {
			String sql = READ_USER_SESSION;
			Collection<SQLValue> bindVars = new ArrayList<SQLValue>();
			if (userSession.getUserId() > 0) {
				sql += AND + "`USER_ID` = ? ";
				bindVars.add(SQLValue.Long(userSession.getUserId()));
			}
			if (userSession.getSessionId() != null) {
				sql += AND + "`SESSION_ID` = ? ";
				bindVars.add(SQLValue.String(userSession.getSessionId()));
			}

			logger.debug("QUERY - Loading Address :" + sql);
			rst = executeQuery(sql, bindVars);

			return loadUserVO(userSession, rst);
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
		return false;
	}

	/**
	 * 
	 * @param userSession
	 * @param rst
	 * @throws SQLException
	 */
	public DataModel loadUserVO(UserSessionData userSession, ResultSet rst)
			throws TMException, SQLException {
		if (!rst.next()) {
			return null;
		}

		if (userSession == null) {
			userSession = new UserSessionData();
		}

		userSession.setSessionId(rst.getString(SESSION_ID));
		userSession.setUserId(rst.getLong(USER_ID));
		userSession.setCreateTime(rst.getDate(CREATE_TIME));
		userSession.setLastUpdateTime(rst.getDate(LAST_MODIFY_TIME));
		userSession.setLanguage(rst.getString(LANGUAGE));
		userSession.setEncryptionKey(rst.getString(ENCRYPTION_KEY));
		userSession.setTimeout(rst.getLong(TIMEOUT));
		userSession.setIpAddress(rst.getString(IP_ADDRESS));
		userSession.setHostName(rst.getString(HOST_NAME));
		userSession.setBrowser(rst.getString(BROWSER));
		userSession.setCountry(rst.getString(COUNTRY));
		userSession.setLoaded(true);
		return userSession;
	}

	public static final String READ_USER_SESSION = "SELECT * FROM `USER_SESSIONS` WHERE 1=1 ";
	public static final String DELETE_USER_SESSION = "DELETE * FROM `USER_SESSIONS` WHERE 1=1 ";
	public static final String UPDATE_USER_SESSION = "UPDATE `USER_SESSIONS` SET `TIMEOUT` = ? ,`ENCRYPTION_KEY` = ? WHERE `SESSION_ID` = ? AND `USER_ID` = ?";
	public static final String CREATE_USER_SESSION = "INSERT INTO `USER_SESSIONS` (`SESSION_ID`,`USER_ID`,`CREATE_TIME`,`LAST_MODIFY_TIME`,`LANGUAGE`,`ENCRYPTION_KEY`,`TIMEOUT`,`IP_ADDRESS`,`HOST_NAME`,`BROWSER`,`COUNTRY`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public static final String SESSION_ID = "SESSION_ID";
	public static final String USER_ID = "USER_ID";
	public static final String CREATE_TIME = "CREATE_TIME";
	public static final String LAST_MODIFY_TIME = "LAST_MODIFY_TIME";
	public static final String LANGUAGE = "LANGUAGE";
	public static final String ENCRYPTION_KEY = "ENCRYPTION_KEY";
	public static final String TIMEOUT = "TIMEOUT";
	public static final String IP_ADDRESS = "IP_ADDRESS";
	public static final String HOST_NAME = "HOST_NAME";
	public static final String BROWSER = "BROWSER";
	public static final String COUNTRY = "COUNTRY";

}
