/*
 *
 * Copyright 2002, AppIQ, Inc.  All Rights Reserved
 */
package com.sras.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import oracle.sql.BLOB;
import oracle.sql.CLOB;

/**
 * This class is a wrapper class that serves to translate Java types to JDBC
 * types. Why is this necessary, you may ask? Doesn't this get done for you
 * already? Well, yes, mostly. There's this one nasty little exception: when
 * setting NULL values in a PreparedStatement, you need to know the JDBC type of
 * the value. JDK 1.4 has a getParameterMetaData() method on PreparedStatement,
 * but SQLAnywhere doesn't support it (and neither does JBoss), so we can't use
 * it. That means we're stuck with wrapping Java values inside of this class so
 * that we can distinguish the types of null values when necessary.
 */
public class SQLValue
{
	public static final String CHARACTER_ENCODING = "UTF-8";

	/*
	 * Various static methods that will construct the appropriate SQLValue
	 * valueect
	 */
	public static SQLValue Boolean(Boolean value)
	{
		return new SQLValue(value, Types.BIT);
	}

	public static SQLValue Boolean(boolean value)
	{
		return new SQLValue(Boolean.valueOf(value), Types.BIT);
	}

	public static SQLValue Integer(Integer value)
	{
		return new SQLValue(value, Types.INTEGER);
	}

	public static SQLValue Integer(int value)
	{
		return new SQLValue(Integer.valueOf(value), Types.INTEGER);
	}

	public static SQLValue Short(Short value)
	{
		return new SQLValue(value, Types.INTEGER);
	}

	public static SQLValue Short(short value)
	{
		return new SQLValue(Integer.valueOf(value), Types.INTEGER);
	}

	public static SQLValue String(String value)
	{
		return new SQLValue(value, Types.VARCHAR);
	}

	/**
	 * Ensure that the String is no longer than maxBytes. Java strings can have
	 * unicode characters. the maximum string length in Oracle is 4000 bytes. If
	 * more than 4000 bytes is written to a VARCHAR2(4000 CHAR) it throws
	 * ORA-01461: can bind a LONG value only for insert into LONG column. google
	 * ORA-01461 unicode
	 * 
	 * @see http://java.sun.com/mailers/techtips/corejava/2006/tt0822.html
	 * @author deackoff
	 */
	public static SQLValue String(String value, int maxBytes)
	{
		try
		{
			byte[] valueBytes = value.getBytes(CHARACTER_ENCODING);
			int valueBytesLength = valueBytes.length;
			if (valueBytesLength <= maxBytes)
				return new SQLValue(value, Types.VARCHAR);
			int newLength = Math.min(value.length(), maxBytes);
			String newValue = value.substring(0, newLength);
			while (true)
			{
				valueBytes = newValue.getBytes(CHARACTER_ENCODING);
				valueBytesLength = valueBytes.length;
				if (valueBytesLength <= maxBytes)
					return new SQLValue(newValue, Types.VARCHAR);
				newLength = newLength - Math.max(1, ((valueBytesLength - maxBytes) / 2));
				newValue = newValue.substring(0, newLength);
			}
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static SQLValue String(Collection<?> value)
	{
		return new SQLValue(value != null ? value.toString() : (String) null, Types.VARCHAR);
	}

	public static SQLValue Long(Long value)
	{
		return new SQLValue(value, oracle.jdbc.OracleTypes.NUMBER);
	}

	public static SQLValue Long(long value)
	{
		return new SQLValue(Long.valueOf(value), oracle.jdbc.OracleTypes.NUMBER);
	}

	public static SQLValue Float(Float value)
	{
		return new SQLValue(value, Types.REAL);
	}

	public static SQLValue Float(float value)
	{
		return new SQLValue(new Float(value), Types.REAL);
	}

	public static SQLValue Double(Double value)
	{
		return new SQLValue(value, Types.DOUBLE);
	}

	public static SQLValue Double(double value)
	{
		return new SQLValue(new Double(value), Types.DOUBLE);
	}

	public static SQLValue BigDecimal(BigDecimal value)
	{
		return new SQLValue(value, Types.DECIMAL);
	}

	public static SQLValue BinaryStream(byte[] value)
	{
		return new SQLValue(new ByteArrayInputStream(value), oracle.jdbc.OracleTypes.RAW);
	}

	public static SQLValue BinaryStream(String value)
	{
		return SQLValue.BinaryStream(value.getBytes());
	}

	public SQLValue BinaryStream(InputStream value)
	{
		return new SQLValue(value, oracle.jdbc.OracleTypes.RAW);
	}

	public static SQLValue Raw(byte[] value)
	{
		return new SQLValue(value, oracle.jdbc.OracleTypes.RAW);
	}

	public static SQLValue Raw(String value)
	{
		if (value == null)
			return new SQLValue(null, oracle.jdbc.OracleTypes.RAW);

		try
		{
			return new SQLValue(value.getBytes(CHARACTER_ENCODING), oracle.jdbc.OracleTypes.RAW);
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static SQLValue Date(Date value)
	{
		return new SQLValue(value, Types.DATE);
	}

	public static SQLValue Time(Time value)
	{
		return new SQLValue(value, Types.TIME);
	}

	public static SQLValue Timestamp(Timestamp value)
	{
		return new SQLValue(value, Types.TIMESTAMP);
	}

	public static SQLValue Timestamp(java.util.Date value)
	{
		return new SQLValue(value != null ? new Timestamp(value.getTime()) : null, Types.TIMESTAMP);
	}

	public static SQLValue Clob(Clob value)
	{
		return new SQLValue(value, Types.CLOB);
	}

	public static SQLValue Blob(Blob value)
	{
		return new SQLValue(value, Types.BLOB);
	}

	public static SQLValue Array(Array value)
	{
		return new SQLValue(value, Types.ARRAY);
	}

	public static SQLValue SQLData(SQLData value)
	{
		return new SQLValue(value, Types.STRUCT);
	}

	public static SQLValue Ref(Ref value)
	{
		return new SQLValue(value, Types.REF);
	}

	public static SQLValue Object(Object value, int type)
	{
		if (value instanceof java.util.Date)
			return new SQLValue(new Timestamp(((java.util.Date) value).getTime()), Types.TIMESTAMP);

		return new SQLValue(value, type);
	}

	/*
	 * Various static methods that will construct a primitive type wrapper or
	 * return null if the result is really null. JDBC has a braindead interface
	 * that requires you to get the result first and then check for null by
	 * calling wasNull() afterwards. Duh.
	 */
	public static String String(ResultSet rs, int colIndex) throws SQLException
	{
		return (rs.getString(colIndex));
	}

	public static Boolean Boolean(ResultSet rs, int colIndex) throws SQLException
	{
		boolean d = rs.getBoolean(colIndex);
		return rs.wasNull() ? null : Boolean.valueOf(d);
	}

	public static Blob Blob(ResultSet rs, int colIndex) throws SQLException
	{
		Blob b = rs.getBlob(colIndex);
		return rs.wasNull() ? null : b;
	}

	public static Integer Integer(ResultSet rs, int colIndex) throws SQLException
	{
		int d = rs.getInt(colIndex);
		return rs.wasNull() ? null : Integer.valueOf(d);
	}

	public static Short Short(ResultSet rs, int colIndex) throws SQLException
	{
		short d = rs.getShort(colIndex);
		return rs.wasNull() ? null : Short.valueOf(d);
	}

	public static Long Long(ResultSet rs, int colIndex) throws SQLException
	{
		long d = rs.getLong(colIndex);
		return rs.wasNull() ? null : Long.valueOf(d);
	}

	public static Float Float(ResultSet rs, int colIndex) throws SQLException
	{
		float d = rs.getFloat(colIndex);
		return rs.wasNull() ? null : Float.valueOf(d);
	}

	public static Double Double(ResultSet rs, int colIndex) throws SQLException
	{
		double d = rs.getDouble(colIndex);
		return rs.wasNull() ? null : Double.valueOf(d);
	}

	public static byte[] ByteArray(ResultSet rs, int colIndex) throws SQLException
	{
		// Oracle-specific, because the InputStream doesn't work.
		BLOB blob = (BLOB) rs.getBlob(colIndex);
		if (blob == null)
			return null;

		long len = blob.length();
		if (len <= 0)
			return null;

		byte[] bytes = new byte[(int) len]; // hmm, well, what if we have a
											// REALLY big blob?
		int bufferSize = blob.getBufferSize();

		if (bufferSize > len)
			bufferSize = (int) len;

		for (long bytesRead = 0; bytesRead < len; bytesRead += bufferSize)
		{
			byte[] chunk = blob.getBytes(bytesRead + 1, bufferSize);
			System.arraycopy(chunk, 0, bytes, (int) bytesRead, chunk.length);
		}

		return bytes;
	}

	public static byte[] Raw(ResultSet rs, int colIndex) throws SQLException
	{
		byte[] result = rs.getBytes(colIndex);
		return rs.wasNull() ? null : result;
	}

	public static Date Date(ResultSet rs, int colIndex) throws SQLException
	{
		Timestamp d = rs.getTimestamp(colIndex);
		return rs.wasNull() ? null : new Date(d.getTime());
	}

	public static Timestamp Timestamp(ResultSet rs, int colIndex) throws SQLException
	{
		Timestamp t = rs.getTimestamp(colIndex);
		return rs.wasNull() ? null : t;
	}

	public static InputStream BinaryStream(ResultSet rs, int colIndex) throws SQLException
	{
		return rs.getBinaryStream(colIndex);
	}

	public static String BinaryStreamString(ResultSet rs, int colIndex) throws SQLException,
			IOException
	{
		InputStream is = rs.getBinaryStream(colIndex);
		StringWriter sw = new StringWriter();
		int chunk;

		while ((chunk = is.read()) != -1)
			sw.write(chunk);
		sw.flush();

		return sw.getBuffer().toString();
	}

	public static String StringFromRaw(ResultSet rs, int colIndex) throws SQLException
	{
		byte[] bytes = ByteArray(rs, colIndex);
		if (bytes == null)
			return null;
		try
		{
			return new String(bytes, CHARACTER_ENCODING);
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static String StringFromCLOB(ResultSet rs, int colIndex) throws SQLException
	{
		String s = null;
		CLOB clob = (CLOB) rs.getClob(colIndex);

		if (clob != null)
			s = clob.getSubString(1, (int) clob.length());

		return s;
	}

	public static Collection<String> Collection(ResultSet rs, int colIndex) throws SQLException
	{
		String str = rs.getString(colIndex);
		if (str == null)
			return null;
		ArrayList<String> list = new ArrayList<String>();
		StringTokenizer cst = new StringTokenizer(str.substring(1, str.length() - 1), ",");
		while (cst.hasMoreTokens())
		{
			list.add(cst.nextToken().trim());
		}
		return list;

	}

	// accessors
	public int getType()
	{
		return type;
	}

	public Object getValue()
	{
		return value;
	}

	public boolean isNull()
	{
		return value == null;
	}

	public String toString()
	{
		return value != null ? value.toString() : NULL;
	}

	// private methods
	private SQLValue(Object value, int type)
	{
		this.value = value;
		this.type = type;
	}

	private static final String NULL = "null";

	// member data
	private Object value;
	private int type;
}
