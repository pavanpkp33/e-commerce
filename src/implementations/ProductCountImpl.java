package implementations;

import java.sql.ResultSet;

import javax.json.Json;
import javax.json.JsonArray;

import javax.json.JsonObject;

import helpers.DBHelper;
import helpers.Query;

public class ProductCountImpl {
	ResultSet resultSet, innerRs;
	
	JsonArray jsonArr;
	JsonObject respObj;
	public JsonObject getCount(String sku){		
								
		
		int stockQty = 0;
		stockQty = DBHelper.getQuantity(Query.checkSku,sku);
		try{
		
				respObj = Json.createObjectBuilder()
						.add("status", 200)
						.add("message", "Search successful")
						.add("data", stockQty).build();
			
		}
		catch(Exception e){
			e.printStackTrace();
			respObj = Json.createObjectBuilder()
					.add("status", 500)
					.add("message", "Internal Server error")
					.add("data", "[]").build();
		}
		
		
		return respObj;
		
		
	}

}
