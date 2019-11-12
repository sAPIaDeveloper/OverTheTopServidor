/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConexionesTCP;

import java.util.Stack;

/**
 *
 * @author josea
 */
public class Player {
    private String codEmparejamiento;
    private String nombre_boxeador;
    private String tipo_boxeador;
    
    private Stack accionesBoxeador;
    private Stack accionesContrincante;
    private int golpesLanzados;
    private int golpesConectados;
    private int salud;
    private int asalto;

    public Player(String codEmparejamiento,String tipo_boxeador,String nombre_boxeador) {
        this.codEmparejamiento=codEmparejamiento;
        this.tipo_boxeador=tipo_boxeador;
        this.nombre_boxeador=nombre_boxeador;
        accionesBoxeador=new Stack();
        accionesContrincante= new Stack();
        salud=100;
        golpesLanzados=0;
        golpesConectados=0;
    }
    
    public synchronized void addAccionBoxeador(String estado){
        accionesBoxeador.push(estado);
    }
    
    public synchronized void addAccionContrincante(String estado){
        accionesContrincante.push(estado);
    }
    
    public synchronized String getAccionBoxeador(){
        if(accionesBoxeador.size()>0){
            return (String) accionesBoxeador.pop();
        }
        
        return "PIVOTANDO";
        
    }
    
    public synchronized String getAccionContrincante(){
        if(accionesContrincante.size()>0){
            return (String) accionesContrincante.pop();
        }
        
        return "PIVOTANDO";
        
    }
        
    
    public synchronized int getSalud(){
        return salud;
    }
    
   
    public void setGolpesLanzados(int golpesLanzados) {
        this.golpesLanzados = golpesLanzados;
    }

    public void setGolpesConectados(int golpesConectados) {
        this.golpesConectados = golpesConectados;
    }

    public void setSalud(int salud) {
        this.salud = salud;
    }
       

    public String getCodEmparejamiento() {
        return codEmparejamiento;
    }

    public String getTipo_boxeador() {
        return tipo_boxeador;
    }

    public String getNombre_boxeador() {
        return nombre_boxeador;
    }

    public Stack getAccionesBoxeador() {
        return accionesBoxeador;
    }

    public Stack getAccionesContrincante() {
        return accionesContrincante;
    }

    public int getGolpesLanzados() {
        return golpesLanzados;
    }

    public int getGolpesConectados() {
        return golpesConectados;
    }

    public int getAsalto() {
        return asalto;
    }

    public void setAsalto(int asalto) {
        this.asalto = asalto;
    }
    
    
    
    public void limpiarAcciones(){
        
       accionesBoxeador.removeAllElements();
        accionesContrincante.removeAllElements();
    }
    
    
}
