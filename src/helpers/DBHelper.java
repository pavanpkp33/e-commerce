package helpers;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import implementations.ProductCountImpl;
import model.Product;


public class DBHelper implements java.io.Serializable {
    private static String connectionURL = "jdbc:mysql://127.0.0.1:3306/jadrn036?user=root&password=pavap";               
    private static Connection connection = null;
    private static Statement statement = null;
    private static ResultSet resultSet = null;

    public DBHelper() {}    
    
    public static Vector<String []> runQuery(String s) {
        String answer = "";
        Vector<String []> answerVector = null;
		
	try {
	Class.forName("com.mysql.jdbc.Driver").newInstance();
	connection = DriverManager.getConnection(connectionURL);
	statement = connection.createStatement();
	resultSet = statement.executeQuery(s);        
        ResultSetMetaData rsmd = resultSet.getMetaData();
        answerVector = new Vector<String []>();
	while(resultSet.next()) {
            String [] row = new String[rsmd.getColumnCount()];
            for(int i=0; i < rsmd.getColumnCount(); i++)
                row[i] = resultSet.getString(i+1);
            answerVector.add(row);       
		}
	}
	catch(Exception e) {
	    e.printStackTrace();
	}
        //////////////////////////////////////////////////////////////////////////            
        // The finally clause always runs, and closes resources if open.
        // DO NOT OMIT THIS!!!!!!
        finally {
            try {
                if(resultSet != null)
                    resultSet.close();
                if(statement != null)
                    statement.close();
                if(connection != null)                   
            	    connection.close();
                }
            catch(SQLException e) {
                answer += e;
                }
        //////////////////////////////////////////////////////////////////////////
        }
        return answerVector;
    }    
    
    public static String doQuery(String s) {
                String answer = "";		
		try {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(connectionURL);
		statement = connection.createStatement();
		resultSet = statement.executeQuery(s);
                ResultSetMetaData rsmd = resultSet.getMetaData();                
                answer += "<h2>Results from the database:</h2>\n";
                answer += "<table>\n";                
		while(resultSet.next()) {
                    int columns = rsmd.getColumnCount();
                    answer += "<tr>\n";
                    for(int i=1; i <= columns; i++)  {                      
                        answer += "<td>"+rsmd.getColumnName(i) + "</td><td>" + 
                                resultSet.getString(rsmd.getColumnName(i)) + "</td>\n"; 
                        }
                    answer += "</tr>\n";                                                                                 
		    }
                answer += "</table>\n";                    
		}
		catch(Exception e) {
			e.printStackTrace();
			}
        //////////////////////////////////////////////////////////////////////////            
        // The finally clause always runs, and closes resources if open.
        // DO NOT OMIT THIS!!!!!!
        finally {
            try {
                if(resultSet != null)
                    resultSet.close();
                if(statement != null)
                    statement.close();
                if(connection != null)                   
            	    connection.close();
                }
            catch(SQLException e) {
                answer += e;
                }
        //////////////////////////////////////////////////////////////////////////
        }
        return answer;
    }
    
   public static int executeCommand(String s) {
   		int howMany = 0;
		try {	
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL);
			statement = connection.createStatement();
			howMany = statement.executeUpdate(s);
		}
		catch(Exception e) {}
        	finally {
            	try {
                	if(statement != null)
                    	statement.close();
                	if(connection != null)                   
            	    	connection.close();
                	}
            	catch(SQLException e) {}
               }
        	return howMany;	
		}	   
   
   public static ResultSet selectWithParams(String Query, Object ...a) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL);
			stmt = connection.prepareStatement(Query);
			for(int i =0; i<a.length; i++){
				if (a[i] instanceof Date) {
					stmt.setDate(i+1, (java.sql.Date) a[i]);
				} else if (a[i] instanceof Integer) {
					stmt.setInt(i+1, (Integer) a[i]);
				} else {
					stmt.setString(i+1, (String) a[i]);
				}
			}
			
			rs = stmt.executeQuery();
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return rs;
	}
   
   public ResultSet runCommandNoParams(String Query) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL);
			stmt = connection.prepareStatement(Query);	
			
			rs = stmt.executeQuery();

		} catch (Exception e) {
			
			e.printStackTrace();

		}
//		finally{			  
//			 
//			try {
//				if(stmt != null) stmt.close();
//				if(connection != null)connection.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
//	}

		return rs;
		

	}

   
   public ArrayList<Product> getProductList(String Query){
	   ArrayList<Product> prodList = new ArrayList<>();
	   Product product;
	   ResultSet rs = null;
		PreparedStatement stmt = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL);
			stmt = connection.prepareStatement(Query);	
			
			rs = stmt.executeQuery();
			while(rs.next()){
				product = new Product();
				product.setSku(rs.getString(1));
				product.setCategory(rs.getString(2));
				product.setBrand(rs.getString(3));
				product.setVendorId(rs.getString(4));
				product.setDescription(rs.getString(5));
				product.setFeatures(rs.getString(6));
				product.setRetail(rs.getDouble(8));
				product.setImage(rs.getString(9));
				
				prodList.add(product);
				
			}

		} catch (Exception e) {
			
			e.printStackTrace();

		}
		finally{			  
			 
			try {
				if(stmt != null) stmt.close();
				if(connection != null)connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
	   
	   
	   return prodList;
	   
   }
   
   public static int insertValues(String query, Object...params) throws Exception{
		PreparedStatement stmt = null;
		int resCode =0;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL);
			stmt = connection.prepareStatement(query);
			for(int i =0; i<params.length; i++){
				
				if (params[i] instanceof Date) {
					stmt.setDate(i+1, (java.sql.Date) params[i]);
				} else if (params[i] instanceof Integer) {
					stmt.setInt(i+1, (Integer) params[i]);
				} else {
					stmt.setString(i+1, (String) params[i]);
				}
			}
			
		resCode = stmt.executeUpdate();
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{			  
			 
			try {
				if(stmt != null) stmt.close();
				if(connection != null)connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return resCode;		
		
	}
   
   public static int getQuantity(String Query, Object ...a) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int quantity = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL);
			stmt = connection.prepareStatement(Query);
			for(int i =0; i<a.length; i++){
				if (a[i] instanceof Date) {
					stmt.setDate(i+1, (java.sql.Date) a[i]);
				} else if (a[i] instanceof Integer) {
					stmt.setInt(i+1, (Integer) a[i]);
				} else {
					stmt.setString(i+1, (String) a[i]);
				}
			}
			
			rs = stmt.executeQuery();
			if(rs.isBeforeFirst()){
				while(rs.next()){
					quantity = rs.getInt(1);
				}
			}else{
				quantity = -1;
			}
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{			  
			 
			try {
				if(stmt != null) stmt.close();
				if(connection != null)connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
		
		return quantity;
	}
   
   public ArrayList<String> getCatBrand(String Query) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		ArrayList<String> resultList = new ArrayList<>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL);
			stmt = connection.prepareStatement(Query);	
			
			rs = stmt.executeQuery();
			while(rs.next()){
				resultList.add(rs.getString(2));
			}

		} catch (Exception e) {
			
			e.printStackTrace();

		}
		finally{			  
			 
			try {
				if(stmt != null) stmt.close();
				if(connection != null)connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}

		return resultList;
		

	}
}            
	
	
