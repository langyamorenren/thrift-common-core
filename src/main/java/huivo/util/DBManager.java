package huivo.util;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import huivo.core.data.postgresql.PGDatabase;

/**
 * 数据库管理类
 * 
 * @author WeiWei
 *
 */
public class DBManager {

	private static final Logger LOG = LoggerFactory.getLogger(DBManager.class);

	private Connection connection;

	/**
	 * 获取Connection对象
	 * 
	 * @return
	 */
	public Connection getConnection() {

		try {
			this.connection = PGDatabase.getInstance().getConnection();
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
		}

		return this.connection;

	}

	/**
	 * 开启事物，使用默认隔离级别
	 * 
	 * @throws SQLException
	 */
	public void startTransaction() throws SQLException {
		this.startTransaction(Connection.TRANSACTION_READ_COMMITTED);
	}

	/**
	 * 开启事物，使用指定隔离级别
	 * 
	 * @throws SQLException
	 */
	public void startTransaction(int level) throws SQLException {
		this.connection.setAutoCommit(false);
		this.connection.setTransactionIsolation(level);
	}

	/**
	 * 结束事物，并执行一次提交
	 * 
	 * @throws SQLException
	 */
	public void endTransaction() throws SQLException {
		this.connection.commit();
		this.connection.setAutoCommit(true);
		this.connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
	}

	/**
	 * 回滚事物
	 */
	public void rollback() {
		try {
			this.connection.rollback();
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 提交事物
	 * @throws SQLException 
	 */
	public void commit() throws SQLException {
		this.connection.setAutoCommit(true);
		this.connection.commit();
	}

	/**
	 * 关闭连接
	 */
	public void close() {

		if (this.connection != null) {
			try {
				this.connection.close();
			} catch (SQLException e) {
				LOG.error(e.getMessage(), e);
			} finally {
				this.connection = null;
			}
		}

	}

}
