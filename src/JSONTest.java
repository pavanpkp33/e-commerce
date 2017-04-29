

import java.io.IOException;
import java.io.StringReader;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class JSONTest
 */
@WebServlet("/JSONTest")
public class JSONTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JSONTest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String personJSONData =
				"  {" +
						"   \"name\": \"Jack\", " +
						"   \"age\" : 13, " +
						"   \"isMarried\" : false, " +
						"   \"address\": { " +
						"     \"street\": \"#1234, Main Street\", " +
						"     \"zipCode\": \"123456\" " +
						"   }, " +
						"   \"phoneNumbers\": [\"011-111-1111\", \"11-111-1111\"] " +
						" }";
			String sku = request.getParameter("sku");

			JsonReader reader = Json.createReader(new StringReader(sku));

			JsonObject personObject = reader.readObject();

			reader.close();

			System.out.println("Name   : " + personObject.getString("name"));
			System.out.println("Age    : " + personObject.getInt("age"));
			System.out.println("Married: " + personObject.getBoolean("isMarried"));

			response.setContentType("application/json");
			response.getWriter().print("Test");
			return;

	}

}
