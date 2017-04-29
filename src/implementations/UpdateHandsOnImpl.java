package implementations;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.json.JsonArray;
import javax.json.JsonObject;

import helpers.DBHelper;
import helpers.Query;

public class UpdateHandsOnImpl {
	String resp = "";
	  int resCode = 0;
	  DateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  String lastUpdated = timestamp.format(new Date());
	  
	  
	  
	public String updateHandsOn(JsonArray dataArr){
		JsonObject jsonObject;
		if(!dataArr.isEmpty()){
			int resCode = 0;
			for(int i=0; i< dataArr.size(); i++){
				jsonObject = dataArr.getJsonObject(i);
				try {
					resCode = updateSent(lastUpdated, jsonObject.getInt("quantity"), jsonObject.getString("sku"));
					if(resCode == -1){
						resp = "{ \"status\":200, \"message\": \"Purchase successful\", \"data\": null }";
					}else if(resCode == -2){
						resp = "{ \"status\":500, \"message\": \" Item currently unavailable \", \"data\": null }";
					}else{
						resp = "{ \"status\":500, \"message\": \"Insufficient stock for selected Item\", \"data\": null }";
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					 resp = "{ \"status\":500, \"message\": "+e.getMessage()+", \"data\": null }";
				}
			}
			
		}
		return resp;
	}
		
	public int updateSent(String date, int quantity, String sku) throws Exception{
			DateFormat time = new SimpleDateFormat("yyyy-MM-dd");
			String dateObj = time.format(new Date());
				int resultCode = 0;
				resultCode = updateHandsOnSent(sku, quantity);
				if(resultCode == -1){
					//return success
					DBHelper.insertValues(Query.insertMerchandiseOut, sku, dateObj, quantity);
					
					return resultCode;
				}else if(resultCode == -2){
					//return fail. does not exist
					
					return resultCode;
					
				}else{
					// return insuf quantity
					
					return resultCode;
				}
				

		  
	  }
	
	private int updateHandsOnSent(String sku, int quantity) throws Exception {
		// TODO Auto-generated method stub
		  int qtyToInsert = quantity;
		  int curQty = 0;
		  curQty = DBHelper.getQuantity(Query.checkSku, sku);
		  if(curQty > -1){
			  if(qtyToInsert <= curQty){
				  //Product in stock.
				  curQty = curQty - qtyToInsert;
				  resCode = DBHelper.insertValues(Query.updateHandsOn, curQty, lastUpdated, sku);
				  return -1;
			  }else{
				  
				  return curQty;
			  }
			  
			  
		  }else{
			  // No shipment received. No stock throw error.			 
			  return -2;
		  }
		
	}

}
