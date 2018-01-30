package dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import entity.Cost;
import util.DBUtil;

public class CostDao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//查找总行数
	public int findRows(){
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select count(*) from cost_kane";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询行数失败！",e);
		}finally{
			DBUtil.close(conn);
		}
		return 0;
	}
	//根据页面查找资费
	public List<Cost> findByPage(int page,int size){
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from(select c.*,rownum r from(select * from cost_kane "
					+ "order by cost_id)c)where r between ? and ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, (page-1)*size+1);
			ps.setInt(2, page*size);
			ResultSet rs = ps.executeQuery();
			List<Cost> list = new ArrayList<Cost>();
			while(rs.next()){
				list.add(createCost(rs));
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查找资费失败！",e);
		}finally{
			DBUtil.close(conn);
		}
	}
	//根据id删除资费
	public void delete(int id){
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "delete from cost_kane where cost_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("删除资费失败！",e);
		}finally{
			DBUtil.close(conn);
		}
		
	}
	//通过id修改资费
	public void update(Cost c){
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			//差了一个逗号
			String sql = "update cost_kane set name=?,base_duration=?,"
					+ "base_cost=?,unit_cost=?,descr=?,cost_type=?"
					+ "where cost_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, c.getName());
			//因为可以传入null值，所以设置objcet
			ps.setObject(2, c.getBaseDuration());
			ps.setObject(3, c.getBaseCost());
			ps.setObject(4, c.getUnitCost());
			ps.setString(5, c.getDescr());
			ps.setString(6, c.getCostType());
			ps.setInt(7, c.getCostId());
			ps.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			DBUtil.rollback(conn);
			throw new RuntimeException("修改资费失败！",e);
		}finally{
			DBUtil.rollback(conn);
		}
		
	}
	//通过id查找资费
	public Cost findById(int id){
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			//select * from cost_kane where cost_id=
			String sql = "select * from cost_kane where cost_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			Cost c = null;
			if(rs.next()){
				//没有接受数据
				c = createCost(rs);
			}
			return c;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查找资费失败！",e);
		}finally{
			DBUtil.close(conn);
		}
		
	}
	//查找所有数据
	public List<Cost> findAll(){
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			List<Cost> list = new ArrayList<Cost>();
			Statement smt = conn.createStatement();
			String sql = "select * from cost_kane order by cost_id";
			ResultSet rs = smt.executeQuery(sql);
			while(rs.next()){
				list.add(createCost(rs));
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询资费失败！",e);
		}finally{
			DBUtil.close(conn);
		}
	}
	private Cost createCost(ResultSet rs) throws SQLException {
		Cost c = new Cost();
		c.setCostId(rs.getInt("cost_id"));
		c.setName(rs.getString("name"));
		c.setBaseDuration(rs.getInt("base_duration"));
		c.setBaseCost(rs.getDouble("base_cost"));
		c.setUnitCost(rs.getDouble("unit_cost"));
		c.setStatus(rs.getString("status"));
		c.setCreatime(rs.getTimestamp("creatime"));
		c.setStartime(rs.getTimestamp("startime"));
		c.setDescr(rs.getString("descr"));
		c.setCostType(rs.getString("cost_type"));
		return c;
	}
	//增加数据
	public void save(Cost c){
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
//			conn.setAutoCommit(false);
			String sql = "insert into cost_kane values(cost_seq_kane.nextval,?,?,?,?,'1',?,sysdate,null,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, c.getName());
			//setInt setDouble 不允许传入null，但当前的业务
			//的确可能出入null。要想解决问题，需要把这些
			//数据当作普通的对象来看待以及传入
			ps.setObject(2, c.getBaseDuration());
			ps.setObject(3, c.getBaseCost());
			ps.setObject(4, c.getUnitCost());
			ps.setString(5, c.getDescr());
			ps.setString(6, c.getCostType());
			ps.executeUpdate();
//			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
//			DBUtil.rollback(conn);
			throw new RuntimeException("保存资费失败！",e);
		}finally{
			DBUtil.close(conn);
		}
	}
	public static void main(String[] args) {
		int id = new CostDao().findRows();
		System.out.println(id);
//		List<Cost> list = new CostDao().findByPage(1, 5);
//		for(Cost c:list){
//			System.out.println(c.getName());
//		}
//		Cost c = new Cost();
//		CostDao dao = new CostDao();
//		c.setName("包月");
////		c.setBaseDuration(660);
//		c.setBaseCost(66.0);
////		c.setUnitCost(0.6);
//		c.setDescr("很实惠");
//		c.setCostType("2");
//		dao.save(c);
		;
		CostDao dao = new CostDao();
		Cost c = dao.findById(5);
		System.out.println(c.getName());
//		c.setName("包月");
////		c.setBaseDuration(660);
//		c.setBaseCost(300.0);
////		c.setUnitCost(0.6);
//		c.setDescr("很爽");
//		c.setCostType("1");
//		dao.save(c);
	}
	

}
