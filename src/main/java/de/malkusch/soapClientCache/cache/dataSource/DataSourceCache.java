package de.malkusch.soapClientCache.cache.dataSource;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import de.malkusch.soapClientCache.cache.Cache;
import de.malkusch.soapClientCache.cache.Payload;
import de.malkusch.soapClientCache.cache.dataSource.call.DataSourceExecution;
import de.malkusch.soapClientCache.cache.dataSource.call.DataSourceQuery;
import de.malkusch.soapClientCache.cache.exception.CacheException;


/**
 * Cache backed by a DataSource.
 * 
 * A pooled DataSource would be good choice (e.g. tomcat's jdbc-pool).
 * 
 * @see DataSource
 * @author Markus Malkusch <markus@malkusch.de>
 */
public class DataSourceCache<K extends Serializable, V>
		extends Cache<K, V> {

	private DataSource dataSource;
	
	private int adaptKey(K key) {
		return key.hashCode();
	}

	/**
	 * Set the seconds after which stored objects expire and the data source
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	public DataSourceCache(int expireSeconds, DataSource dataSource) throws IOException, ClassNotFoundException {
		super(expireSeconds);
		this.dataSource = dataSource;
		
		try {
			new DataSourceExecution(dataSource,
					"CREATE TABLE soapcache(" +
					"	cachekey INT NOT NULL PRIMARY KEY," +
					"	payload BLOB)").execute();
			
		} catch (SQLException e) {
			// assume that the table exists already.
			
		}
	}

	@Override
	protected Payload<V> getPayload(final K key) throws CacheException {
		try {
			return new DataSourceQuery<Payload<V>>(dataSource,
					"SELECT payload from soapcache WHERE cachekey=?") {

				@Override
				protected void setParameters(PreparedStatement statement) throws SQLException {
					statement.setInt(1, adaptKey(key));
				}

				@Override
				protected void readResults(ResultSet results) throws SQLException, IOException, ClassNotFoundException {
					setResult(getObject(results, 1));
				}
				
			}.query();

		} catch (SQLException e) {
			throw new CacheException(e);

		} catch (IOException e) {
			throw new CacheException(e);

		} catch (ClassNotFoundException e) {
			throw new CacheException(e);
			
		}
	}

	@Override
	public void put(final K key, final Payload<V> payload) throws CacheException {
		try {
			try {
				new DataSourceExecution(dataSource,
						"INSERT INTO soapcache (cachekey, payload) VALUES(?, ?)") {
					
					@Override
					protected void setParameters(PreparedStatement statement) throws SQLException {
						statement.setInt(1, adaptKey(key));
						statement.setObject(2, payload);
					}
					
				}.execute();
				
			} catch (SQLException e) {
				new DataSourceExecution(dataSource,
						"UPDATE soapcache SET payload=? WHERE cachekey=?") {

					@Override
					protected void setParameters(PreparedStatement statement) throws SQLException {
						statement.setObject(1, payload);
						statement.setInt(2, adaptKey(key));
					}
					
				}.execute();
				
			}
		} catch (SQLException e) {
			throw new CacheException(e);

		} catch (IOException e) {
			throw new CacheException(e);

		} catch (ClassNotFoundException e) {
			throw new CacheException(e);
			
		}
	}

	@Override
	protected void remove(final K key) throws CacheException {
		try {
			new DataSourceExecution(dataSource,
					"DELETE FROM soapcache WHERE cachekey=?") {

				@Override
				protected void setParameters(PreparedStatement statement) throws SQLException {
					statement.setInt(1, adaptKey(key));
				}
				
			}.execute();

		} catch (SQLException e) {
			throw new CacheException(e);

		} catch (IOException e) {
			throw new CacheException(e);

		} catch (ClassNotFoundException e) {
			throw new CacheException(e);
			
		}
	}
	
	@Override
	public void clear() throws CacheException {
		try {
			new DataSourceExecution(dataSource, "DELETE FROM soapcache").execute();
			
		} catch (SQLException e) {
			throw new CacheException(e);
			
		} catch (IOException e) {
			throw new CacheException(e);
			
		} catch (ClassNotFoundException e) {
			throw new CacheException(e);
			
		}
	}

}
