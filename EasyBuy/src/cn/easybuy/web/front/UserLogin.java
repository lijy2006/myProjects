package cn.easybuy.web.front;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.easybuy.entity.User;
import cn.easybuy.service.user.UserService;
import cn.easybuy.service.user.UserServiceImpl;
import cn.easybuy.utils.EmptyUtils;
import cn.easybuy.utils.ReturnResult;
import cn.easybuy.utils.SecurityUtils;

/**
 * Servlet implementation class UserLogin
 */
@WebServlet("/UserLogin")
public class UserLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserService userService;
	@Override
	public void init() throws ServletException {
		userService = new UserServiceImpl(); 
	}
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserLogin() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取事件参数
		String action = request.getParameter("action");
		try {
			if ("toLogin".equals(action)) {
				this.toLogin(request, response);
			}else if("login".equals(action)) {
				this.login(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理登录
	 * @param request
	 * @param response
	 */
	private ReturnResult login(HttpServletRequest request, HttpServletResponse response) {
		ReturnResult result=new ReturnResult();
        //获取页面参数
        String loginName=request.getParameter("loginName");
        String password=request.getParameter("password");
        //到数据库中查询对应的信息
        User user=userService.getUser(null, loginName);
        if(EmptyUtils.isEmpty(user)){
            result.returnFail("用户不存在");
        }else{
           if(user.getPassword().equals(SecurityUtils.md5Hex(password))){
               //登陆成功
               request.getSession().setAttribute("loginUser", user);
               result.returnSuccess("登陆成功");
           }else{
               result.returnFail("密码错误");
           }
        }
        return result;
	}

	/**
	 * 跳转到登录页面
	 * 
	 * @param request
	 * @param response
	 */
	private void toLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String path = request.getContextPath();
		response.sendRedirect(path + "/pre/login.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
