/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClasesComunes;

/**
 *
 * @author josea
 */
public class HiloCompruebaSiUsuarioLogueado extends Thread {
    InformacionCompartida info;

    public HiloCompruebaSiUsuarioLogueado(InformacionCompartida info) {
        this.info = info;
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(40000);
                String lista = info.obtenerTodosJugadoresLogueados();
                String jugadores[]= lista.split("&");  
                if(!jugadores[0].equals("")){                                      
                    for (int i = 0; i < jugadores.length; i++) { 
                        System.out.println("Lista "+jugadores[i]);
                        info.comprobarSiExisteSeñalDeVida(jugadores[i]);

                    }
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
}
