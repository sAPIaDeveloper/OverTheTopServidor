package BaseDeDatos;


import BaseDeDatos.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author josea
 */
public class AccesoBaseDatos {
     
    public boolean existeEmailUsuario(String email){
        Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();          
        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement("Select cod_usuario from usuario where email=?");
            pps.setString(1, email);
            
            ResultSet result=pps.executeQuery();
            while(result.next()){
                return true;
            }
                       
        }catch(SQLException e){e.printStackTrace();}
        return false;
    }// Metodo para comprobar si existe ya un usuario registrado con ese email
    
    public boolean existeCodigoEmparejamiento(long codigo_emparejamiento){
        Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();          
        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement("Select cod_usuario from usuario where cod_emparejamiento=?");
            pps.setLong(1, codigo_emparejamiento);
            
            ResultSet result=pps.executeQuery();
            while(result.next()){
                return true;
            }
                       
        }catch(SQLException e){e.printStackTrace();}
        return false;
    } // Metodo para comprobar si existe ya ese codigo de emparejamiento.
    
    public boolean registrarUsuario(String email,String contrasena,long codigo_emparejamiento){
        boolean registro_exito=false;
                
        Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();

        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement("INSERT INTO Usuario (email,contrasena,cod_emparejamiento)  VALUES (?,?,?)");
            pps.setString(1, email);
            pps.setString(2, contrasena);
            pps.setLong(3, codigo_emparejamiento);

            if(pps.executeUpdate()>0){
                registro_exito=true;
            }

        }catch(SQLException e){e.printStackTrace();}
        return registro_exito;
    }
    
    public String iniciarSesion(String email,String contrasena){
        String respuesta="";
        Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();          
        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement("Select cod_emparejamiento from usuario where email=? && contrasena=?");
            pps.setString(1, email);
            pps.setString(2, contrasena);
            
            ResultSet result=pps.executeQuery();
            while(result.next()){                
                respuesta=result.getLong(1)+"";
            }
                       
        }catch(SQLException e){e.printStackTrace();}
        return respuesta;
    }
    
    public String devolverBoxeadores(String email){
        String boxeadores="";
        Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();          
        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement("Select nombre, tipo_boxeador, peso, pais from Boxeador b, Usuario u where b.cod_usuario=u.cod_usuario and u.email=?");
            pps.setString(1, email);
            
            
            ResultSet result=pps.executeQuery();
            while(result.next()){
                boxeadores+=result.getString(1)+"@"+result.getString(2)+"@"+result.getDouble(3)+"@"+result.getString(4)+"&";
                System.out.println(boxeadores);
                
            }                       
        }catch(SQLException e){e.printStackTrace();}
        return boxeadores;
    }
    
    public boolean registrarBoxeador(int cod_usuario,String nombre,String tipo_boxeador,String sexo, String peso,String pais,String categoria){
        boolean registro_exito=false;
                
        Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();

        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement("INSERT INTO Boxeador (nombre,tipo_boxeador,sexo,peso,pais,categoria,cod_usuario)  VALUES (?,?,?,?,?,?,?)");
            pps.setString(1, nombre);
            pps.setString(2, tipo_boxeador);
            pps.setString(3, sexo);
            pps.setDouble(4, Double.parseDouble(peso));
            pps.setString(5, pais);
            pps.setString(6, categoria);
            pps.setInt(7, cod_usuario);

            if(pps.executeUpdate()>0){
                registro_exito=true;
            }

        }catch(SQLException e){e.printStackTrace();}
        return registro_exito;
    }
    
    public boolean comprobarSiExisteBoxeador(String nombre_boxeador){
        
        Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();          
        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement("Select cod_boxeador from boxeador where nombre=?");
            pps.setString(1, nombre_boxeador);
            
            ResultSet result=pps.executeQuery();
            while(result.next()){
                return true;
            }
                       
        }catch(SQLException e){e.printStackTrace();}
        return false;
    }
    
    public int devolverCodigoDeUsuario(String email){        
        Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();          
        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement("Select cod_usuario from usuario where email=?");
            pps.setString(1, email);
            
            ResultSet result=pps.executeQuery();
            while(result.next()){
                return result.getInt(1);
            }
                       
        }catch(SQLException e){e.printStackTrace();}
        return 0;
    }
    
    public String devolverCompeticionesApuntadasPorUnBoxeador(String nombre_boxeador){
        String lista_competiciones="Partida rapida&";
        Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();          
        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement("Select c.nombre from Boxeador b, Apuntarse a, Competicion c where b.cod_boxeador=a.cod_boxeador && a.cod_competicion=c.cod_competicion && b.nombre=?;");
            pps.setString(1, nombre_boxeador);
            
            
            ResultSet result=pps.executeQuery();
            while(result.next()){
                lista_competiciones+=result.getString(1)+"&";
                
            }                       
        }catch(SQLException e){e.printStackTrace();}
        
        return lista_competiciones;
        
        
    }
    
    public String obtenerCodigoEmparejamiento(String nombre_boxeador){
        String respuesta="";
         Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();   
        String cod_emparejamiento;
        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement("Select u.cod_emparejamiento from Usuario u, Boxeador b where u.cod_usuario=b.cod_usuario && b.nombre=?");
            pps.setString(1, nombre_boxeador);
            
            
            ResultSet result=pps.executeQuery();
            while(result.next()){
                respuesta=result.getLong(1)+"";
                
            }                       
        }catch(SQLException e){e.printStackTrace();}
        return respuesta;
    }
    
    public boolean comprobarSiExisteCompeticionMismoNombre(String nombre_competicion){
        Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();          
        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement("Select nombre from competicion where nombre=?");
            pps.setString(1, nombre_competicion);
            
            ResultSet result=pps.executeQuery();
            while(result.next()){
                return true;
            }
                       
        }catch(SQLException e){e.printStackTrace();}
        return false;
    }
    
    public String registrarCompeticion(String nombre_competicion,String fecha,String categoria,String email){
        String respuesta="0&9";
          Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();

        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement("INSERT INTO Competicion (nombre,categoria,fecha_comienzo,cod_creador,compe_activa)  VALUES (?,?,?,?,?)");
            pps.setString(1, nombre_competicion);
            pps.setString(2, categoria);
            pps.setString(3, fecha);            
            pps.setInt(4, devolverCodigoDeUsuario(email));
            pps.setBoolean(5, false);            
            if(pps.executeUpdate()>0){
                respuesta="7";
            }

        }catch(SQLException e){e.printStackTrace();}
        return respuesta;
    }
    
    public String modificarDatosBoxeador(String nombre_anterior,String nombre,String tipo_boxeador,String sexo, String peso,String pais,String categoria){
        String respuesta="0&10";
        Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();
        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement("UPDATE Boxeador SET nombre=?, tipo_boxeador=?, sexo=?, peso=?, pais=?, categoria=? where nombre=?");
            pps.setString(1,nombre);
            pps.setString(2,tipo_boxeador);
            pps.setString(3,sexo);
            pps.setDouble(4,Double.parseDouble(peso));
            pps.setString(5,pais);
            pps.setString(6,categoria);
            pps.setString(7,nombre_anterior);
            if(pps.executeUpdate()>0){
                respuesta="8";
            }

        }catch(SQLException e){e.printStackTrace();}
        
        return respuesta;
    }
    
    public String borrarBoxeador(String nombre_boxeador){
        String respuesta="0&11";
        
        Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();
        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement("DELETE FROM Boxeador where nombre=?");
            pps.setString(1,nombre_boxeador);
            
            if(pps.executeUpdate()>0){
                respuesta="9";
            }

        }catch(SQLException e){e.printStackTrace();}
        
        
        return respuesta;
    }
    
    public String obtenerEstadisticasBoxeador(String nombre_boxeador){
        String estadisticas = "";
        
        estadisticas=obtenerDatosBoxeador(nombre_boxeador)+"&"+obtenerInformacionAdicional(nombre_boxeador);
        
        return estadisticas;
    }
    
    public String obtenerDatosBoxeador(String nombre_boxeador){
        String respuesta="";
         Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();   
        String cod_emparejamiento;
        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement("Select tipo_boxeador, peso, categoria, pais from Boxeador where nombre=?");
            pps.setString(1, nombre_boxeador);
            
            
            ResultSet result=pps.executeQuery();
            while(result.next()){
                respuesta=result.getString(1)+"&"+result.getDouble(2)+"&"+result.getString(3)+"&"+result.getString(4);
                
            }                       
        }catch(SQLException e){e.printStackTrace();}
        return respuesta;
    }
    
    public String obtenerInformacionAdicional(String nombre_boxeador){
        String respuesta="";
         Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();   
        String cod_emparejamiento;
        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement("Select COUNT(p.cod_participacion) from Participacion p, Boxeador b where p.cod_boxeador=b.cod_boxeador && b.nombre=?");
            pps.setString(1, nombre_boxeador);
            ResultSet result=pps.executeQuery();
            while(result.next()){
                respuesta+=result.getInt(1)+"&";
                
            }
            System.out.println(respuesta+"");
            pps=(PreparedStatement) connection.prepareStatement("Select count(c.cod_combate) from Combate c, Boxeador b where b.cod_boxeador=c.ganador_combate && b.nombre=?");
            pps.setString(1, nombre_boxeador);
            result=pps.executeQuery();
            while(result.next()){
                respuesta+=result.getInt(1)+"&";
                
            }
            System.out.println(respuesta+"");
            pps=(PreparedStatement) connection.prepareStatement("Select count(c.cod_combate) from Combate c, Boxeador b where b.cod_boxeador=c.ganador_combate && b.nombre=? && tipo_victoria='KO'");
            pps.setString(1, nombre_boxeador);
            result=pps.executeQuery();
            while(result.next()){
                respuesta+=result.getInt(1)+"&";
                
            }
            System.out.println(respuesta+"");
            pps=(PreparedStatement) connection.prepareStatement("Select c.nombre from Competicion c, Boxeador b where c.poseedor_titulo=b.cod_boxeador && b.nombre=?");
            pps.setString(1, nombre_boxeador);
            result=pps.executeQuery();
            while(result.next()){
                respuesta+=result.getString(1)+"@";
                
            }
          System.out.println(respuesta+"");
            respuesta+="&";
            
            pps=(PreparedStatement) connection.prepareStatement("Select SUM(p.puntos_obtenidos), SUM(golpes_lanzados), SUM(golpes_conectados) from Participacion p, Boxeador b where p.cod_boxeador=b.cod_boxeador && b.nombre=?");
            pps.setString(1, nombre_boxeador);
            result=pps.executeQuery();
            while(result.next()){
                respuesta+=result.getInt(1)+"&"+result.getInt(2)+"&"+result.getInt(3);
                
            }
            System.out.println(respuesta+"");
        }catch(SQLException e){e.printStackTrace();}
        return respuesta;
    }
    
    public String obtenerCompeticionesParaApuntarse(String nombre_boxeador,int offset){
        String respuesta="";
        String categoria = obtenerCategoriaBoxeador(nombre_boxeador);
        Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();   
        String cod_emparejamiento;
        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement("Select count(c.nombre) from Competicion c where fecha_comienzo > now() && c.categoria = ? && c.poseedor_titulo IS NULL");
            
            pps.setString(1, categoria);
            
            ResultSet result=pps.executeQuery();
            while(result.next()){
                respuesta+=result.getInt(1)+"&";
                
            }
            
            pps=(PreparedStatement) connection.prepareStatement("Select c.nombre, u.email, c.fecha_comienzo, (Select count(a.cod_competicion) from Apuntarse a where a.cod_competicion = c.cod_competicion)" +
" from Competicion c, Usuario u where u.cod_usuario = c.cod_creador && c.fecha_comienzo > now() && c.categoria = ? && c.poseedor_titulo IS NULL GROUP BY c.cod_competicion LIMIT 5 OFFSET ?");
            
            pps.setString(1, categoria);
            pps.setInt(2, offset);
            
            result=pps.executeQuery();
            while(result.next()){
                respuesta+=result.getString(1)+";"/*+result.getString(2)+";"*/+result.getString(3)+";"+result.getInt(4)+"&";
                
            }                       
        }catch(SQLException e){e.printStackTrace();}
        return respuesta;
    }
    
    public String obtenerCategoriaBoxeador(String nombre_boxeador){
        String categoria="";
        Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();   
        String cod_emparejamiento;
        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement("Select categoria from Boxeador where nombre = ?");
            
            pps.setString(1, nombre_boxeador);
            
            ResultSet result=pps.executeQuery();
            while(result.next()){
                categoria=result.getString(1);
                
            }                       
        }catch(SQLException e){e.printStackTrace();}
        return categoria;
    }
    
    public String apuntarBoxeadorACompeticion(String nombre_boxeador,String nombre_competicion){
        String respuesta="0&13";
        Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();

        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement("INSERT INTO Apuntarse(cod_boxeador,cod_competicion)"+
            "VALUES ((Select b.cod_boxeador from Boxeador b where b.nombre = ?),(Select c.cod_competicion from Competicion c where c.nombre = ?))");
            pps.setString(1, nombre_boxeador);
            pps.setString(2, nombre_competicion);                       
            if(pps.executeUpdate()>0){
                respuesta="16";
            }

        }catch(SQLException e){e.printStackTrace();}
        return respuesta;
    }
    
    public boolean comprobarSiYaApuntadoEnCompeticion(String nombre_boxeador,String nombre_competicion){
        
        Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();          
        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement("Select a.cod_boxeador from Apuntarse a where"+
                    " a.cod_boxeador=(Select b.cod_boxeador from Boxeador b where b.nombre = ?) && a.cod_competicion = (Select c.cod_competicion from competicion c where c.nombre = ?)"+
                    " ");
            pps.setString(1, nombre_boxeador);
            pps.setString(2, nombre_competicion);
            
            ResultSet result=pps.executeQuery();
            while(result.next()){
                return true;
            }
                       
        }catch(SQLException e){e.printStackTrace();}
        return false;
    }
    
    public void guardarDatosCombate( int asaltos, String tipoVictoria,String ganador,String campeonato){
        boolean registro_exito=false;
        Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();
        String consulta;
            if(campeonato.equals("Partida rapida")){
                consulta = "INSERT INTO Combate (numero_asaltos,tipo_victoria,ganador_combate,fecha)"+
                        "  VALUES (?,?,(Select cod_boxeador from Boxeador where nombre = ?),(Select NOW()))";
            }else{
                consulta = "INSERT INTO Combate (numero_asaltos,tipo_victoria,ganador_combate,cod_competicion,fecha)"+
                        "  VALUES (?,?,(Select cod_boxeador from Boxeador where nombre = ?),(Select cod_competicion from Competicion where nombre = ?),(Select NOW()))";
            }
        
        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement(consulta);
            pps.setInt(1, asaltos);
            pps.setString(2, tipoVictoria);
            pps.setString(3, ganador);

            if(!campeonato.equals("Partida rapida")){
                pps.setString(4, campeonato);
            }
            
            if(pps.executeUpdate()>0){
                registro_exito=true;
            }
            
        }catch(SQLException e){e.printStackTrace();}
        
        System.out.println("Registro Combate --> "+registro_exito);
    }
    
    public void guardardatosParticipacion(int golpes_lanzados,int golpes_conectados,int salud,String nombre_boxeador,String nombre_ganador){
        Conexion cn=new Conexion();
         boolean registro_exito=false;
        Connection connection=(Connection) cn.conectar();
        int puntuacion = golpes_conectados;
        if(nombre_boxeador.equals(nombre_ganador)){
            puntuacion += 15;
        }else{
            puntuacion -= 5;
            
            if( puntuacion < 0 ){
                puntuacion = 0;
            }
            
        }
        String consulta = "INSERT INTO Participacion (golpes_lanzados,golpes_conectados,puntos_obtenidos,salud,cod_boxeador,cod_combate)"+
        " VALUES (?,?,?,?,(Select cod_boxeador from Boxeador where nombre = ?),"+
        "(Select cod_combate from Combate where ganador_combate = (Select cod_boxeador from Boxeador where nombre = ?) ORDER BY cod_combate DESC LIMIT 1));";
        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement(consulta);
            pps.setInt(1, golpes_lanzados);
            pps.setInt(2, golpes_conectados);
            pps.setInt(3, puntuacion);
            pps.setInt(4, salud);
            pps.setString(5, nombre_boxeador);
            pps.setString(6, nombre_ganador);                        

            
            
            if(pps.executeUpdate()>0){
                registro_exito=true;
            }
            
        }catch(SQLException e){e.printStackTrace();}
        
        System.out.println("Registro psrticipacion --> "+registro_exito);
    }
    
    public String obtenerCompeticionesEmpezadasYNoAcabadas(String nombre_boxeador){
        String respuesta="Partida rapida&";
        boolean terminada = false;
        Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();   

        try{
            
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement("Select c.nombre from Competicion c, Boxeador b, Apuntarse a where b.cod_boxeador = a.cod_boxeador && b.nombre = ? && a.cod_competicion = c.cod_competicion" +
" && c.fecha_comienzo <= NOW() && c.poseedor_titulo IS NULL");
            
            pps.setString(1, nombre_boxeador);
            
            ResultSet result=pps.executeQuery();
            while(result.next()){
                respuesta+=result.getString(1)+"&";
               
            }                       
        }catch(SQLException e){e.printStackTrace();}
        return respuesta;
    }
    
    public String obtenerInformacionCompeticion(String nombre_boxeador, String nombre_competicion){ // POR HACER
        String respuesta = "";
        int posicion_boxeador;
        int mi_posicion;
        Conexion cn=new Conexion();
        Connection connection=(Connection) cn.conectar();          
        try{
            PreparedStatement pps=(PreparedStatement) connection.prepareStatement("");
           // pps.setString(1, email);
            
            
            ResultSet result=pps.executeQuery();
            while(result.next()){
                
                
            }                       
        }catch(SQLException e){e.printStackTrace();}
        return respuesta;
    }
}
