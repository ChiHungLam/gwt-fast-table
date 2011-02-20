package com.jwh.gwt.fasttable.db.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ServerException extends Exception implements IsSerializable {

	private static final long serialVersionUID = 1L;

	public ServerException() {
		super();
	}

	public ServerException(String arg0) {
		super(arg0);
	}

}
