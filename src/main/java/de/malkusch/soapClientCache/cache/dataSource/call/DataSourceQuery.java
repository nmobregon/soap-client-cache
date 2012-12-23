package de.malkusch.soapClientCache.cache.dataSource.call;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

abstract public class DataSourceQuery<T> extends DataSourceCall {
	
	private T result;

	public DataSourceQuery(DataSource dataSource, String query) {
		super(dataSource, query);
	}
	
	protected void setResult(T result) {
		this.result = result;
	}
	
	protected T getObject(ResultSet resultSet, int index) throws SQLException, IOException, ClassNotFoundException {
		// Object object = results.getObject(1);
		
		byte[] bytes = resultSet.getBytes(1);
		if (bytes == null) {
			return null;
			
		}
	    ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
	    Object object = objectStream.readObject();
	
	    @SuppressWarnings("unchecked")
	    T castedObject = (T) object;
	    
	    return castedObject;
	}
	
	abstract protected void readResults(ResultSet results) throws SQLException, IOException, ClassNotFoundException;
	
	public T query() throws SQLException, IOException, ClassNotFoundException {
		execute();
		return result;
	}

	@Override
	void execute(PreparedStatement statement) throws SQLException, IOException, ClassNotFoundException {
		ResultSet results = statement.executeQuery();
		try {
			while (results.next()) {
				readResults(results);
				
			}
		} finally {
			results.close();
			
		}
	}

}
