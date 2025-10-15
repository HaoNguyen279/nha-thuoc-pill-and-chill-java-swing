/*
 * @ (#) ConnectDB.java   1.0     9/29/2025
 * Copyright (c) 2025 IUH, All rights reserved.
 */


package app.ConnectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * @description:
 * @author:
 * @date:  9/29/2025
 * @version:    1.0
 */
public class ConnectDB {
    public static Connection con = null;
    private static ConnectDB instance =  new ConnectDB();
    public static ConnectDB getInstance() {
        return instance;
    }
    public Connection getConnection() throws SQLException {
        if (con == null || con.isClosed()) {
            connect();
        }
        return con;
    }
    public void connect() {
        // Kiểm tra nếu đã có connection và connection vẫn còn mở, không tạo mới
        try {
            if (con != null && !con.isClosed()) {
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Tạo connection mới
        String url = "jdbc:sqlserver://localhost:1433;DatabaseName=QuanLyNhaThuoc;encrypt=true;trustServerCertificate=true;";
        String user = "sa";
        String pwd = "sapassword";
        try {
            con = DriverManager.getConnection(url,user,pwd);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void disconnect(){
        if(con != null)
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
}