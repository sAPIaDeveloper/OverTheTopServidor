package ClasesComunes;


import ConexionesTCP.Combate;
import ConexionesTCP.IntervaloDeEventos;
import ConexionesTCP.Player;
import ConexionesTCP.SesionBoxeadorTCP;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.util.TreeMap;

/**
 *
 * @author josea
 */
public class InformacionCompartida {
    LinkedList<String> jugadores_logueados;
    LinkedList<String> moviles_logueados;
    TreeMap<String,Stack<String>> campeonato_listaJugadoresEnEsperaPartida; 
    TreeMap<String,Stack<String>> listaMensajesParaHiloEscritorio;
    TreeMap<String,Stack<String>> listaMensajesParaHiloMoviles;
    
    ArrayList<String> señal_vida;
    //-----------
    TreeMap<String,SesionBoxeadorTCP> cod_emparejamiento_hiloEscritorio; 
    //-----------
    TreeMap<String,SesionBoxeadorTCP> cod_emparejamiento_hiloMoviles; 
    //-----------
    TreeMap<String,Player> cod_emparejamiento_informacionJugador;
    TreeMap<Integer,ArrayList<String>> indice_combate_jugadores;
    
    TreeMap<Integer,Thread> indice_combate_combate;
    
    TreeMap<String,Thread> cod_emparejamiento_hilointervaloEvento;        
    
    TreeMap<Integer,Integer> cod_combate_numero_participantes_preparados;
    //-----------
    public InformacionCompartida() {
        jugadores_logueados=new LinkedList<String>();
        moviles_logueados=new LinkedList<String>();
        campeonato_listaJugadoresEnEsperaPartida=new TreeMap();
        listaMensajesParaHiloEscritorio=new TreeMap();
        listaMensajesParaHiloMoviles=new TreeMap();
        cod_emparejamiento_hiloEscritorio=new TreeMap();
        cod_emparejamiento_hiloMoviles=new TreeMap();
        cod_emparejamiento_informacionJugador=new TreeMap();
        indice_combate_jugadores=new TreeMap();
        indice_combate_combate=new TreeMap();
        cod_combate_numero_participantes_preparados=new TreeMap();
        cod_emparejamiento_hilointervaloEvento=new TreeMap();
        señal_vida = new ArrayList();
    }

    public synchronized int numeroLogueados(){
        return jugadores_logueados.size();
    }
    
    public synchronized void borrarJugadorEnListaEspera(String competicion,String codEmparejamiento){
        Stack s = campeonato_listaJugadoresEnEsperaPartida.get(competicion);
        s.remove(codEmparejamiento);
    }
    
    public synchronized void addHiloEscritorio(String cod_emparejamiento,SesionBoxeadorTCP sesion){
        cod_emparejamiento_hiloEscritorio.put(cod_emparejamiento, sesion);
    }

    public synchronized void addHiloMovil(String cod_emparejamiento,SesionBoxeadorTCP sesion){
        cod_emparejamiento_hiloMoviles.put(cod_emparejamiento, sesion);
    }

    public synchronized void addCodEmparejamientoHiloIntervalo(String cod_emparejamiento,Thread intervalo){
        cod_emparejamiento_hilointervaloEvento.put(cod_emparejamiento, intervalo);
    }

    public synchronized void avisarLeerDatoAHiloEscritorio(String cod_emparejamiento){
        if(cod_emparejamiento_hiloEscritorio.containsKey(cod_emparejamiento)){
            cod_emparejamiento_hiloEscritorio.get(cod_emparejamiento).interrupt();
        }

    }

    public synchronized void avisarLeerDatoAHiloMovil(String cod_emparejamiento){
        if(cod_emparejamiento_hiloMoviles.containsKey(cod_emparejamiento)){
            cod_emparejamiento_hiloMoviles.get(cod_emparejamiento).interrupt();
        }

    }

    public synchronized void avisarHiloIntervalo(String cod_emparejamiento){
        cod_emparejamiento_hilointervaloEvento.get(cod_emparejamiento).interrupt();
    }

    public synchronized void addMensajeParaHiloEscritorio(String cod_emparejamiento,String mensaje){
        if(listaMensajesParaHiloEscritorio.containsKey(cod_emparejamiento)){
            Stack s=listaMensajesParaHiloEscritorio.get(cod_emparejamiento);
            s.push(mensaje);
        }else{
            Stack mensajes=new Stack();            
            mensajes.add(mensaje);
            listaMensajesParaHiloEscritorio.put(cod_emparejamiento, mensajes);
        }
    }

    public synchronized void addMensajeParaHiloMoviles(String cod_emparejamiento,String mensaje){        
        if(listaMensajesParaHiloMoviles.containsKey(cod_emparejamiento)){
            Stack s=listaMensajesParaHiloMoviles.get(cod_emparejamiento);
            s.push(mensaje);
        }else{
            Stack mensajes=new Stack();            
            mensajes.add(mensaje);
            listaMensajesParaHiloMoviles.put(cod_emparejamiento, mensajes);
        }
    }

    public synchronized String getMensajeParaHiloEscritorio(String cod_emparejamiento){
        Stack s=listaMensajesParaHiloEscritorio.get(cod_emparejamiento);
        String mensaje= (String) s.pop();
        return mensaje;
    }

    public synchronized String getMensajeParaHiloMoviles(String cod_emparejamiento){
        Stack s=listaMensajesParaHiloMoviles.get(cod_emparejamiento);
        String mensaje= (String) s.pop();
        return mensaje;
    }

    public synchronized void addUsuarioListaLogueados(String cod_emparejamiento){
        jugadores_logueados.add(cod_emparejamiento);

    } 

    public synchronized boolean comprobarUsuarioListaLogueados(String cod_emparejamiento){        
        return jugadores_logueados.contains(cod_emparejamiento);
    }

    public synchronized boolean comprobarUsuarioListaLogueadosEscritorio(String cod_emparejamiento){        
        return cod_emparejamiento_hiloEscritorio.containsKey(cod_emparejamiento);
    }

    public synchronized void removeUsuarioListaLogueados(String cod_emparejamiento){
        jugadores_logueados.remove(cod_emparejamiento);
    }

    public synchronized void addMovilListaLogueados(String cod_emparejamiento){
        moviles_logueados.add(cod_emparejamiento);

    }

    public synchronized void removeMovilListaLogueados(String cod_emparejamiento){
        moviles_logueados.remove(cod_emparejamiento);
    }

    public synchronized String comprobarMovilLogueado(String cod_emparejamiento){
        String respuesta="";
        if(moviles_logueados.contains(cod_emparejamiento)){
            respuesta="4";
        }else respuesta="0&6";

        return respuesta;
    }

    public synchronized String comprobarOPonerEnlistaDeEsperaParaPartidaDeUnCampeonato(String campeonato,String cod_emparejamiento_jugador,String nombre_boxeador,String tipo_boxeador){
         String respuesta="";
         int indice_combate_actual;
         if(campeonato_listaJugadoresEnEsperaPartida.containsKey(campeonato)){ // compruebo que ya este el campeonato en lista
             Stack s=campeonato_listaJugadoresEnEsperaPartida.get(campeonato); // obtengo la primera persona en espera para jugar
             if(!s.isEmpty()){
                 //Emparejar jugadores
                 String cod_emparejamiento_contrincante= (String) s.pop();

                 Player p1=new Player(cod_emparejamiento_jugador,tipo_boxeador,nombre_boxeador);  
                 cod_emparejamiento_informacionJugador.put(cod_emparejamiento_jugador, p1);
                 //Creo un indice para identificar el combate en el lado servidor. No es el codigo que irá en la bbdd.
                 indice_combate_actual= (int) (Math.random()*1000)+1;
                 while(indice_combate_combate.containsKey(indice_combate_actual)){
                     indice_combate_actual= (int) (Math.random()*1000)+1;
                 }

                 Thread combate=new Combate(p1,cod_emparejamiento_informacionJugador.get(cod_emparejamiento_contrincante),cod_emparejamiento_hiloEscritorio.get(cod_emparejamiento_jugador),cod_emparejamiento_hiloEscritorio.get(cod_emparejamiento_contrincante),this,campeonato);

                 indice_combate_combate.put(indice_combate_actual, combate);//guardo el combate asociado al indice

                 //tengo que informar al otro jugador de que el oponente fue encontrado y darle el codigo de la partida.
                 String mensajeParaContrincante="1@"+indice_combate_actual+"@"+cod_emparejamiento_informacionJugador.get(cod_emparejamiento_contrincante).getTipo_boxeador()+
                    "@"+tipo_boxeador+"@"+cod_emparejamiento_informacionJugador.get(cod_emparejamiento_contrincante).getNombre_boxeador()+"@"+nombre_boxeador;// Para los mensajes de interrupcion es @

                 addMensajeParaHiloEscritorio(cod_emparejamiento_contrincante,mensajeParaContrincante);
                 avisarLeerDatoAHiloEscritorio(cod_emparejamiento_contrincante);

                 // Aqui concateno con & porque se manda directamente al cliente sin pasar por comprobacion de interrupcion
                 respuesta="6&"+tipo_boxeador+"&"+cod_emparejamiento_informacionJugador.get(cod_emparejamiento_contrincante).getTipo_boxeador()+"&"
                    +nombre_boxeador+"&"+cod_emparejamiento_informacionJugador.get(cod_emparejamiento_contrincante).getNombre_boxeador()+"&"+indice_combate_actual;
             }else{// No hay ningun jugador esperando   
                 Player p1=new Player(cod_emparejamiento_jugador,tipo_boxeador,nombre_boxeador);
                 cod_emparejamiento_informacionJugador.put(cod_emparejamiento_jugador, p1);
                 s.push(cod_emparejamiento_jugador);
                 respuesta="0&7";
             }
         }else{// Es el primer boxeador en darle a jugar para ese campeonato
             Stack jugadoresEnEsperaPartida=new Stack();
             jugadoresEnEsperaPartida.push(cod_emparejamiento_jugador);
             campeonato_listaJugadoresEnEsperaPartida.put(campeonato, jugadoresEnEsperaPartida);      
             Player p1=new Player(cod_emparejamiento_jugador,tipo_boxeador,nombre_boxeador);
             cod_emparejamiento_informacionJugador.put(cod_emparejamiento_jugador, p1);
             respuesta="0&7";
         }

         return respuesta;
    }

    public synchronized void ponerJugadorPreparado(int codPartida, String codEmparejamiento){
        if(cod_combate_numero_participantes_preparados.containsKey(codPartida)){
            indice_combate_jugadores.get(codPartida).add(codEmparejamiento);
            indice_combate_combate.get(codPartida).start();
        }else{
            cod_combate_numero_participantes_preparados.put(codPartida, 1);
            ArrayList<String> jugadores=new ArrayList();
            jugadores.add(codEmparejamiento);
            indice_combate_jugadores.put(codPartida, jugadores);

        }        
    }

    public synchronized void addEventoMandoAJugadores(int codPartida,String codEmparejamiento,String accion){// mirar bien        
        ArrayList<String> codigos=indice_combate_jugadores.get(codPartida);
        if(codigos == null){
            System.out.println("Error null");
        }else{
            for (int i = 0; i < codigos.size(); i++) {
                String codEmpare=codigos.get(i);
                if(!codEmpare.equals(codEmparejamiento)){
                    cod_emparejamiento_informacionJugador.get(codEmpare).addAccionContrincante(accion);
                    addMensajeParaHiloEscritorio(codEmpare,"0");
                    //avisarLeerDatoAHiloEscritorio(codEmpare);
                }else{
                    cod_emparejamiento_informacionJugador.get(codEmpare).addAccionBoxeador(accion);
                }
            }
        }
    }

    public synchronized String getEventoMandoContrincante(String codEmparejamiento){        
        return cod_emparejamiento_informacionJugador.get(codEmparejamiento).getAccionContrincante();
    }

    public synchronized String getEventoMandos(int codPartida,String codEmparejamiento){     
        String respuesta="";
        Player p= cod_emparejamiento_informacionJugador.get(codEmparejamiento);
        respuesta = p.getAccionBoxeador() + "&" + p.getAccionContrincante();
        return respuesta;
    }

    public synchronized void actualizarInformacionPlayerCombate(String cod_emparejamiento,int vida, int golpesLanzados , int golpesConectados){
        Player p = cod_emparejamiento_informacionJugador.get(cod_emparejamiento);
        p.setSalud(vida);
        p.setGolpesConectados(golpesConectados);
        p.setGolpesLanzados(golpesLanzados);

    }

    public synchronized void actualizarAsalto(String cod_emparejamiento,int asalto){
        Player p = cod_emparejamiento_informacionJugador.get(cod_emparejamiento);
        p.setAsalto(asalto);
    }

    public synchronized void setSeñalDeVida(String cod_emparejamiento){
         if(!señal_vida.contains(cod_emparejamiento)){
             señal_vida.add(cod_emparejamiento);
         }

    }

    public synchronized void borrarreferenciaHiloTCPEscritorio(String cod_emparejamiento){
        if(cod_emparejamiento_hiloEscritorio.containsKey(cod_emparejamiento)){
            cod_emparejamiento_hiloEscritorio.remove(cod_emparejamiento);
        }
        
    }
    
    public synchronized void comprobarSiExisteSeñalDeVida(String cod_emparejamiento){
        if(señal_vida.contains(cod_emparejamiento)){
            señal_vida.remove(cod_emparejamiento);
        }else{
            addMensajeParaHiloMoviles(cod_emparejamiento,"6");
            addMensajeParaHiloEscritorio(cod_emparejamiento,"6");
            avisarLeerDatoAHiloEscritorio(cod_emparejamiento);
            avisarLeerDatoAHiloMovil(cod_emparejamiento);
                       
            jugadores_logueados.remove(cod_emparejamiento);                   
            cod_emparejamiento_hilointervaloEvento.remove(cod_emparejamiento);
        }
    }

    public void eliminarDatosJugadorEnMemoria(String cod_emparejamiento,String rol,int codigoActual){
        System.out.println("Eliminare los datos en memoria del "+rol);
        if(rol.equals("movil")){            
            listaMensajesParaHiloMoviles.remove(cod_emparejamiento);
            cod_emparejamiento_hiloMoviles.remove(cod_emparejamiento);
            moviles_logueados.remove(cod_emparejamiento);
        }else{
            listaMensajesParaHiloEscritorio.remove(cod_emparejamiento);
            cod_emparejamiento_hiloEscritorio.remove(cod_emparejamiento);
            jugadores_logueados.remove(cod_emparejamiento);
            cod_emparejamiento_informacionJugador.remove(cod_emparejamiento);
            if(indice_combate_jugadores.containsKey(codigoActual)){
                indice_combate_jugadores.remove(codigoActual);
            }
            if(indice_combate_combate.containsKey(codigoActual)){
                indice_combate_combate.remove(codigoActual);
            }
            if(cod_combate_numero_participantes_preparados.containsKey(codigoActual)){
                cod_combate_numero_participantes_preparados.remove(codigoActual);
            }
            cod_emparejamiento_hilointervaloEvento.remove(cod_emparejamiento);
            for (String competicion : campeonato_listaJugadoresEnEsperaPartida.keySet()) {
                Stack s = campeonato_listaJugadoresEnEsperaPartida.get(competicion);
                if(s.contains(cod_emparejamiento)){
                    s.remove(cod_emparejamiento);
                }
            }
               // avisarLeerDatoAHiloMovil(cod_emparejamiento); 
        }
    }

    public String obtenerTodosJugadoresLogueados(){
        String jugadores = "";        
        for (int i = 0; i < jugadores_logueados.size(); i++) {
            jugadores += jugadores_logueados.get(i) + "&";            
        }

        return jugadores;
    }      

    public boolean comprobarSiDerrotado(String cod_emparejamiento){
       if(cod_emparejamiento_informacionJugador.get(cod_emparejamiento).getSalud() <= 0){return true;}
       return false;
    }

    public void avisarCombate(int cod_combate){
       indice_combate_combate.get(cod_combate).interrupt();
   }

}
