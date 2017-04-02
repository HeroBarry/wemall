package com.wemall.core.tools.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;

@Repository
public class DbConnection {
    @Autowired
    private DataSource dataSource;
    public static final ThreadLocal<Connection> thread = new ThreadLocal();

    public Connection getConnection(){
        Connection conn = (Connection)thread.get();
        if (conn == null){
            try {
                conn = this.dataSource.getConnection();
                thread.set(conn);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return conn;
    }

    public void closeAll(){
        try {
            Connection conn = (Connection)thread.get();
            if (conn != null){
                conn.close();
                thread.set(null);
            }
        } catch (Exception e){
            try {
                throw e;
            } catch (Exception e1){
                e1.printStackTrace();
            }
        }
    }
}
