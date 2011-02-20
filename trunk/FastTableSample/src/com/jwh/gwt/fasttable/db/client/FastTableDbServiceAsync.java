package com.jwh.gwt.fasttable.db.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.jwh.gwt.fasttable.db.shared.DbRows;

public interface FastTableDbServiceAsync {

	void getDbResults(String dbUrl, String driver, String sql, String user, String password,
			AsyncCallback<DbRows> callback);
}
