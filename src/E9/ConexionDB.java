/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package E9;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pedro
 */
public class ConexionDB {
     private static ConexionDB cx=null;//singleton
    public static ConexionDB getInstance(){
        if(cx==null)
            cx=new ConexionDB();
        return cx;
    } 
    private Connection con;
    private ConexionDB(){
        String url="jdbc:postgresql://localhost:5432/Temperaturas";
        try {
            
            Class.forName("org.postgresql.Driver");
            con=DriverManager.getConnection(url,"postgres","1234");
            Logger.getLogger(ConexionDB.class.getName()).log(Level.INFO, "se conecto a la bd");
         } catch (SQLException ex) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
        }catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public boolean execute(String sql){
    boolean res=false;
        try {
            Statement st=con.createStatement();
            st.execute(sql);
            res=true;
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
        }
    public ResultSet select(String sql){
        ResultSet reg=null;
            try {
                
                Statement st=con.createStatement();
                reg=st.executeQuery(sql);
                
            } catch (SQLException ex) {
                Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
            }
            return reg;
    }
}
