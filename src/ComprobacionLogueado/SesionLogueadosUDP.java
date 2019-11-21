/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComprobacionLogueado;

import ClasesComunes.InformacionCompartida;
import ConexionesUDP.HiloComprobarSiPaqueteEntrada;
import ConexionesUDP.InformacionCompartidaUDP;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 *
 * @author josea
 */
public class SesionLogueadosUDP extends Thread{
    protected DatagramSocket socket = null;
    protected BufferedReader in = null;    
    InformacionCompartida info;
     Thread hiloEscribirDatos;
     InformacionCompartidaUDP informacionCompartidaUDP;
    public SesionLogueadosUDP(InformacionCompartida info,InformacionCompartidaUDP informacionCompartidaUDP) {        
        this.info=info;        
        this.informacionCompartidaUDP = informacionCompartidaUDP;
        try {
            socket= new DatagramSocket(5555);
            hiloEscribirDatos=new HiloEscribirDatos(informacionCompartidaUDP,info,socket);
            hiloEscribirDatos.start();
        } catch (IOException e) {
            System.err.println("Error al crear el DatagramSocket en SesionLigueadosUDP");
        }
    }

    @Override
    public void run() {        
        try {
            while(true){                
                byte[] bufIn = new byte[256]; 
                DatagramPacket paqueteEntrada = new DatagramPacket(bufIn, bufIn.length);                               
                socket.receive(paqueteEntrada);                
                informacionCompartidaUDP.addPaqueteEntradaLogueado(paqueteEntrada);                
                hiloEscribirDatos.interrupt();                                      
            }
            
        } catch (IOException e) {
            System.err.println("Error de E/S en la clase SesionLigueadosUDP");
        }
    }
    
     
}
