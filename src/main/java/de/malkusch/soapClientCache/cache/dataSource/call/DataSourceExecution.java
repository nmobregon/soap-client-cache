package de.malkusch.soapClientCache.cache.dataSource.call;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class DataSourceExecution extends DataSourceCall {

	public DataSourceExecution(DataSource dataSource, String query) {
		super(dataSource, query);
	}

	@Override
	void execute(PreparedStatement statement) throws SQLException {
		statement.executeUpdate();
	}

}
