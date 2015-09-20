package model;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


public class Database {
	
	/**
	 * @author kai
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */

	// INSTALLING HSQLDB
	
	// 1. HSQLDB only runs with JVM 1.6.0, Download for Mac available at http://developer.apple.com/java/download/
	// 2. DOWNLOAD HSQLDB from http://hsqldb.org/
	// 3. Copy the "hsqldb.jar" from "/lib"
	// 4. For MAC OSX paste it to "/System/Library/Java/Extensions"
	//    For Windows paste it to "C:\Programme\Java\jdk1.6.0_12\jre\lib\ext" or wherever local JAVA installtion is located
	// 5. HSQLDB is now available in all projects

	
	
	//  CODE EXAMPLES
	
	//  Create database-table if it doesn't already exists
	//	If first SQL-query (SELECT) fails, perform second query (CREATE)
	//	Database.dbCheck("SELECT ID FROM games LIMIT 1","CREATE TABLE games (ID INTEGER, datetime CHAR(19), player INTEGER, column INTEGER, winner INTEGER);");
	
	//  Create an SQL-query-result
	//  ResultSet qresult = Database.selectDB("SELECT * FROM games");
	
	//  Perform an SQL-command
	//  Database.sqlDB("INSERT INTO games VALUES(2241,'2009-04-04 14:21:23', 1, 3, 0);");
	
	//  Print query results to consule (debugging)
	//	Database.printTable(qresult);
	
	//  Get an array of (columns,rows) of an query result
	//	int[] columnsrows = Database.getColumsRows(qresult);
	
	//  Get the meta-data (contents) of an query-result
	//	ResultSetMetaData games = Database.getMetaDB(qresult);
	
	public Connection connectDB() throws IOException, SQLException{
		/**
		 * Method to connect to HSQL database
		 * Path will be ./database/viergewinnt
		 */
		
		try {
			//Class.forName("org.hsqldb.jdbc.JDBCDriver" );
			Class.forName("org.hsqldb.jdbcDriver" );
			} catch (Exception e) {
			System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
			e.printStackTrace();
			}
		
		URL db_path = Database.class.getResource("./");	
		Connection con = DriverManager.getConnection("jdbc:hsqldb:"+db_path+"/database/viergewinnt;shutdown=true", "sa", "");
		return con;
	}
	
	public Statement statementDB(Connection con) throws IOException, SQLException{
		/**
		 * Create an sql-statement
		 * 
		 */
		
		Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);	
		return stmt;
	}
	
	public void dbCommit(Connection con) throws SQLException{
		/**
		 * commit DB-queries and close connection
		 * 
		 */
		
		con.prepareStatement("SHUTDOWN").execute();
		con.commit();
	}
	
	public boolean sqlDB(String sql) throws IOException, SQLException{
		/**
		 * Method to execute SQL-query with no return-values
		 * 
		 */
		
		Connection con = connectDB();
		Statement stmt = statementDB(con);
		try {
			stmt.executeUpdate(sql);
			} catch (Exception e) {
				System.err.println(e);
				return false;
			}
		dbCommit(con);
		return true;
	}
	
	
	public ResultSet selectDB(String sql) throws IOException, SQLException{
		/**
		 * Method to execute SQL-query with return values (resultSet)
		 * 
		 */
		
		Connection con = connectDB();
		Statement stmt = statementDB(con);
		try {
			ResultSet rs = stmt.executeQuery(sql);
			dbCommit(con);
			return rs;
			} catch (Exception e) {
				dbCommit(con);
				return null;
			}
	}
	
	public ResultSetMetaData getMetaDB(ResultSet rs) throws IOException, SQLException{	
		/**
		 * Get the ResultSetMetaData of an ResultSet
		 * 
		 */
		
		ResultSetMetaData rsmd = rs.getMetaData();
		return rsmd;
	}
	
	public void dbCheck(String sql_check, String sql_action) throws IOException, SQLException{
		/**
		 * Checks whether sql_check succeeds or fails
		 * if it fails, performs sql_action
		 * 
		 */
			
		if(selectDB(sql_check) == null){
			sqlDB(sql_action);
		}
	}
	
	public void printTable(ResultSet resultset) throws SQLException, IOException {
		/**
		 * Prints resultSet to console for debugging issues
		 * 
		 */
		
		ResultSetMetaData metadata = getMetaDB(resultset);
		
		int i, n = metadata.getColumnCount();
		for( i=0; i<n; i++ )
	        System.out.print( "+---------------" );
	      System.out.println( "+" );
	      for( i=1; i<=n; i++ )    // Attention: first column with 1 instead of 0
	        System.out.print( " | " + metadata.getColumnName( i ) );
	      System.out.println( " |" );
	      for( i=0; i<n; i++ )
	        System.out.print( "+---------------" );
	      System.out.println( "+" );
	      while( resultset.next() ) {
	        for( i=1; i<=n; i++ )  // Attention: first column with 1 instead of 0
	          System.out.print( " | " + resultset.getString( i ) );
	        System.out.println( " |" );
	      }
	      for( i=0; i<n; i++ )
	        System.out.print( "+---------------" );
	      System.out.println( "+" );
	}
	
	public int[] getColumsRows(ResultSet resultset) throws SQLException, IOException {
		/**
		 * Returns number of columns and number of rows of an reultSet as an array
		 * 
		 */
		
		ResultSetMetaData metadata = getMetaDB(resultset);
		int[] columnsrows = {0,0};
		int columns = metadata.getColumnCount();
		resultset.last();
		int rows = resultset.getRow();
		resultset.beforeFirst();
		columnsrows[0] = columns;
		columnsrows[1] = rows;
		return columnsrows;
	}
	
}