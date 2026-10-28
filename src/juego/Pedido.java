package juego;

import java.awt.Image;
import entorno.Herramientas;
import entorno.Entorno;

public class Pedido {

    private Casa casaDestino;
    private Mercado mercado;
    private int estado;
    private Image flechaRoja;
    private Image flechaVerde;
    private int contadorTicksLocal;
    private int ticksInicio;
    private final int TIEMPO_LIMITE = 1500;

    private final int REALIZADO = 0;
    private final int ESPERANDO = 1;
    private final int EN_CAMINO = 2;
    private final int ENTREGADO = 3;
    private final int PERDIDO = 4;

    public Pedido(Casa casaDestino, Mercado mercado, int ticksActuales) {
        this.casaDestino = casaDestino;
        this.mercado = mercado;
        this.estado = REALIZADO;
        this.ticksInicio = ticksActuales;
        
  
        this.flechaRoja = Herramientas.cargarImagen("imagenes/flechita.png");
        this.flechaVerde = Herramientas.cargarImagen("imagenes/flechaV.png");
        this.contadorTicksLocal = ticksActuales;
    }

    public void actualizar(int ticksActuales) {
  
        this.contadorTicksLocal = ticksActuales;

        if (estado == REALIZADO) {
            estado = ESPERANDO;
        }

        if (estado == ESPERANDO || estado == EN_CAMINO) {
            if (ticksActuales - ticksInicio >= TIEMPO_LIMITE) {
                estado = PERDIDO;
            }
        }
    }

    public void dibujar(Entorno entorno) {
   
        double oscilacionY = Math.sin(this.contadorTicksLocal * 0.1) * 5;


        if (estado == ESPERANDO) {
            if (this.flechaRoja != null) {
                entorno.dibujarImagen(this.flechaRoja, mercado.getX(), mercado.getY() + 14 + oscilacionY, 0 , 0.04);
            }
        } 
 
        else if (estado == EN_CAMINO) {
            if (this.flechaVerde != null) {
                entorno.dibujarImagen(this.flechaVerde, casaDestino.getX(), casaDestino.getY() + 14 + oscilacionY, 0, 0.04);
            }
        }
    }

    public boolean estaEsperando() {
        return estado == ESPERANDO;
    }

    public boolean estaEnCamino() {
        return estado == EN_CAMINO;
    }

    public boolean estaTerminado() {
        return estado == ENTREGADO || estado == PERDIDO;
    }
    
    public boolean sePerdio() {
        return estado == PERDIDO;
    }

    public int getEstado() {
        return estado;
    }

    public void recoger() {
        if (estado == ESPERANDO) {
            estado = EN_CAMINO;
        }
    }

    public void entregar() {
        if (estado == EN_CAMINO) {
            estado = ENTREGADO;
        }
    }

    public Casa getCasaDestino() {
        return casaDestino;
    }

    public Mercado getMercado() {
        return mercado;
    }
}