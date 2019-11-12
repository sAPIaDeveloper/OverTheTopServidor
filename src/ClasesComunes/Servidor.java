package ClasesComunes;


import ConexionesTCP.SesionBoxeadorTCP;
import ConexionesUDP.SesionBoxeadorUDP;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.TreeMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author josea
 */
public class Servidor {
    static TreeMap<String,String> g=new TreeMap();
    public static void main(String[] args) {
        Servidor s=new Servidor();      
        InformacionCompartida info=new InformacionCompartida();
        
        
        try {
            Servidor server=new Servidor();
            ServerSocket serverSocket = null;
            boolean listening = true;
            serverSocket = new ServerSocket(4444);
            new HiloCompruebaSiUsuarioLogueado(info).start();
            new SesionBoxeadorUDP(info).start();
            
            while (listening) {
                
                new SesionBoxeadorTCP(serverSocket.accept(),info).start();
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
            
    }
    
    
    
}
