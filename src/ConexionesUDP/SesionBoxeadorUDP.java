package ConexionesUDP;


import ClasesComunes.InformacionCompartida;
import ClasesComunes.Codigos_Servidor;
import BaseDeDatos.AccesoBaseDatos;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author josea
 */
public class SesionBoxeadorUDP extends Thread{
    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    InformacionCompartidaUDP informacionCompartidaUDP;
    InformacionCompartida info;
     Thread hiloComprueba;

    public SesionBoxeadorUDP(InformacionCompartida info) {        
        this.info=info;
        informacionCompartidaUDP=new InformacionCompartidaUDP();
        
        try {
            socket= new DatagramSocket(4444);
            hiloComprueba=new HiloComprobarSiPaqueteEntrada(informacionCompartidaUDP,info,socket);
        hiloComprueba.start();
        } catch (IOException e) {
            System.err.println("Error al crear el DatagramSocket en SesionBoxeadorUDP");
        }
    }

    @Override
    public void run() {
        
        try {
            while(true){                
                byte[] bufIn = new byte[256]; 
                DatagramPacket paqueteEntrada = new DatagramPacket(bufIn, bufIn.length);                               
                socket.receive(paqueteEntrada);                
                informacionCompartidaUDP.addPaqueteEntrada(paqueteEntrada);
                hiloComprueba.interrupt();                                      
            }
            
        } catch (IOException e) {
            System.err.println("Error de E/S en la clase SesionBoxeadorUDP");
        }
    }
    
    
    
    
}
