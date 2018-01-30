package web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {

	public void destroy() {

	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
//		RequestFacade（鼠标）  HttpServletRequest（设备）  ServletRequest(东西）
		//1.将参数强转为子类型
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		//2.获取路径，有3个路径不许要判断，将他们排除
		String[] paths = new String[]{
			"/tologin.do","/login.do","/createimg"	
		};
		String path = request.getServletPath();
		for(String s:paths){
			//若路径是当前路径之一，请求继续执行
			if(s.equals(path)){
				chain.doFilter(req, res);
				//同时结束方法
				return;
			}
		}
		//获取sessionId，判断用户是否登录
		HttpSession session = request.getSession();
		String user = (String) session.getAttribute("user");
		if(user == null){
			//如果未登录，重定向到登录界面
			response.sendRedirect(request.getContextPath()+"/tologin.do");
		}else{
			//如果已经登录，请求继续执行
			chain.doFilter(req, res);
		}
		
		

	}

	public void init(FilterConfig arg0) throws ServletException {

	}

}
