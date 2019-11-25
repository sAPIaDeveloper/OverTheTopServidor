package ClasesComunes;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author josea
 */
public class Codigos_Servidor {
    
    public enum Codigo{
        REGISTRAR_USUARIO,
        INICIAR_SESION,
        REGISTRAR_BOXEADOR,
        OBTENER_COMPETICIONES_APUNTADAS_BOXEADOR,
        COMPROBAR_EMPAREJAMIENTO_MOVIL_MAS_DEVOLVER_COD,
        EMPAREJAR_MOVIL_ORDENADOR, // Codigo para lo que recibe desde el movil
        COMPROBAR_EMPAREJAMIENTO,
        BUSCAR_OPONENTE,
        CREAR_COMPETICION,
        MODIFICAR_BOXEADOR,
        BORRAR_BOXEADOR,
        ACCION_BOXEADOR,
        PARTIDA_EMPEZADA,
        OBTENER_ESTADISTICAS_BOXEADOR,
        ACTUALIZACION_DATOS_BOXEADOR_COMBATE,
        OBTENER_COMPETICIONES_CREADAS,
        APUNTAR_BOXEADOR_COMPETICION,
        SIGO_ACTIVO,
        TIEMPO_ASALTO_ACABADO,
        COMBATE_TERMINADO,
        OBTENER_INFORMACION_COMPETICIONES_APUNTADAS,
        JUGADOR_NO_ENCONTRADO
    }
    
    public enum Interrupcion{
        ACCION_MANDO,
        INDICE_COMBATE_ACTUAL,
        COMBATE_EMPEZADO,
        ACCION_MANDO_CONTRINCANTE,
        ASALTO_TERMINADO,
        COMBATE_TERMINADO,
        TERMINAR_HILO,
        HACER_VIBRAR
    }
    
    public enum Acciones{
        PIVOTANDO,
        BLOQUEO,
        DIRECTO,
        GANCHO_IZQUIERDA,
        GANCHO_DERECHA,
        ESQUIVAR_IZQUIERDA,
        ESQUIVAR_DERECHA
    }
    
    
    
    public static Codigo codigo_servidor(int num){
        Codigo[] cod = Codigo.values();
        return cod[num];
    }
    
    public static Interrupcion interrupcion_servidor(int num){
        Interrupcion[] cod = Interrupcion.values();
        return cod[num];
    }
    
    public static Acciones acciones_servidor(int num){        
        Acciones[] cod = Acciones.values();
        return cod[num];
    }
}
