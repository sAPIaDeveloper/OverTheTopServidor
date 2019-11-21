/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComprobacionLogueado;

import ClasesComunes.Codigos_Servidor;
import ClasesComunes.InformacionCompartida;
import ConexionesUDP.HiloTratarYEnviarPaqueteUDP;
import ConexionesUDP.InformacionCompartidaUDP;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author josea
 */
public class HiloEscribirDatos extends Thread {
    InformacionCompartidaUDP informacionCompartidaUDP;
    InformacionCompartida info;
    DatagramSocket socket;
    public HiloEscribirDatos(InformacionCompartidaUDP informacionCompartidaUDP,InformacionCompartida info,DatagramSocket socket) {
        this.informacionCompartidaUDP = informacionCompartidaUDP;
        this.info=info;
        this.socket=socket;
    }
    
     @Override
    public void run() {
        DatagramPacket paquete_entrada;
        
        while(true){
            if(informacionCompartidaUDP.paqueteEntradaPorTratarLogueado()){                
                try {                      
                    paquete_entrada = informacionCompartidaUDP.getPaqueteEntradaLogueado();
                    String recibido = new String(paquete_entrada.getData(), 0, paquete_entrada.getLength());
                    tratarMensaje(recibido);
                    String respuesta_servidor=tratarMensaje(recibido);
                    if(!respuesta_servidor.equals("")){
                        byte bufOut[]=respuesta_servidor.getBytes();               
                        InetAddress address = paquete_entrada.getAddress();
                        int port = paquete_entrada.getPort();                               
                        DatagramPacket packetOut = new DatagramPacket(bufOut, bufOut.length, address, port);                               
                        socket.send(packetOut);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                
                
                
            }else{
                try{                   
                    Thread.sleep(15000000);                
                }catch(InterruptedException ie){
                   //ie.printStackTrace();                    
                }
            }
            
            
        }
    }
    
    public String tratarMensaje(String mensaje){        
        String respuesta="";
        String argumentos[]=mensaje.split("&");
        int codigo=Integer.parseInt(argumentos[0]);
        switch(Codigos_Servidor.codigo_servidor(codigo)){                        
            case SIGO_ACTIVO:
               
                info.setSeñalDeVida(argumentos[1]); 
               respuesta = "17";
                break;
                
            
        }
        return respuesta;
        
    }
    
}
