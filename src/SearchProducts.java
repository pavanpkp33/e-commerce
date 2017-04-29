

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import implementations.GetProductsImpl;
import implementations.SearchProductsImpl;

/**
 * Servlet implementation class SearchProducts
 */
@WebServlet("/SearchProducts")
public class SearchProducts extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchProducts() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String searchQ = request.getParameter("search");
		if(searchQ != null){
			response.setContentType("application/json");
			response.getWriter().print(new SearchProductsImpl().searchProducts(searchQ));
			return;
		}else{
			response.setContentType("application/json");
			response.getWriter().print("No Search Query!");
			return;
		}
		
	}

}
