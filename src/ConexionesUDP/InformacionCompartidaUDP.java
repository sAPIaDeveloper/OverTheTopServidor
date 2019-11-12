/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConexionesUDP;

import java.net.DatagramPacket;
import java.util.Stack;

/**
 *
 * @author Administrador
 */
public class InformacionCompartidaUDP {
    Stack paquetes_entrada;
    

    public InformacionCompartidaUDP() {
        paquetes_entrada=new Stack();     
        
    }
    
    public synchronized void addPaqueteEntrada(DatagramPacket paquete_entrada){
        paquetes_entrada.push(paquete_entrada);
    }
    
    public synchronized boolean paqueteEntradaPorTratar(){
        return !paquetes_entrada.isEmpty();
    }
    
    public synchronized DatagramPacket getPaqueteEntrada(){
        DatagramPacket entrada= (DatagramPacket) paquetes_entrada.pop();       
        return entrada;            
    }
}
