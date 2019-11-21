package ClasesComunes;


import ComprobacionLogueado.HiloCompruebaSiUsuarioLogueado;
import ConexionesTCP.SesionBoxeadorTCP;
import ConexionesUDP.SesionBoxeadorUDP;
import java.awt.Color;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Timer;
import java.util.TimerTask;
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
    public static int numeroLogueadosUltimaVez;
    public static void main(String[] args) {        
        Servidor s=new Servidor();      
        s.numeroLogueadosUltimaVez = 0;
        InformacionCompartida info=new InformacionCompartida();
        
        
        try {
            Servidor server=new Servidor();
            ServerSocket serverSocket = null;
            boolean listening = true;
            serverSocket = new ServerSocket(4444);
            new HiloCompruebaSiUsuarioLogueado(info).start();
            new SesionBoxeadorUDP(info).start();
            
            Timer timer;
            TimerTask tarea;
            
            PanelInformacion panel = new PanelInformacion();
            panel.setVisible(true);
            tarea = new TimerTask(){
                @Override
                public void run() {
                    int logueados = info.numeroLogueados();
                    long memoriaLibre = ((Runtime.getRuntime().freeMemory() + (Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()))/(1024*1024));
                    long memoriaUtilizada = ((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/(1024*1024));
                    if(logueados < numeroLogueadosUltimaVez){
                        Runtime.getRuntime().gc();
                    }
                    
                    if(memoriaLibre < 100){
                         panel.getMemoria_libre().setForeground(Color.red);
                    }else{
                        panel.getMemoria_libre().setForeground(Color.black);
                    }
                    numeroLogueadosUltimaVez = logueados;
                    panel.getJugadores_logueados().setText(logueados+"");
                    panel.getMemoria_libre().setText(memoriaLibre+" MB");
                    panel.getMemoria_utilizada().setText(memoriaUtilizada+" MB");
                }
                
            };
            
            timer = new Timer();
            
            timer.scheduleAtFixedRate(tarea, 2000, 2000);
            
            while (listening) {
                System.out.println("Acepto socket");
                new SesionBoxeadorTCP(serverSocket.accept(),info).start();
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
            
    }
    
    
    
}
