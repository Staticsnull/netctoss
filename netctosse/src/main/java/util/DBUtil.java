package util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;

public class DBUtil{
	//声明连接池
	private static BasicDataSource dataSource = new BasicDataSource();
	static{
		/**
		 * 1,创建参数
		 * 2，创建连接池
		 * 3，设置参数
		 */
		Properties p = new Properties();
		try {
			p.load(DBUtil.class.getClassLoader().getResourceAsStream("db.properties"));
			String driver = p.getProperty("driver");
			String url = p.getProperty("url");
			String user = p.getProperty("user");
			String pwd = p.getProperty("pwd");
			String initsize = p.getProperty("initsize");
			String maxsize = p.getProperty("maxsize");
			//设置参数
			
			dataSource.setDriverClassName(driver);
			dataSource.setUrl(url);
			dataSource.setUsername(user);
			dataSource.setPassword(pwd);
			dataSource.setInitialSize(Integer.parseInt(initsize));
			dataSource.setMaxActive(Integer.parseInt(maxsize));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("加载db.properties失败",e);
		} 
		
		
	}
	public static Connection getConnection() throws SQLException{
		Connection conn = dataSource.getConnection();
		return  conn;
	}
	/*
	 * 目前连接是由连接池创建的，连接的实现是由连接池提供的
	 * 连接池将连接对象的close方法改为归还连接的逻辑
	 */
	public static void close(Connection conn){
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("关闭连接失败！",e);
			}
		}
	}
	public static void rollback(Connection conn){
		try {
			if(conn!=null){
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("回滚失败！",e);
		}
	}
	
}
