package implementations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import helpers.DBHelper;
import helpers.Query;

public class GetCategoriesImpl {
	ResultSet resultSet;	
	JsonArray jsonArr;
	ArrayList<String> result;
	public JsonObject getCategories(){
		
		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		DBHelper dbInstance = new DBHelper();
		result = dbInstance.getCatBrand(Query.retrieveCategories);
		try {
			if(result.size() > 0){
				for(int i=0; i< result.size(); i++){
					arrBuilder.add(result.get(i));
					
				}
				jsonArr =	arrBuilder.build();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JsonObject respObj = Json.createObjectBuilder()
							.add("status", 200)
							.add("message", "Category fetch successfull")
							.add("categories", jsonArr).build();
		return respObj;
	}

}
