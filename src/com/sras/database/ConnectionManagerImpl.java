package com.sras.database;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import com.sras.datamodel.exceptions.TMException;

public final class ConnectionManagerImpl {

	private static Logger logger = Logger
			.getLogger(ConnectionManagerImpl.class);
	private boolean initialized = false;
	private String url;
	private String user;
	private String password;
	private String driverName;
	// The minimum number of connections that are to be created when the pool is
	// started.
	private int initialSize;
	// The maximum number of active connections that can be allocated from this
	// pool at the same time.
	private int maxActive;
	// The minimum number of active connections that can remain idle in the
	// pool, without extra ones being created
	private int minIdle;
	// The maximum number of connections that can remain idle in the pool,
	private int maxIdle;
	// The maximum number of milliseconds that the pool will wait for a
	// connection to be returned
	private long maxWait;

	private static BasicDataSource bds;
	private static final String VALIDATION_QUERY = "SELECT 1";
	private static ConnectionManagerImpl instance = new ConnectionManagerImpl();

	private ConnectionManagerImpl() {

	}

	public static ConnectionManagerImpl getConnectionManager() {
		return instance;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public long getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(long maxWait) {
		this.maxWait = maxWait;
	}

	/**
	 * @return the initialSize
	 */
	public int getInitialSize() {
		return initialSize;
	}

	/**
	 * @param initialSize
	 *            the initialSize to set
	 */
	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}

	/**
     *
     */
	public synchronized void initialize() {
		if (initialized)
			return;
		logger.debug("Initializing data source");
		bds = new BasicDataSource();
		bds.setUsername("admin");
		bds.setPassword("password");
		bds.setUrl("jdbc:mysql://127.0.0.1:3306/clicktobuy");
		bds.setDriverClassName("com.mysql.jdbc.Driver");
		bds.setInitialSize(10);
		bds.setMaxActive(20);
		bds.setMaxOpenPreparedStatements(120);
		bds.setPoolPreparedStatements(true);
		bds.setMinIdle(2);
		bds.setMaxIdle(3);
		bds.setMaxWait(300000);
		bds.setMinEvictableIdleTimeMillis(60000);
		bds.setTimeBetweenEvictionRunsMillis(10000);
		bds.setTestWhileIdle(true);
		bds.setValidationQuery(VALIDATION_QUERY);

		initialized = true;
		logger.debug("Initialized data source ");
	}

	/**
	 * cleaning up the connections that created by the BasicDataSource pool
	 */
	public void shutdownDriver() throws SQLException {
		if (initialized) {
			logger.debug("shutting down the data source for ");
			try {
				if (bds != null) {
					bds.close();
					logger.debug("finished shutting down the data source of type ");
				}
			} catch (SQLException e) {
				logger.error(
						"SQL Exception while shutting down connection pool ", e);
				throw e;
			}
		}
	}

	/**
	 * Connection object from mysql
	 * 
	 * @return Connection to the DB
	 */
	public Connection getConnection() throws TMException {
		logger.debug("Inside mySQLConnection method, trying to acquire MYSQL connection");
		try {
			if (!initialized)
				initialize();
			return bds.getConnection();
		} catch (SQLException sql) {
			logger.error("Unable to get connection ..", sql);
			throw new TMException("No Connection", sql.getMessage());
		}
	}

	/**
	 * Rollback a connection if autocommit on the connection is set to false.
	 * 
	 * @param conn
	 * @throws TMException
	 */
	public static void rollback(Connection conn) throws TMException {
		try {
			if (conn != null && !conn.getAutoCommit()) {
				logger.info("Rolling back");
				conn.rollback();
			}
		} catch (SQLException e) {
			logger.error("Error while rolling back", e);
			throw new TMException("SQL Exception", e.getMessage());
		}
	}

	/**
	 * Closes the connection (inturn returns the connection to the
	 * ConnectionPool)
	 * 
	 * @param conn
	 *            db connection instance
	 */
	public void returnConnection(Connection conn) throws TMException {
		try {
			if (conn != null) {
				conn.setAutoCommit(true);
				conn.close();
			}

		} catch (SQLException sql) {
			logger.error("Unable to close connection object", sql);
			throw new TMException("SQL Exception", sql.getMessage());
		}
	}

	/**
	 * @return the minIdle
	 */
	public int getMinIdle() {
		return minIdle;
	}

	/**
	 * @param minIdle
	 *            the minIdle to set
	 */
	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	/**
	 * @return the driverName
	 */
	public String getDriverName() {
		return driverName;
	}

	/**
	 * @param driverName
	 *            the driverName to set
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
}
