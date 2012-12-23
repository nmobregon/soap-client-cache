package de.malkusch.soapClientCache.cache.dataSource.call;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

abstract public class DataSourceCall {
	
	private DataSource dataSource;
	private String query;

	public DataSourceCall(DataSource dataSource, String query) {
		this.dataSource = dataSource;
		this.query = query;
	}

	abstract void execute(PreparedStatement statement) throws SQLException, IOException, ClassNotFoundException;
	
	protected void setParameters(PreparedStatement statement) throws SQLException {
	}
	
	public void execute() throws SQLException, IOException, ClassNotFoundException {
		Connection connection = dataSource.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			try {
				setParameters(statement);
				execute(statement);
				if (! connection.getAutoCommit()) {
					connection.commit();
					
				}
			} finally {
				statement.close();
				
			}
		} finally {
			connection.close();
			
		}
	}

}
