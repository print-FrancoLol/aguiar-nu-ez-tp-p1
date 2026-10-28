package juego;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Repartidor {
    private int x;
    private int y;
    private int velocidad;
    private int dirX;
    private int dirY;
    private int vidas;
    private int ancho;
    private int alto;
    private Image imagenArriba;
    private Image imagenAbajo;
    private Image imagenIzquierda;
    private Image imagenDerecha;
    private Image imagen;

    public Repartidor(int x, int y) {
        this.x = x;
        this.y = y;
        this.ancho=20;
        this.alto=20;
        this.velocidad = 2;
        this.dirX = 0;
        this.dirY = -1; 
        this.vidas = 3;
     
        this.imagenArriba = Herramientas.cargarImagen(
            "Imagenes/RepartidorArriba.png"
        );

        this.imagenAbajo = Herramientas.cargarImagen(
            "Imagenes/RepartidorAbajo.png"
        );

        this.imagenIzquierda = Herramientas.cargarImagen(
            "Imagenes/RepartidorIzquierda.png"
        );

        this.imagenDerecha = Herramientas.cargarImagen(
            "Imagenes/RepartidorDerecha.png"
        );

        
        this.imagen = this.imagenArriba;
        
        
    }

    public void mover(Entorno entorno, Manzana[] manzanas) {
        
        int anteriorX = this.x;
        int anteriorY = this.y;

        // Movimiento 
        if (entorno.estaPresionada('a') || entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
            this.x = this.x - velocidad;
            this.dirX = -1;
            this.dirY = 0;
        } 
         if (entorno.estaPresionada('d') || entorno.estaPresionada(entorno.TECLA_DERECHA)) {
            this.x = this.x + velocidad;
            this.dirX = 1;
            this.dirY = 0;
        } 
   
        if (this.x < 10 || this.x > 790 || chocaConAlgunaManzana(manzanas)) {
            this.x = anteriorX;
        }
         if (entorno.estaPresionada('w') || entorno.estaPresionada(entorno.TECLA_ARRIBA)) {
            this.y = this.y - velocidad;
            this.dirX = 0;
            this.dirY = -1;
        } 
         if (entorno.estaPresionada('s') || entorno.estaPresionada(entorno.TECLA_ABAJO)) {
            this.y = this.y + velocidad;
            this.dirX = 0;
            this.dirY = 1;
        }
         
      
         if (this.y < 80 || this.y > 590 || chocaConAlgunaManzana(manzanas)) {
             this.y = anteriorY; 
             
         }
         actualizarImagen();
    }

    private boolean chocaConAlgunaManzana(Manzana[] manzanas) {
        for (int i = 0; i < manzanas.length; i++) {
            if (manzanas[i] != null) {
               
                int mX = (int) manzanas[i].getX();
                int mY = (int) manzanas[i].getY();
                int anchoM = (int) manzanas[i].getAncho();
                int altoM = (int) manzanas[i].getAlto();

               
                int izq = mX - (anchoM / 2) - 10;
                int der = mX + (anchoM / 2) + 10;
                int arriba = mY - (altoM / 2) - 10;
                int abajo = mY + (altoM / 2) + 10;

               
                if (this.x > izq && this.x < der && this.y > arriba && this.y < abajo) {
                    return true;
                }
            }
        }
        return false;
    }

    public void dibujar(Entorno entorno) {
        if (this.imagen != null) {
            entorno.dibujarImagen(this.imagen, this.x, this.y, 0, 0.05);
        } else {
           
            entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto,0,  Color.BLUE);
        }
    }
    private void actualizarImagen() {
        if (this.dirX == 0 && this.dirY == -1) {
            this.imagen = this.imagenArriba;
        } else if (this.dirX == 0 && this.dirY == 1) {
            this.imagen = this.imagenAbajo;
        } else if (this.dirX == -1 && this.dirY == 0) {
            this.imagen = this.imagenIzquierda;
        } else if (this.dirX == 1 && this.dirY == 0) {
            this.imagen = this.imagenDerecha;
        }
    }
    public void restarVida() {
        if (this.vidas > 0) {
            this.vidas--;
        }
    }

    public void respawn() {
        this.x = 385;
        this.y = 590;

        
        this.dirX = 0;
        this.dirY = -1;
    }
    public void reiniciar() {
        this.vidas = 3;
        respawn();
    }

    // Getters
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public int getDirX() { return this.dirX; }
    public int getDirY() { return this.dirY; }
    public int getVidas() { return this.vidas; }
}
