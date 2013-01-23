package com.sras.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import com.sras.database.ConnectionManagerImpl;
import com.sras.datamodel.DataModel;
import com.sras.datamodel.exceptions.DataModelException;
import com.sras.datamodel.exceptions.InvalidSQLValueException;
import com.sras.datamodel.exceptions.TMException;

public abstract class BaseDao
{
	private static Logger logger = Logger.getLogger(BaseDao.class.getName());

	protected DataModel datamodel;
	
	public BaseDao(DataModel datamodel)
	{
		this.datamodel = datamodel;
	}

	public abstract long create() throws DataModelException, TMException,
			SQLException;

	public abstract long update() throws DataModelException, TMException,
			SQLException;

	public abstract ArrayList<DataModel> enumerate() throws DataModelException,
			TMException, SQLException;

	public abstract int delete() throws DataModelException, TMException,
			SQLException;

	public abstract DataModel read() throws DataModelException, TMException,
			SQLException;

	public abstract boolean validateRules() throws TMException;

	public Connection getConnection() throws TMException, SQLException
	{
		return ConnectionManagerImpl.getConnectionManager().getConnection();
	}

	public void closeConnection(Connection connection) throws TMException, SQLException
	{
		if (connection != null && !connection.isClosed())
		{
			ConnectionManagerImpl.getConnectionManager().returnConnection(connection);
		}
	}

	/**
	 * @param stmt
	 *            PreparedStatement instance A convenience method for closing a
	 *            statement only. Calls the three param close method.
	 */
	public void close(PreparedStatement stmt) throws TMException
	{
		close(stmt, null);
	}

	/**
	 * This method attempts to close each resource in the proper order. It logs,
	 * but does not propogate, any SQLException incurred during the closing.
	 * 
	 * @param stmt
	 *            the statement to close
	 * @param rs
	 *            the resultset to close
	 */
	public void close(PreparedStatement stmt, ResultSet rs) throws TMException
	{
		logger.debug("in close(pstmt, rset)");
		if (rs != null)
			try
			{
				rs.close();
			}
			catch (SQLException se1)
			{
				logger.error("Error closing resource resultset .", se1);
				throw new TMException("RSET_CLOSE", "Unable to close the Resultset instance");
			}

		if (stmt != null)
			try
			{
				stmt.close();
			}
			catch (SQLException se2)
			{
				logger.error("Error closing resource statement.", se2);
				throw new TMException("STMT_CLOSE", "Unable to close the statement instance");
			}
	}

	public static boolean isEmptyOrNull(String msg)
	{
		return (msg == null || msg.trim().equalsIgnoreCase(""));
	}

	public void prepareStatmentAttribute(PreparedStatement pstmt, int type, String value, int count)
			throws SQLException
	{
		if (value == null || value.trim().equalsIgnoreCase(""))
			pstmt.setNull(count, type);
		else
		{
			value = value.trim();
			switch (type)
			{
				case java.sql.Types.VARCHAR:
					pstmt.setString(count, value);
					break;
				case java.sql.Types.INTEGER:
					long l = Long.parseLong(value);
					if (l <= 0)
						pstmt.setNull(count, type);
					else
						pstmt.setInt(count, Integer.parseInt(value));
					break;
				case java.sql.Types.BOOLEAN:
					pstmt.setBoolean(count, Boolean.valueOf(value));
					break;
				default:
					throw new SQLException("INVALID_DATA_TYPE");
			}

		}
	}

	protected void bindVariables(PreparedStatement statement, Object[] data) throws SQLException
	{
		for (int i = 0; i < data.length; i++)
		{
			if (data[i] == null)
				throw new InvalidSQLValueException("NULL SQLValue object in bindVariables");

			if (!(data[i] instanceof SQLValue))
				throw new InvalidSQLValueException("Bind variable type "
						+ data[i].getClass().getName() + " should be of type SQLValue");
			SQLValue val = (SQLValue) data[i];
			int type = val.getType(); // java.sql.Types
			Object obj = val.getValue();

			if (val.isNull())
				statement.setNull(i + 1, val.getType());
			else if (type == OracleTypes.RAW)
				try
				{
					if (obj instanceof InputStream)
					{
						InputStream stream = (InputStream) val.getValue();
						statement.setBinaryStream(i + 1, stream, stream.available());
					}
					else if (obj instanceof byte[])
						statement.setBytes(i + 1, (byte[]) obj);
				}
				catch (IOException e)
				{
					throw new InvalidSQLValueException("Failure to retrieve SQL BinaryStream value");
				}
			else if (type == java.sql.Types.VARCHAR)
				statement.setString(i + 1, obj.toString());
			else if (obj instanceof Short)
				// Oracle-ism. Need to deal with short specially
				statement.setLong(i + 1, ((Short) obj).intValue());
			else if (obj instanceof oracle.sql.BLOB) // add Oracle BLOB type to
														// baseImple
				statement.setBlob(i + 1, (oracle.sql.BLOB) obj);
			else
				statement.setObject(i + 1, obj);
		}
	}

	/**
	 * Prepare, bind and execute the given SQL statement on the supplied
	 * connection
	 * 
	 * @param sql
	 *            The SQL statement
	 * @param bindVars
	 *            The collection of bind variables
	 * @return int The result of the statement.executeUpdate() call
	 * @throws SQLException
	 *             If bad stuff happens
	 * @throws TMException 
	 */
	protected int executeUpdate(String sql, Collection<SQLValue> bindVars) throws DataModelException,
			SQLException, TMException
	{
		PreparedStatement statement = null;
		try
		{
			Connection connection = getConnection(); 
			statement = connection.prepareStatement(sql);
			if (bindVars != null)
				bindVariables(statement, bindVars.toArray());
				logger.debug("executeUpdate: " + sql + ", bindVars: "
						+ (bindVars == null ? "none" : bindVars.toString()));
			return statement.executeUpdate();
		}
		finally
		{
			if (statement != null)
				statement.close();
			statement = null;
		}
	}

	/**
	 * Prepare bind and execute the given SQL query on the supplied connection.
	 * 
	 * @param sql
	 *            The SQL statement
	 * @param bindVars
	 *            The vector of bind variables
	 * @return ResultSet The result of the query
	 * @throws SQLException
	 *             If bad stuff happens
	 * @throws TMException
	 */
	protected ResultSet executeQuery(String sql, Collection<SQLValue> bindVars) throws DataModelException,
			SQLException, TMException
	{
		return executeQuery(sql, bindVars, ResultSet.TYPE_FORWARD_ONLY);
	}

	/**
	 * Prepare bind and execute the given SQL query on the supplied connection.
	 * 
	 * @param sql
	 *            The SQL statement
	 * @param bindVars
	 *            The vector of bind variables
	 * @param resultSetType
	 *            The result type
	 * @return ResultSet The result of the query
	 * @throws SQLException
	 *             If bad stuff happens
	 * @throws TMException
	 */
	protected ResultSet executeQuery(String sql, Collection<SQLValue> bindVars, int resultSetType)
			throws DataModelException, SQLException, TMException
	{
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement(sql, resultSetType,
				ResultSet.CONCUR_READ_ONLY);
		if (bindVars != null)
			bindVariables(statement, bindVars.toArray());

		logger.debug("executeQuery: " + sql + ", bindVars: "
				+ (bindVars == null ? "none" : bindVars.toString()));
		return statement.executeQuery();
	}

	public static final String ID = "ID";
	public static final char LIKE_MODIFIER = '%';
	public static final String PSTMT_MODIFIER = "?";
	public static final char SPACE = ' ';
	public static final char COMMA = ',';
	public static final String COMMA_STR = ",";
	public static final char LEFT_PARANTHASIS = '(';
	public static final char RIGHT_PARANTHASIS = ')';
	public static final String RIGHT_PARANTHASIS_STR = ")";
	public static final String MYSQL_ESC_CHAR = "`";
	public static final String TABLE = " table ";
	public static final String WHERE = " where ";
	public static final String FROM = " from ";
	public static final String AND = " and ";
	public static final String OR = " or ";
	public static final String SELECT = " select ";
}
