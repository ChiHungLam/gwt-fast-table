package com.jwh.gwt.fasttable.db.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.jwh.gwt.fasttable.db.client.FastTableDbService;
import com.jwh.gwt.fasttable.db.shared.DbRow;
import com.jwh.gwt.fasttable.db.shared.DbRows;
import com.jwh.gwt.fasttable.db.shared.ServerException;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class FastTableDbServiceImpl extends RemoteServiceServlet implements FastTableDbService {

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html
	 *            the html string to escape
	 * @return the escaped string
	 */
	// private String escapeHtml(String html) {
	// if (html == null) {
	// return null;
	// }
	// return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
	// .replaceAll(">", "&gt;");
	// }

	@Override
	public DbRows getDbResults(String dbUrl, String driver, String sql, String user, String password)
			throws ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	public void getRows(String jdbcURL, String driver, String sql, String user, String password)
			throws ServerException {
		// final String jdbcURL = "jdbc:oracle:thin:@xxx:1521:ORCL";
		// final String oracleDriver = "oracle.jdbc.driver.OracleDriver";
		Connection connection = null;
		final DbRows answer = new DbRows();
		try {
			Class.forName(driver).newInstance();
			connection = DriverManager.getConnection(jdbcURL, user, password);
			final Statement stmt = connection.createStatement();
			final ResultSet rs = stmt.executeQuery(sql);
			final ResultSetMetaData metaData = rs.getMetaData();
			final int columnCount = metaData.getColumnCount();
			for (int i = 0; i < columnCount; i++) {				
				answer.columnNames.add(metaData.getColumnLabel(i));				
				answer.columnTypes.add(metaData.getColumnType(i));
			}
			while (rs.next()) {
				for (int col = 0; col < columnCount; col++) {				
				final DbRow row = new DbRow();
				answer.rows.add(row);
				for (int type : answer.columnTypes) {
					switch (type) {
					case Types.ARRAY:
						row.add(rs.getBytes(col));
						break;
					case Types.BIGINT:
						row.add(rs.getInt(col));
						break;
					case Types.BINARY:
						row.add(rs.getBoolean(col));
						break;
					case Types.BIT:
						row.add(rs.getShort(col));
						break;
					case Types.BLOB:
						row.add(rs.getBlob(col).toString());
						break;
					case Types.BOOLEAN:
						row.add(rs.getBoolean(col));
						break;
					case Types.CHAR:
						row.add(rs.getByte(col));
						break;
					case Types.CLOB:
						row.add(rs.getClob(col).toString());
						break;
					case Types.DATALINK:
						row.add(rs.getBytes(col));
						break;
					case Types.DATE:
						row.add(rs.getDate(col));
						break;
					case Types.DECIMAL:
						row.add(rs.getBigDecimal(col));
						break;
					case Types.DOUBLE:
						row.add(rs.getDouble(col));
						break;
					case Types.FLOAT:
						row.add(rs.getFloat(col));
						break;
					case Types.INTEGER:
						row.add(rs.getInt(col));
						break;
					case Types.JAVA_OBJECT:
						row.add(rs.getBytes(col));
						break;
					case Types.LONGNVARCHAR:
						row.add(rs.getBytes(col));
						break;
					case Types.LONGVARBINARY:
						row.add(rs.getBytes(col));
						break;
					case Types.LONGVARCHAR:
						row.add(rs.getBytes(col));
						break;
					case Types.NCHAR:
						row.add(rs.getBytes(col));
						break;
					case Types.NCLOB:
						row.add(rs.getNClob(col).toString());
						break;
					case Types.NUMERIC:
						row.add(rs.getDouble(col));
						break;
					case Types.NVARCHAR:
						row.add(rs.getBytes(col));
						break;
					case Types.OTHER:
						row.add(rs.getBytes(col));
						break;
					case Types.REAL:
						row.add(rs.getDouble(col));
						break;
					case Types.ROWID:
						row.add(rs.getRowId(col).getBytes());
						break;
					case Types.SMALLINT:
						row.add(rs.getInt(col));
						break;
					case Types.SQLXML:
						row.add(rs.getBytes(col));
						break;
					case Types.STRUCT:
						row.add(rs.getBytes(col));
						break;
					case Types.TIME:
						// TODO serializable?
						row.add(rs.getTime(col));
						break;
					case Types.TIMESTAMP:
						row.add(rs.getTimestamp(col));
						break;
					case Types.TINYINT:
						row.add(rs.getInt(col));
						break;
					case Types.VARBINARY:
						row.add(rs.getBytes(col));
						break;
					case Types.VARCHAR:
						row.add(rs.getBytes(col));
						break;
					default:
						row.add(rs.getBytes(col));
						break;
					}
				}
				}
			}
		} catch (SQLException e) {
			throw new ServerException(e.getMessage());
		} catch (InstantiationException e) {
			throw new ServerException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new ServerException(e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new ServerException(e.getMessage());
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}
	}
}