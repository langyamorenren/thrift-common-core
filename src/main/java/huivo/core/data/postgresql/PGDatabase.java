package huivo.core.data.postgresql;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Created by wuzhixin on 11/1/14.
 */

@Repository
public class PGDatabase implements ApplicationContextAware {

    static Logger LOG = LoggerFactory.getLogger(PGDatabase.class);
    private static PGDatabase pgDatabase = null;
    private static DruidDataSource _dataSource;

    public static PGDatabase getInstance() {
        if (null == pgDatabase) {
            pgDatabase = new PGDatabase();
        }
        return pgDatabase;
    }

    public static DruidDataSource createDriudDataSource(final String db_host, final int db_port, final String db_name, final String db_user_name, final String db_user_pwd) throws SQLException {

        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setName("huivo.org.postgres");
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://" + db_host + ":" + String.valueOf(db_port) + "/" + db_name);
        dataSource.setUsername(db_user_name);
        dataSource.setPassword(db_user_pwd);


        dataSource.setMaxActive(20);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxOpenPreparedStatements(50);

        dataSource.setTestWhileIdle(false);


        //dataSource.setTimeBetweenLogStatsMillis(60000);
        //dataSource.setMinEvictableIdleTimeMillis(300000); //如果打开此俩项会影响监控日志存入DB
        dataSource.setDefaultReadOnly(false);
        dataSource.setFilters("stat"); //STAT为监控统计使用，commonlogging,LOG4J之类日志用，防御SQL注入WALL

        LOG.info("Datasource params - " + dataSource.getUsername() + ", "
                + dataSource.getPassword() + ", " + dataSource.getUrl());

        return dataSource;
    }

    public static String unescape(String obj) {
        return obj.replace("\n", "<br/>");
    }

    public void init(final String db_host, final int db_port, final String db_name, final String db_user_name, final String db_user_pwd) throws SQLException {
        if(_dataSource != null)
            return;
        LOG.info("Begin init PostgreSQL database.");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            LOG.error("postgresql database driver not found. Please ensure the driver jar is in classpath.");
            throw new SQLException("postgresql database driver not found");
        }

        _dataSource = createDriudDataSource(db_host, db_port, db_name, db_user_name, db_user_pwd);

        Connection connection = _dataSource.getConnection();
        if (connection == null) {
            throw new SQLException("Unexpected: cannot get connection from pool");
        }
        connection.close();

    }

    public Statement getStatement() throws SQLException {
        Connection connection = _dataSource.getConnection();
        if (connection == null) {
            throw new SQLException("Unexpected: cannot get connection from pool");
        }
        return connection.createStatement();
    }

    public PreparedStatement getPreparedStatement(String sql) throws SQLException {
        Connection connection = _dataSource.getConnection();
        if (connection == null) {
            throw new SQLException("Unexpected: cannot get connection from pool");
        }
        return connection.prepareStatement(sql);
    }

    public Connection getConnection() throws SQLException {
        Connection connection = _dataSource.getConnection();
        if (connection == null) {
            throw new SQLException("Unexpected: cannot get connection from pool");
        }
        return connection;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, DruidDataSource> map = applicationContext.getBeansOfType(DruidDataSource.class);
        if(map != null && map.size() > 0){
            _dataSource = map.values().iterator().next();
        }
    }
}