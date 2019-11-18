package com.grillo78.BeyCraft.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import com.grillo78.BeyCraft.BeyCraft;

public class DatabaseConnection {
	
	private Statement statement;
	private Connection conn = null;
	private static final String DB_URL = "jdbc:mysql://sql2.freesqldatabase.com:3306/sql2312370";
	
	public DatabaseConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL,"sql2312370","cK7%vG9%");
			setStatement(conn.createStatement());
			if (!conn.isClosed()) {
				BeyCraft.logger.info("Database connection working using TCP/IP...");
			}
		}catch (Exception e) {
			BeyCraft.logger.error("Database exception: "+e.getMessage());
		}
	}

	public Connection getConn() {
		return conn;
	}

	public Statement getStatement() {
		return statement;
	}

	private void setStatement(Statement statement) {
		this.statement = statement;
	}
}
