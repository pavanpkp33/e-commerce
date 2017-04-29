

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

import implementations.GetProductsImpl;
import implementations.UpdateHandsOnImpl;

/**
 * Servlet implementation class UpdateHandsOn
 */
@WebServlet("/UpdateHandsOn")
public class UpdateHandsOn extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateHandsOn() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JsonArray data;
		String strData = request.getParameter("cart");
		if(strData != null){
			JsonReader reader = Json.createReader(new StringReader(strData));
			data = reader.readArray();
			reader.close();			

			response.setContentType("application/json");
			response.getWriter().print(new UpdateHandsOnImpl().updateHandsOn(data));
			return;
		}
	}

}
