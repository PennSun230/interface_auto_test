package util;

import data.Constants;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author penn
 * @date 2021/4/10 --- 11:10
 * 柠檬班学习
 */
public class JDBCUtils {
    public static Connection getConnection() {
        String url="jdbc:mysql://"+ Constants.DB_BASE_URI+"/"+Constants.DB_NAME+"?useUnicode=true&characterEncoding=utf-8";
        String user=Constants.DB_USERNAME;
        String password= Constants.DB_PWD;

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return connection;
    }

    public static void closeConnection(Connection connection){
        //判空
        if(connection!=null) {
            //关闭数据库连接
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    public static void update(String sql) {
        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            queryRunner.update(connection, sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭数据库链接
            closeConnection(connection);
        }
    }

    public static Map<String,Object>  queryOne (String sql) {
        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        Map<String,Object> result = null;
        try {
            result =queryRunner.query(connection, sql,new MapHandler() );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭数据库链接
            closeConnection(connection);
        }
        return result;
    }

    public static List<Map<String,Object>> queryAll (String sql) {
        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        List<Map<String,Object>> result = null;
        try {
            result =queryRunner.query(connection, sql,new MapListHandler() );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭数据库链接
            closeConnection(connection);
        }
        return result;
    }

    public static Object querySingleCount (String sql) {
        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        Object result = null;
        try {
            result =queryRunner.query(connection, sql,new ScalarHandler<Object>() );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭数据库链接
            closeConnection(connection);
        }
        return result;
    }
}
