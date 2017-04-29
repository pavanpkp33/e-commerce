package implementations;

import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import helpers.DBHelper;
import helpers.Query;
import model.Product;

public class GetProductImpl {
	DBHelper dbInstance;
	JsonArray jsonArr;
	JsonObject respObj;
	public JsonObject getProductDetails(String sku){
		
		String BASE_QUERY = Query.BASE_PRODUCTS_URL;
		BASE_QUERY += "AND sku =\""+sku+"\"";
		
		dbInstance = new DBHelper();
		Product product;
		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		
		ArrayList<Product> prodList = dbInstance.getProductList(BASE_QUERY);
		try{
			if(prodList.size() > 0){
				for(int i=0; i <prodList.size(); i++){
					product = new Product();
					product = prodList.get(i);
					String skuDB = product.getSku();
					int stockQty = 0, val =0;
					// Check sku exisits in on_hand
					val = DBHelper.getQuantity(Query.getSKUOnHand, skuDB);
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

}
