package BaseDeDatos;


import java.sql.Connection;
import java.sql.DriverManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author josea
 */
public class Conexion {
       Connection link=null;
    public Conexion(){}
    public Connection conectar(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            
        link=DriverManager.getConnection("jdbc:mysql://127.0.0.1/juegoboxeo","root","");
        if(link==null){
           System.err.println("Error al conectarse");
        }
        }catch(Exception e){e.printStackTrace();}
        return link;
    }
}
