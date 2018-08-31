package cn.easybuy.web.front;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.easybuy.entity.News;
import cn.easybuy.entity.Product;
import cn.easybuy.param.NewsParams;
import cn.easybuy.service.news.NewsService;
import cn.easybuy.service.news.NewsServiceImpl;
import cn.easybuy.service.product.ProductCategoryService;
import cn.easybuy.service.product.ProductCategoryServiceImpl;
import cn.easybuy.service.product.ProductService;
import cn.easybuy.service.product.ProductServiceImpl;
import cn.easybuy.utils.Pager;
import cn.easybuy.utils.ProductCategoryVo;

/**
 * 易买网首页
 */
@WebServlet("/Index")
public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// 声明业务对象
	private ProductService productService;
	private NewsService newsService;
	private ProductCategoryService productCategoryService;

	// 通过servlet容器进行业务层对象初始化
	public void init() throws ServletException {
		productService = new ProductServiceImpl();
		newsService = new NewsServiceImpl();
		productCategoryService = new ProductCategoryServiceImpl();
	}

	public IndexServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取事件参数
		String action = request.getParameter("action");
		try {
			// 查询商品分裂
			List<ProductCategoryVo> productCategoryVoList = productCategoryService.queryAllProductCategoryList();
			Pager pager = new Pager(5, 5, 1);
			NewsParams params = new NewsParams();
			params.openPage((pager.getCurrentPage() - 1) * pager.getRowPerPage(), pager.getRowPerPage());
			List<News> news = newsService.queryNewsList(params);
			// 查询一楼
			for (ProductCategoryVo vo : productCategoryVoList) {
				List<Product> productList = productService.getProductList(1, 8, null, vo.getProductCategory().getId(),
						1);
				vo.setProductList(productList);
			}
			// 封装返回
			request.setAttribute("productCategoryVoList", productCategoryVoList);
			request.setAttribute("news", news);
			request.getRequestDispatcher("/pre/index.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
