/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConexionesUDP;

import BaseDeDatos.AccesoBaseDatos;
import ClasesComunes.Codigos_Servidor;
import ClasesComunes.InformacionCompartida;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author Administrador
 */
public class HiloTratarYEnviarPaqueteUDP extends Thread{
    
    private DatagramPacket paquete_entrada;
    private DatagramSocket socket;
    private AccesoBaseDatos bbdd;
    InformacionCompartida info;

    public HiloTratarYEnviarPaqueteUDP(DatagramPacket paquete_entrada, InformacionCompartida info,DatagramSocket socket) {
        this.paquete_entrada = paquete_entrada;
        this.info = info;
        this.socket=socket;
        bbdd=new AccesoBaseDatos();
    }

    

    @Override
    public void run() {
        try{
            
            
            byte[] bufIn = new byte[256];            
            
            String recibido = new String(paquete_entrada.getData(), 0, paquete_entrada.getLength());
            
            String respuesta_servidor=tratarMensaje(recibido);
             if(!respuesta_servidor.equals("")){
                byte bufOut[]=respuesta_servidor.getBytes();               
                InetAddress address = paquete_entrada.getAddress();
                int port = paquete_entrada.getPort();                               
                DatagramPacket packetOut = new DatagramPacket(bufOut, bufOut.length, address, port);                               
                socket.send(packetOut);
             }   
            
            
            
        }catch(IOException io){System.err.println("Error en el hilo de tratar paquete UDP");
            io.printStackTrace();
        }
    }
    
    
    
    public String tratarMensaje(String mensaje){        
        String respuesta="";
        String argumentos[]=mensaje.split("&");
        int codigo=Integer.parseInt(argumentos[0]);
        switch(Codigos_Servidor.codigo_servidor(codigo)){
            case REGISTRAR_USUARIO:
                    if(bbdd.existeEmailUsuario(argumentos[1])){ // Si existe
                        // Crear mensaje para decir que ya hay un usuario registrado con ese email.
                        respuesta="0&1";
                    }else{
                        long codigo_emparejamiento=generarCodigoEmparejamiento();
                        if(bbdd.registrarUsuario(argumentos[1], argumentos[2], codigo_emparejamiento)){                                                        
                            respuesta="1&"+codigo_emparejamiento; // generar respuesta de registro con exito
                            info.addUsuarioListaLogueados(codigo_emparejamiento+"");
                        }else respuesta="0&2"; // generar respuesta de registro sin exito

                    }
                break;
                
            case INICIAR_SESION:     
                    String codido_emparejamiento=bbdd.iniciarSesion(argumentos[1], argumentos[2]);
                    if(!codido_emparejamiento.equals("")){  
                        if(info.comprobarUsuarioListaLogueados(codido_emparejamiento)){
                            System.out.println("Ya ha iniciado sesion");
                            respuesta="0&12";
                        }else{
                            respuesta="2&"+codido_emparejamiento+"&"+bbdd.devolverBoxeadores(argumentos[1]);
                        info.addUsuarioListaLogueados(codido_emparejamiento);
                        }
                        
                        
                    }else{
                        respuesta="0&3";
                    }
                    
                break;
                
            case REGISTRAR_BOXEADOR:
                if(bbdd.comprobarSiExisteBoxeador(argumentos[2])){
                    respuesta="0&4";
                }else{
                    boolean registrado=bbdd.registrarBoxeador(bbdd.devolverCodigoDeUsuario(argumentos[1]), argumentos[2], argumentos[3], argumentos[4], argumentos[5], argumentos[6], argumentos[7]);
                    if(registrado){
                        respuesta="3";
                    }else{
                        respuesta="0&5";
                    }
                }
                break;
                
            case OBTENER_COMPETICIONES_APUNTADAS_BOXEADOR:
                respuesta=bbdd.obtenerCompeticionesEmpezadasYNoAcabadas(argumentos[1]);
                break;
                
            case CREAR_COMPETICION:
                if(bbdd.comprobarSiExisteCompeticionMismoNombre(argumentos[1])){
                    respuesta="0&8";
                }else{
                    //crear competicion
                    respuesta=bbdd.registrarCompeticion(argumentos[1], argumentos[2], argumentos[3], argumentos[4]);
                }
                break;
                
            case MODIFICAR_BOXEADOR:
                if(argumentos.length==7){
                    System.out.println(mensaje+"   "+argumentos.length);
                    respuesta=bbdd.modificarDatosBoxeador(argumentos[1], argumentos[1], argumentos[2], argumentos[3], argumentos[4], argumentos[5], argumentos[6]);
                }else{
                    if(bbdd.comprobarSiExisteBoxeador(argumentos[2])){
                        respuesta="0&4";
                    }else{
                        respuesta=bbdd.modificarDatosBoxeador(argumentos[1], argumentos[2], argumentos[3], argumentos[4], argumentos[5], argumentos[6], argumentos[7]);
                    }
                }
                    
                    break;
                    
            case BORRAR_BOXEADOR:
                    respuesta=bbdd.borrarBoxeador(argumentos[1]);
                break;
                
            case OBTENER_ESTADISTICAS_BOXEADOR:
                respuesta = "14&"+bbdd.obtenerEstadisticasBoxeador(argumentos[1]);
                System.out.println("Estadisticas: "+respuesta);
                break;
                
            case OBTENER_COMPETICIONES_CREADAS:
                respuesta = "15&"+bbdd.obtenerCompeticionesParaApuntarse(argumentos[1],Integer.parseInt(argumentos[2]));
                break;
                
            case APUNTAR_BOXEADOR_COMPETICION:
                if(!bbdd.comprobarSiYaApuntadoEnCompeticion(argumentos[1], argumentos[2])){
                    respuesta = bbdd.apuntarBoxeadorACompeticion(argumentos[1], argumentos[2]);
                }else{
                    respuesta = "0&14";
                }
                
                break;
            
            case SIGO_ACTIVO:
                info.setSeñalDeVida(argumentos[1]);                
                break;
                
            case OBTENER_INFORMACION_COMPETICIONES_APUNTADAS: // POR HACER
                String nombre_boxeador = argumentos[1] ;
                String nombre_competicion = argumentos[2] ;
                break;
        }
        
        return respuesta;
    }
    
    
    public long generarCodigoEmparejamiento(){
        long codigo_emparejamiento=0;
        boolean codigo_generado_exito=true;
        while(codigo_generado_exito){
            codigo_emparejamiento=System.currentTimeMillis();
            codigo_generado_exito=bbdd.existeCodigoEmparejamiento(codigo_emparejamiento);
        }
        return codigo_emparejamiento;
    }
}
