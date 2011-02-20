package com.jwh.gwt.fasttable.db.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.jwh.gwt.fasttable.db.shared.DbRows;
import com.jwh.gwt.fasttable.db.shared.ServerException;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("fasttabledb")
public interface FastTableDbService extends RemoteService {
	public DbRows getDbResults(String dbUrl, String driver, String sql, String user, String password) throws ServerException ;
}