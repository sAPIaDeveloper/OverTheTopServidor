package ConexionesTCP;


import ClasesComunes.InformacionCompartida;
import ClasesComunes.Codigos_Servidor;
import BaseDeDatos.AccesoBaseDatos;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author josea
 */
public class SesionBoxeadorTCP extends Thread{
    String rolHilo;
    private Socket socket;
    private AccesoBaseDatos bbdd;
    InformacionCompartida info;
    private boolean listening;
    String cod_emparejamiento;
    int codigoPartidaActual;
    boolean combateCurso;
    public SesionBoxeadorTCP(Socket socket, InformacionCompartida info) {
        super("SesionBoxeador");
        this.socket = socket;
        this.info = info;
        combateCurso=false;
        rolHilo="escritorio";
    }

    
    @Override
    public void run() {
        System.out.println("Empieza la sesion...");
        bbdd=new AccesoBaseDatos();
        try {
            listening=true;
            String mensaje="";
            String respuesta="";
            String argumentos[];
            BufferedReader cliente = new BufferedReader (new InputStreamReader(socket.getInputStream()));
            PrintWriter print = new PrintWriter (socket.getOutputStream(), true);  
            mensaje=cliente.readLine();
            
            respuesta=tratarMensaje(mensaje);
            while(listening){ 
                if(!respuesta.isEmpty()){
                    print.println(respuesta);
                    respuesta="";
                }
                
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                   if(rolHilo.equals("movil")){
                       mensaje=info.getMensajeParaHiloMoviles(cod_emparejamiento);
                   }else mensaje=info.getMensajeParaHiloEscritorio(cod_emparejamiento);
                    
                    respuesta=tratarInterrupcion(mensaje);                    
                    if(!respuesta.equals("")){
                        System.out.println(respuesta);
                        print.println(respuesta);
                    }
                    
                    respuesta="";
                }
                if(cliente.ready()){
                    mensaje=cliente.readLine();
                    respuesta=tratarMensaje(mensaje);
                }
                
                
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        info.eliminarDatosJugadorEnMemoria(cod_emparejamiento, rolHilo,codigoPartidaActual);
        
        System.out.println("Termino hilo TCP");
    }
    
    
    public String tratarMensaje(String mensaje){
        String respuesta="";
        String datos[]=mensaje.split("&");
        int cod= Integer.parseInt(datos[0]);
        
        switch(Codigos_Servidor.codigo_servidor(cod)){
        
            case COMPROBAR_EMPAREJAMIENTO_MOVIL_MAS_DEVOLVER_COD:
                
                cod_emparejamiento=bbdd.obtenerCodigoEmparejamiento(datos[1]);
                info.addHiloEscritorio(cod_emparejamiento, this);
                if(info.comprobarMovilLogueado(cod_emparejamiento).equals("4")){
                    respuesta="4";
                }else
                respuesta="5&"+cod_emparejamiento;
                
                break;
                
                
            case COMPROBAR_EMPAREJAMIENTO:
                respuesta=info.comprobarMovilLogueado(cod_emparejamiento);
                break;
                
            case EMPAREJAR_MOVIL_ORDENADOR: // Esto para cuando el mensaje viene desde el movil no de la aplicacion escritorio
                
                cod_emparejamiento=datos[1];
                if(info.comprobarUsuarioListaLogueadosEscritorio(cod_emparejamiento)){
                    info.addMovilListaLogueados(datos[1]);
                    respuesta="4";
                    rolHilo="movil";
                    info.addHiloMovil(cod_emparejamiento, this);
                }else{
                    respuesta="0&1";
                }
                    
                break;
                
            case BUSCAR_OPONENTE:
                
                respuesta=info.comprobarOPonerEnlistaDeEsperaParaPartidaDeUnCampeonato(datos[2], cod_emparejamiento,datos[1],datos[3]);//campeonato , nombre_boxeador
                
                String indice[]=respuesta.split("&");
                if(indice.length > 5){
                    codigoPartidaActual=Integer.parseInt(indice[5]);
                }
                
                break;
                
            case ACCION_BOXEADOR:      
                
                    
                    //llamar aqui al intervalo
                    if(combateCurso){
                        String mensajeCombate[]= datos[1].split("@");
                        info.addMensajeParaHiloEscritorio(cod_emparejamiento, mensajeCombate[0]);
                        System.out.println("Mensajes hilo escritorio: "+mensajeCombate[0]+" accion: "+mensajeCombate[1]);
                        info.addEventoMandoAJugadores(codigoPartidaActual,cod_emparejamiento,devolverAccionMando(Integer.parseInt(mensajeCombate[1])));
                        info.avisarHiloIntervalo(cod_emparejamiento);
                    }else{
                        
                        info.addMensajeParaHiloEscritorio(cod_emparejamiento, datos[1]);
                        info.avisarLeerDatoAHiloEscritorio(cod_emparejamiento);}
                
                
                break;
                
            case PARTIDA_EMPEZADA:
                info.ponerJugadorPreparado(codigoPartidaActual,cod_emparejamiento);
                combateCurso=true;
                info.addMensajeParaHiloMoviles(cod_emparejamiento, "2@"+codigoPartidaActual);
                info.avisarLeerDatoAHiloMovil(cod_emparejamiento);
                break;
                
            case ACTUALIZACION_DATOS_BOXEADOR_COMBATE:
                int vida_restante = Integer.parseInt(datos[1]);
                int golpes_lanzados = Integer.parseInt(datos[2]);
                int golpes_conectados = Integer.parseInt(datos[3]);
           
                
                if(datos[4].equals("GOLPEADO")){
                    avisarVibrarMando();
                }
                info.actualizarInformacionPlayerCombate(cod_emparejamiento,vida_restante,golpes_lanzados,golpes_conectados);
                if(info.comprobarSiDerrotado(cod_emparejamiento)){
                    info.avisarCombate(codigoPartidaActual);
                }
                break;
                
            case TIEMPO_ASALTO_ACABADO:
                info.actualizarAsalto(cod_emparejamiento, Integer.parseInt(datos[1]));
                info.avisarCombate(codigoPartidaActual);
                break;
           
        }
        
        
        return respuesta;
    }
    
    public String tratarInterrupcion(String mensaje){
        
       
        String respuesta="";
        String datos[]=mensaje.split("@");
        int cod= Integer.parseInt(datos[0]);
        switch(Codigos_Servidor.interrupcion_servidor(cod)){
            case ACCION_MANDO:
                if(combateCurso){
                    /*String accion=devolverAccionMando(Integer.parseInt(datos[1]));
                    info.addEventoMandoAJugadores(codigoPartidaActual,cod_emparejamiento,accion);*/
                    respuesta="13&"+info.getEventoMandos(codigoPartidaActual, cod_emparejamiento);
                    
                }else{
                    respuesta="10&"+devolverAccionMando(Integer.parseInt(datos[1]));
                }
                
                
                break;
                
            case INDICE_COMBATE_ACTUAL:
                
                codigoPartidaActual=Integer.parseInt(datos[1]);
                respuesta="6&"+datos[2]+"&"+datos[3]+"&"+datos[4]+"&"+datos[5];
                
                break;
                
                
            case COMBATE_EMPEZADO:
                combateCurso=true;
                codigoPartidaActual=Integer.parseInt(datos[1]);
                break;
                
                
            case ACCION_MANDO_CONTRINCANTE:
                respuesta="13&"+info.getEventoMandos(codigoPartidaActual, cod_emparejamiento);
                break;
            case ASALTO_TERMINADO:
                respuesta="11";                
                break;
                
            case COMBATE_TERMINADO:
                listening=false;
                respuesta="12&"+datos[1];
                break;
                
            case TERMINAR_HILO:
                listening = false;
                if(rolHilo.equals("movil")){
                    respuesta = "0&1";
                }
                
                break;
            case HACER_VIBRAR:
                respuesta = "1";
                break;
        }
        return respuesta;
    }
    
    public String devolverAccionMando(int cod){
        String respuesta="";
        
        switch(Codigos_Servidor.acciones_servidor(cod)){
            case PIVOTANDO:
                respuesta="PIVOTANDO";
                break;
                
            case BLOQUEO:
                respuesta="BLOQUEO";
                break;
                
            case DIRECTO:
                respuesta="DIRECTO";
                break;
                
            case GANCHO_IZQUIERDA:
                respuesta="GANCHO_IZQUIERDA";
                break;
                
            case GANCHO_DERECHA:
                respuesta="GANCHO_DERECHA";
                break;
                
            case ESQUIVAR_IZQUIERDA:
                respuesta="ESQUIVAR_IZQUIERDA";
                break;
                
            case ESQUIVAR_DERECHA:
                respuesta="ESQUIVAR_DERECHA";
                break;
        }
        
        return respuesta;
    }
    
    public void avisarVibrarMando(){
        info.addMensajeParaHiloMoviles(cod_emparejamiento, "7");
        info.avisarLeerDatoAHiloMovil(cod_emparejamiento);
    }
}
