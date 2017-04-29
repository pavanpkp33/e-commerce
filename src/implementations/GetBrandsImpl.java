package implementations;

import java.sql.ResultSet;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import helpers.DBHelper;
import helpers.Query;

public class GetBrandsImpl {
	ResultSet resultSet;
	DBHelper dbInstance;
	JsonArray jsonArr;
	
	public JsonObject getBrands(){
		dbInstance = new DBHelper();
		ArrayList<String> result;
		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		result = dbInstance.getCatBrand(Query.retrieveBrands);
		try{
			if(result.size() > 0){
				for(int i=0; i< result.size(); i++){
					arrBuilder.add(result.get(i));
					
				}
				jsonArr =	arrBuilder.build();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		JsonObject respObj = Json.createObjectBuilder()
				.add("status", 200)
				.add("message", "Brand Query successful")
				.add("data", jsonArr).build();
					
		
		return respObj;
	}

}
