package web;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.AdminDao;
import dao.CostDao;
import entity.Admin;
import entity.Cost;
import util.ImageUtil;

public class MainServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String path = req.getServletPath();
		//判断请求路径
		if("/findcost.do".equals(path)){
			//查询资费
			findCost(req,res);
		}else if("/toaddcost.do".equals(path)){
			//打开增加资费页面
			toAddCost(req, res);
		}else if("/addcost.do".equals(path)){
			//增加保存资费
			addCost(req,res);
		}else if("/toupdatecost.do".equals(path)){
			//打开修改资费
			toUpdateCost(req,res);
		}else if("/updatecost.do".equals(path)){
			//修改保存资费
			updateCost(req,res);
		}else if("/deletecost.do".equals(path)){
			//删除数据
			delectCost(req,res);
		}else if("/tologin.do".equals(path)){
			//打开登录页面
			toLogin(req,res);
		}else if("/toindex.do".equals(path)){
			//打开主页
			toIndex(req,res);
		}else if("/login.do".equals(path)){
			//验证用户
			login(req,res);
		}else if("/logout.do".equals(path)){
			//
			logout(req,res);
		}else if("/createimg.do".equals(path)){
			//生成验证码
			createImg(req,res);
		}else{
			//错误路径
			throw new RuntimeException("查无此页");
		}
			
	}
	//生成验证码
	private void createImg(HttpServletRequest req, HttpServletResponse res) throws IOException {
		Object[] objs = ImageUtil.createImage();
		//将验证码存入session
		HttpSession session = req.getSession();
		session.setAttribute("imgcode", objs[0]);
		//获取图片
		BufferedImage img = (BufferedImage) objs[1];
		//将图片输出给浏览器
		res.setContentType("imgage/png");
		OutputStream out = res.getOutputStream();
		ImageIO.write(img, "png", out);
		out.close();
	}
	//退出时，销毁SessionId
	private void logout(HttpServletRequest req, HttpServletResponse res) throws IOException {
		HttpSession session = req.getSession();
		session.invalidate();
		res.sendRedirect("tologin.do");
	}
	//登录验证，验证账号 密码 验证码是否正确
	private void login(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String admincode = req.getParameter("adminCode");
		String password = req.getParameter("password");
		String code = req.getParameter("code");
		//检查验证码
		HttpSession session = req.getSession();
		String imgcode = (String) session.getAttribute("imgcode");
		if(code==null || !imgcode.equalsIgnoreCase(code)){
			req.setAttribute("error","验证码错误！");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
			//若验证码有误，直接结束方法，不用取调用数据库
			return;
		}
		Admin a = new AdminDao().findByCode(admincode);
		if(a == null){
			req.setAttribute("error", "账号有误，请重新输入");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
		}else if(!a.getPassword().equals(password)){
			req.setAttribute("error", "密码有误，请重写输入");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
		}else{
			//将账号存入cookie
			Cookie cookie = new Cookie("user", admincode);
			res.addCookie(cookie);
			//将账号存入session
			session.setAttribute("user", admincode);
			//重定向到主页
			res.sendRedirect("toindex.do");
		}
	}
	//主界面
	private void toIndex(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//转发到主界面
		req.getRequestDispatcher("WEB-INF/main/index.jsp").forward(req, res);
	}
	//登录界面
	private void toLogin(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/main/login.jsp").forward(req, res);
	}
	//删除数据
	private void delectCost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String id = req.getParameter("id");
		CostDao dao = new CostDao();
		dao.delete(new Integer(id));
		res.sendRedirect("findcost.do");
	}
	//修改资费
	private void updateCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		//1.接受参数
		req.setCharacterEncoding("utf-8");
		String costId = req.getParameter("costId");
		String name = req.getParameter("name");
		String baseDuration = req.getParameter("baseDuration");
		String baseCost = req.getParameter("baseCost");
		String unitCost = req.getParameter("unitCost");
		String descr = req.getParameter("descr");
		String costType = req.getParameter("costType");
		Cost c = new Cost();
		c.setCostId(new Integer(costId));
		c.setName(name);
		if(baseDuration!=null && baseDuration.length()>0){
			c.setBaseDuration(new Integer(baseDuration));
		}
		if(baseCost!=null && baseCost.length()>0){
			c.setBaseCost(new Double(baseCost));
		}
		if(unitCost!=null && unitCost.length()>0){
			c.setUnitCost(new Double(unitCost));
		}
		c.setCostType(costType);
		c.setDescr(descr);
		new CostDao().update(c);
		res.sendRedirect("findcost.do");
		
	}
	//将要修改的资费显示到修改页面
	private void toUpdateCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//		接受参数，获取id
		req.setCharacterEncoding("utf-8");
		int id = new Integer(req.getParameter("id"));
//		System.out.println(new Integer(id));
		
		Cost cost = new CostDao().findById(id);
//		System.out.println(cost.getDescr());
		System.out.println(cost.getName());
		req.setAttribute("cost", cost);
		req.getRequestDispatcher("WEB-INF/cost/update.jsp").forward(req, res);
		
//		req.setCharacterEncoding("utf-8");
//		Integer id = Integer.valueOf(req.getParameter("id"));
//		System.out.println(id);
//		Cost c = new CostDao().findById(id);
//		req.setAttribute("cost", c);
//		req.getRequestDispatcher("WEB-INF/cost/update.jsp").forward(req, res);
		
	}
	private void findCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//1.接受参数
		String page = req.getParameter("page");
		//2.获取常量
		ServletContext ctx = getServletContext();
		String size = ctx.getInitParameter("size");
		if(page==null || page.length()<0){
			page = "1";
		}
		//3.查询资费
		CostDao dao = new CostDao();
		List<Cost> list = dao.findByPage(new Integer(page), new Integer(size));
		//4.查询总行数
		int  rows = new CostDao().findRows();
		int total = rows/new Integer(size);
		if(rows%new Integer(size)!=0){
			total++;
		}
		//5.设置参数属性
		req.setAttribute("page", page);
		req.setAttribute("total", total);
		req.setAttribute("costs", list);
		//6.转发至find.jsp
		req.getRequestDispatcher("WEB-INF/cost/find.jsp").forward(req, res);
//		//查询所有的资费
//		CostDao dao = new CostDao();
//		List<Cost> list = dao.findAll();
		//转发至find.jsp
//		req.setAttribute("costs", list);
		//当前路径：/netctosse/find.do
		//目标路径：/netctosse/WEB-INF/cost/find.jsp
		//转发至find.jsp
	}
	//保存资费数据
	private void addCost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		//接受参数
		req.setCharacterEncoding("utf-8");
		String name = req.getParameter("name");
		String costType = req.getParameter("costType");
		String baseDuration = req.getParameter("baseDuration");
		String baseCost = req.getParameter("baseCost");
		String unitCost = req.getParameter("unitCost");
		String descr = req.getParameter("descr");
		//保存数据
		Cost cost = new Cost();
		cost.setName(name);
		cost.setCostType(costType);
		if(baseDuration!=null && baseDuration.length()>0){
			cost.setBaseDuration(Integer.valueOf(baseDuration));
		}
		if(baseCost!=null && baseCost.length()>0){
			cost.setBaseCost(Double.valueOf(baseCost));
		}
		if(unitCost!=null && unitCost.length()>0){
			cost.setUnitCost(Double.valueOf(unitCost));
		}
		cost.setDescr(descr);
		CostDao dao = new CostDao();
		dao.save(cost);
		//重定向到查询
		//当前：/netctoss/addcost.do
		//目标：/netctoss/findcost.do
		res.sendRedirect("findcost.do");
		
	}
	//打开资费页面
	protected void toAddCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("WEB-INF/cost/add.jsp").forward(req, res);
		
	}

}
