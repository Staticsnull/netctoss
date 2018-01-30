package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;

import entity.Admin;
import util.DBUtil;

public class AdminDao extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Admin findByCode(String code){
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from admin_info_kane where admin_code=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, code);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				Admin a = new Admin();
				a.setAdminId(rs.getInt("admin_id"));
				a.setAdminCode(rs.getString("admin_code"));
				a.setName(rs.getString("name"));
				a.setPassword(rs.getString("password"));
				a.setTelephone(rs.getString("telephone"));
				a.setEmail(rs.getString("email"));
				a.setEnrolldate(rs.getTimestamp("enrolldate"));
				return a;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询失败！",e);
		}finally{
			DBUtil.close(conn);
		}
		return null;
	}
	public static void main(String[] args) {
		String code = "caocao";
		Admin a = new AdminDao().findByCode(code);
		System.out.println(a.getPassword());
		System.out.println(a.getTelephone());
	}

}
