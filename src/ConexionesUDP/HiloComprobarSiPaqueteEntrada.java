/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConexionesUDP;

import ClasesComunes.InformacionCompartida;
import java.net.DatagramSocket;

/**
 *
 * @author Administrador
 */
public class HiloComprobarSiPaqueteEntrada extends Thread{

    InformacionCompartidaUDP informacionCompartidaUDP;
    InformacionCompartida info;
    DatagramSocket socket;
    public HiloComprobarSiPaqueteEntrada(InformacionCompartidaUDP informacionCompartidaUDP,InformacionCompartida info,DatagramSocket socket) {
        this.informacionCompartidaUDP = informacionCompartidaUDP;
        this.info=info;
        this.socket=socket;
    }
    
    
    
    @Override
    public void run() {
        while(true){
            if(informacionCompartidaUDP.paqueteEntradaPorTratar()){
                new HiloTratarYEnviarPaqueteUDP(informacionCompartidaUDP.getPaqueteEntrada(),info,socket).start();
            }else{
                try{
                    //System.out.println("-- El hilo HiloComprobarSiPaqueteEntrada se dormira 15000000 segundos --");
                    Thread.sleep(15000000);                
                }catch(InterruptedException ie){
                   // System.err.println("El hilo que comprueba si hay paquetes de entrada por tratar fue interrumpido");
                }
            }
            
            
        }
    }
    
}
