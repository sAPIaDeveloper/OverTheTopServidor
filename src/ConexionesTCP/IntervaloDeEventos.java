/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConexionesTCP;

import ClasesComunes.InformacionCompartida;

/**
 *
 * @author josea
 */
public class IntervaloDeEventos extends Thread{
    
    InformacionCompartida info;
    SesionBoxeadorTCP sesionPlayer1;
    SesionBoxeadorTCP sesionPlayer2;
    Thread combate;
    String cod_emparejamiento_player1;
    String cod_emparejamiento_player2;
    public IntervaloDeEventos(InformacionCompartida info, SesionBoxeadorTCP sesionPlayer1, SesionBoxeadorTCP sesionPlayer2,Thread combate,String cod_emparejamiento_player1,String cod_emparejamiento_player2) {
        this.info = info;
        this.sesionPlayer1 = sesionPlayer1;
        this.sesionPlayer2 = sesionPlayer2;
        this.combate = combate;
        this.cod_emparejamiento_player1 =  cod_emparejamiento_player1;
        this.cod_emparejamiento_player2 =  cod_emparejamiento_player2;
    }

    @Override
    public void run() {
        boolean activo=true;
        while(activo){
            try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            //e.printStackTrace();
                System.err.println("Mando uno");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e2) {
                System.err.println("Mando uno");
               // e.printStackTrace();
            }
            if(!combate.isAlive()){activo=false;}else{
                sesionPlayer1.interrupt();
            sesionPlayer2.interrupt();
            }
            
            
        }
            
            
        }
        info.addMensajeParaHiloMoviles(cod_emparejamiento_player1, "5");
        info.addMensajeParaHiloMoviles(cod_emparejamiento_player2, "5");
        info.avisarLeerDatoAHiloMovil(cod_emparejamiento_player1);
        info.avisarLeerDatoAHiloMovil(cod_emparejamiento_player2);
        System.err.println("Hilo intervalo termino");
    }
    
    
}
