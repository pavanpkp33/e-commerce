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
import model.Product;

public class GetProductsImpl {
	ResultSet resultSet, innerRs;
	DBHelper dbInstance;
	JsonArray jsonArr;
	JsonObject respObj;
	public JsonObject getInitProducts(){		
		
		return getDataFromDB(Query.getProducts);
	}

	public JsonObject getFilterProducts(JsonArray brandArr, JsonArray catArr, JsonArray availArr){
		
		String BASE_QUERY = Query.BASE_PRODUCTS_URL;
		
		if(!brandArr.isEmpty()){
			String brands = "(";
			for(int i=0; i < brandArr.size(); i++){
				if(i+1 == brandArr.size()){
					brands += "'"+brandArr.getString(i)+"'";
				}else{
					brands += "'"+brandArr.getString(i)+"' ,";
				}
				
			}
			brands += ")";
			BASE_QUERY += "AND vendor.name IN "+brands;
		}
		if(!catArr.isEmpty()){
			String category = "(";
			for(int i=0; i < catArr.size(); i++){
				if(i+1 == catArr.size()){
					category += "'"+ catArr.getString(i)+"'";
				}else{
					category += "'"+catArr.getString(i)+ "' ,";
				}
				
			}
			category += ")";
			BASE_QUERY += "AND category.name IN "+category;
		}
		if(!availArr.isEmpty()){
			BASE_QUERY += " AND (";
			int size = availArr.size();
			for(int i =0; i < availArr.size(); i++){
				int loc =0;
				if(availArr.getString(i).equalsIgnoreCase("available")){
					loc = getIndex(availArr, "available");
					if(loc == 0){
						BASE_QUERY += "sku IN (select sku from on_hand where on_hand_quantity > 0) ";	
					}else{
						BASE_QUERY += "OR sku IN (select sku from on_hand where on_hand_quantity > 0) ";
					}
					
				}else if(availArr.getString(i).equalsIgnoreCase("outstock")){
					loc = getIndex(availArr, "outstock");
					
					if(loc == 0){
						BASE_QUERY += " sku IN (select sku from on_hand where on_hand_quantity = 0) ";
					}else{
						BASE_QUERY += " OR sku IN (select sku from on_hand where on_hand_quantity = 0) ";
					}
				}else{
					
					loc = getIndex(availArr, "coming");
					if(loc == 0){
						BASE_QUERY += " sku NOT IN (select sku from on_hand) ";
					}else{
						BASE_QUERY += " OR sku NOT IN (select sku from on_hand) ";
					}
				}
			}
			BASE_QUERY += ")";
		}
		
		System.out.println(BASE_QUERY);
		
		return getDataFromDB(BASE_QUERY);
	}

	private JsonObject getDataFromDB(String bASE_QUERY) {
		// TODO Auto-generated method stub
		dbInstance = new DBHelper();
		Product product;
		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		
		ArrayList<Product> prodList = dbInstance.getProductList(bASE_QUERY);
		try{
			if(prodList.size() > 0){
				for(int i=0; i <prodList.size(); i++){
					product = new Product();
					product = prodList.get(i);
					String sku = product.getSku();
					int stockQty = 0, val =0;
					// Check sku exisits in on_hand
					val = DBHelper.getQuantity(Query.getSKUOnHand, sku);
					if(val > -1){
												
						if(val == 0){
							stockQty = 0;
						}else{
							stockQty = val;
						}
												
					}else{
						stockQty = -1;
					}
					
					arrBuilder.add(Json.createObjectBuilder()
							.add("sku", sku)
							.add("category", product.getCategory())
							.add("brand", product.getBrand())
							.add("vendorId", product.getVendorId())
							.add("description", product.getDescription())
							.add("features", product.getFeatures())
							.add("retail", product.getRetail())
							.add("image", product.getImage())
							.add("stock", stockQty).build());
					
				}
				jsonArr = arrBuilder.build();
				respObj = Json.createObjectBuilder()
						.add("status", 200)
						.add("message", "Products Query successful")
						.add("data", jsonArr).build();
			}else{
				// here add null
				respObj = Json.createObjectBuilder()
						.add("status", 200)
						.add("message", "We could not find anything for you!")
						.add("data", "[]").build();
			}
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
	
	private int getIndex(JsonArray arr, String key){
		int index = -1;
		for (int i = 0; (i < arr.size()) && (index == -1); i++) {
	        if (arr.getString(i).equalsIgnoreCase(key)) {
	            index = i;
	        }
	    }
		
		return index;
		
	}
}
