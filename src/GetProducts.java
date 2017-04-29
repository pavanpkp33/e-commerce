

import java.io.IOException;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import implementations.GetCategoriesImpl;
import implementations.GetProductsImpl;

/**
 * Servlet implementation class GetProducts
 */
@WebServlet("/GetProducts")
public class GetProducts extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetProducts() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JsonArray brandArr, categoryArr, availArr;
		String type = request.getParameter("filter");
		if(type != null){
			// filters applied
			
			type = type.replaceAll("\r?\n", "");
			System.out.println(type);
			JsonReader reader = Json.createReader(new StringReader(type));
			JsonObject filterObj = reader.readObject();
			reader.close();
			brandArr = filterObj.getJsonArray("brand");
			categoryArr = filterObj.getJsonArray("category");
			availArr = filterObj.getJsonArray("availability");
			
			response.setContentType("application/json");
			response.getWriter().print(new GetProductsImpl().getFilterProducts(brandArr, categoryArr, availArr));
			return;
			
		}else{
			// Initial load 
			
			response.setContentType("application/json");
			response.getWriter().print(new GetProductsImpl().getInitProducts());
			return;
		}
		
	}

}
