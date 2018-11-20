package huivo.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import huivo.core.data.postgresql.PGDatabase;

/**
 * 数据库持久操作工具类
 * 
 * @author WeiWei
 *
 */
public class SimpleDAO {

	/**
	 * 数据库连接对象
	 */
	private Connection connection = null;

	/**
	 * Statement使用记录，方便close时统一关闭
	 */
	private List<Statement> statements = new ArrayList<Statement>();

	public SimpleDAO() {
		try {
			this.connection = PGDatabase.getInstance().getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void execute(String sql) throws SQLException {

		Statement statement = this.getStatement();

		statement.execute(sql);

	}

	public boolean insert(String sql, Object... parameters) {

		try {

			PreparedStatement preparedStatement = this.getPreparedStatement(sql);

			if (parameters != null) {
				for (int i = 0, l = parameters.length; i < l; i++) {
					preparedStatement.setObject(i + 1, parameters[i]);
				}
			}

			preparedStatement.execute();

			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			this.close();
		}

		return false;

	}

	public boolean update(String sql, Object... parameters) {

		try {

			PreparedStatement preparedStatement = this.getPreparedStatement(sql);

			if (parameters != null) {
				for (int i = 0, l = parameters.length; i < l; i++) {
					preparedStatement.setObject(i + 1, parameters[i]);
				}
			}

			preparedStatement.execute();

			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			this.close();
		}

		return false;

	}

	public boolean delete(String sql, Object... parameters) {

		try {

			PreparedStatement preparedStatement = this.getPreparedStatement(sql);

			if (parameters != null) {
				for (int i = 0, l = parameters.length; i < l; i++) {
					preparedStatement.setObject(i + 1, parameters[i]);
				}
			}

			preparedStatement.execute();

			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			this.close();
		}

		return false;

	}

	public boolean update(List<String> sqls) {

		try {

			Statement statement = this.getStatement();

			for (String sql : sqls) {
				statement.addBatch(sql);
			}

			statement.executeBatch();

			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			this.close();
		}

		return false;

	}
	
	public Map<String, Object> querySingle(String sql, Object... parameters) {

		try {

			PreparedStatement preparedStatement = this.getPreparedStatement(sql);

			if (parameters != null) {
				for (int i = 0, l = parameters.length; i < l; i++) {
					preparedStatement.setObject(i + 1, parameters[i]);
				}
			}

			ResultSet resultSet = preparedStatement.executeQuery();

			int columnCount = resultSet.getMetaData().getColumnCount();

			Map<String, Object> result = null;

			if (resultSet.next()) {
				result = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					result.put(resultSet.getMetaData().getColumnName(i).toLowerCase(), resultSet.getObject(i));
				}
			}

			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			this.close();
		}

		return null;

	}
	
	public List<Map<String, Object>> queryList(String sql, Object... parameters) {

		try {

			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			if (parameters != null) {
				for (int i = 0, l = parameters.length; i < l; i++) {
					preparedStatement.setObject(i + 1, parameters[i]);
				}
			}

			ResultSet resultSet = preparedStatement.executeQuery();

			int columnCount = resultSet.getMetaData().getColumnCount();

			List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();

			while (resultSet.next()) {
				Map<String, Object> result = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					result.put(resultSet.getMetaData().getColumnName(i).toLowerCase(), resultSet.getObject(i));
				}
				results.add(result);
			}

			return results;

		} catch (SQLException e) {
			e.printStackTrace();
			this.close();
		}

		return null;

	}

	/**
	 * 开启事物
	 * 
	 * @throws SQLException
	 */
	public void startTransaction() throws SQLException {
		this.connection.setAutoCommit(false);
	}

	/**
	 * 结束事物
	 * 
	 * @throws SQLException
	 */
	public void endTransaction() throws SQLException {
		this.connection.setAutoCommit(true);
	}

	/**
	 * 提交数据库操作
	 * 
	 * @throws SQLException
	 */
	public void commit() throws SQLException {
		this.connection.commit();
	}

	/**
	 * 回滚数据库操作
	 * 
	 * @throws SQLException
	 */
	public void rollback() throws SQLException {
		this.connection.rollback();
	}

	/**
	 * 释放数据库连接资源
	 */
	public void close() {
		this.close(false);
	}

	/**
	 * 强制释放数据库连接资源
	 * 
	 * @param isForce
	 */
	public void close(boolean isForce) {

		if (this.statements != null && !this.statements.isEmpty()) {
			for (Statement statement : statements) {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						statement = null;
					}
				}
			}
		}

		if (this.connection != null) {
			try {

				if (!this.connection.getAutoCommit() && !isForce) {
					return;
				}

				this.connection.close();

			} catch (SQLException e) {

				e.printStackTrace();

				try {
					this.connection.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				} finally {
					this.connection = null;
				}

			}
		}

	}

	/**
	 * 获取新的Statement对象
	 * 
	 * @return
	 * @throws SQLException
	 */
	private Statement getStatement() throws SQLException {
		Statement statement = this.connection.createStatement();
		statements.add(statement);
		return statement;
	}

	/**
	 * 获取PreparedStatement对象
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement getPreparedStatement(String sql) throws SQLException {
		PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
		statements.add(preparedStatement);
		return preparedStatement;
	}

}
