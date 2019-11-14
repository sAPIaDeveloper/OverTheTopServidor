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
    public IntervaloDeEventos(InformacionCompartida info, SesionBoxeadorTCP sesionPlayer1, SesionBoxeadorTCP sesionPlayer2,Thread combate) {
        this.info = info;
        this.sesionPlayer1 = sesionPlayer1;
        this.sesionPlayer2 = sesionPlayer2;
        this.combate = combate;
    }

    @Override
    public void run() {
        boolean activo=true;
        while(activo){
            try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            System.out.println("Alguien toco mando");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e2) {
                System.out.println("Alguien toco mando x 2 vez");
            }
            sesionPlayer1.interrupt();
            sesionPlayer2.interrupt();
            
        }
            if(!combate.isAlive()){activo=false;}
        }
        System.err.println("Hilo intervalo termino");
    }
    
    
}
