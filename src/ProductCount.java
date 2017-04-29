

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import implementations.ProductCountImpl;
import implementations.SearchProductsImpl;

/**
 * Servlet implementation class ProductCount
 */
@WebServlet("/ProductCount")
public class ProductCount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductCount() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String sku = request.getParameter("sku");
		if(sku != null){
			response.setContentType("application/json");
			response.getWriter().print(new ProductCountImpl().getCount(sku));
			return;
		}else{
			response.setContentType("application/json");
			response.getWriter().print("No SKU!");
			return;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	

}
