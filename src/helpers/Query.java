package helpers;

public class Query {
	
	public static String getProduct = "SELECT sku,category.name,vendor.name,vendorModel, description, features,cost"+
									  ",retail ,image from product,vendor,category where product.venID=vendor.id "+
									  "AND product.catID=category.id AND sku = ?";
	
	public static String checkSku = "SELECT on_hand_quantity FROM on_hand WHERE sku = ?"; 
	public static String updateHandsOn = "UPDATE on_hand SET on_hand_quantity = ?, last_date_modified = STR_TO_DATE(?, '%Y-%m-%d %H:%i:%s') WHERE sku = ?";
	public static String insertHandsOn = "INSERT INTO on_hand VALUES(?,STR_TO_DATE(?, '%Y-%m-%d %H:%i:%s'),?)";
	public static String insertMerchandiseIn = "INSERT INTO merchandise_in VALUES(?,STR_TO_DATE(?, '%Y-%m-%d'),?)";
	public static String insertMerchandiseOut = "INSERT INTO merchandise_out VALUES(?,STR_TO_DATE(?, '%Y-%m-%d'),?)";
	
	public static String retrieveCategories = "SELECT * FROM category";
	public static String retrieveBrands = "SELECT * FROM vendor";
	public static String getProducts = "SELECT sku,category.name,vendor.name,vendorModel, description, features,cost"+
										",retail ,image from product,vendor,category where product.venID=vendor.id "+
										"AND product.catID=category.id";
	
	public static String BASE_PRODUCTS_URL = "SELECT sku,category.name,vendor.name,vendorModel, description, features,cost"+
			",retail ,image from product,vendor,category where product.venID=vendor.id "+
			"AND product.catID=category.id ";
	public static String getSKUOnHand = "SELECT on_hand_quantity from on_hand where sku = ?";
			
	
}
