/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConexionesTCP;

import BaseDeDatos.AccesoBaseDatos;
import ClasesComunes.InformacionCompartida;

/**
 *
 * @author josea
 */
public class Combate extends Thread {
    Player player1;
    Player player2;
    SesionBoxeadorTCP sesionPlayer1;
    SesionBoxeadorTCP sesionPlayer2;
    String campeonato;
    int asaltos;
    long tiempoAsalto;
    String tipoVictoria;
    String ganador;
    boolean combateActivo;
    AccesoBaseDatos bbdd;
    InformacionCompartida info;
    public Combate(Player player1, Player player2, SesionBoxeadorTCP sesionPlayer1, SesionBoxeadorTCP sesionPlayer2,InformacionCompartida info,String campeonato) {
        this.campeonato = campeonato;
        System.out.println("El campe"+this.campeonato);
        this.player1 = player1;
        this.player2 = player2;
        this.sesionPlayer1 = sesionPlayer1;
        this.sesionPlayer2 = sesionPlayer2;
        this.info=info;
        asaltos=1;
        bbdd = new AccesoBaseDatos();
        //tiempo 100000
        tiempoAsalto=6000000;
        combateActivo=true;
    }

    @Override
    public void run() {
       Thread t= new IntervaloDeEventos(info,sesionPlayer1,sesionPlayer2,this);
       info.addCodEmparejamientoHiloIntervalo(player1.getCodEmparejamiento(),t);
       info.addCodEmparejamientoHiloIntervalo(player2.getCodEmparejamiento(),t);
       t.start();
        while(combateActivo){
            try{
                System.err.println("Empezo el combate en el servidor");
                player1.limpiarAcciones();
                player2.limpiarAcciones();
                Thread.sleep(tiempoAsalto);                               
                         
            }catch(InterruptedException ie){
                //Alguien perdio
                System.err.println("Asalto: "+player1.getAsalto());
                if(comprobarSiTerminoCombate()){
                    combateActivo=false;
                }
            }
        }
        
        guardarDatosCombate();
        guardarDatosParticipacion();
        if(!campeonato.equals("Partida rapida")){
            comprobarSiCompeticionTerminada();
        }
        
        System.out.println("Termino el combate");
        info.addMensajeParaHiloEscritorio(player1.getCodEmparejamiento(), "5@"+ganador);
        info.addMensajeParaHiloEscritorio(player2.getCodEmparejamiento(), "5@"+ganador);
        sesionPlayer1.interrupt(); 
        sesionPlayer2.interrupt(); 
        
        
        
    }
    
    public boolean comprobarSiTerminoCombate(){
        
        if(player1.getSalud()<=0 || player2.getSalud()<=0){
            tipoVictoria="KO";
            if(player1.getSalud()<=0){
                ganador=player2.getNombre_boxeador();
            }else{
                ganador=player1.getNombre_boxeador();
            }
            return true;
        }else if(player1.getAsalto() > 3 || player2.getAsalto() > 3){
            tipoVictoria="PUNTOS";
            if(player1.getSalud()> player2.getSalud()){
                ganador=player1.getNombre_boxeador();
            }else if(player1.getSalud()< player2.getSalud()){
                ganador=player2.getNombre_boxeador();
            }else{
                if(player1.getGolpesConectados()> player2.getGolpesConectados()){
                    ganador=player1.getNombre_boxeador();
                }else{
                    ganador=player2.getNombre_boxeador();
                }
            }
            return true;
        }
        return false;
    }
    
    public void guardarDatosCombate(){        
        bbdd.guardarDatosCombate(asaltos,tipoVictoria,ganador,campeonato);
        
        
        
    }
    
    public void guardarDatosParticipacion(){
        bbdd.guardardatosParticipacion(player1.getGolpesLanzados(), player1.getGolpesConectados(), player1.getSalud(), player1.getNombre_boxeador(), ganador);
        bbdd.guardardatosParticipacion(player2.getGolpesLanzados(), player2.getGolpesConectados(), player2.getSalud(), player2.getNombre_boxeador(), ganador);
    }
    
    public void comprobarSiCompeticionTerminada(){
        bbdd.comprobarSiCompeticionTerminada(campeonato);
    }
    
    
}
