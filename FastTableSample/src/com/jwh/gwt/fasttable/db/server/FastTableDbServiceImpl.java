package com.jwh.gwt.fasttable.db.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.jwh.gwt.fasttable.db.client.FastTableDbService;
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
				for (int type : answer.columnTypes) {
					switch (type) {
					case 0:
						
						break;

					default:
						break;
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