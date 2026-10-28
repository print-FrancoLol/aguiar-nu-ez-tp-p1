package juego;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Disparo {
    private int x;
    private int y;
    private int velocidad;
    private int dirX;
    private int dirY;
    private int ancho;
    private int alto;
    private Image imagen;
    private Image imagenArriba;
    private Image imagenAbajo;
    private Image imagenIzquierda;
    private Image imagenDerecha;
    

    
    public Disparo(int x, int y, int dirX, int dirY) {
        this.x = x;
        this.y = y;
        this.dirX = dirX;
        this.dirY = dirY;
        this.ancho = 15;
        this.alto = 15;
        this.velocidad = 5;
        this.imagenArriba = Herramientas.cargarImagen(
    	        "Imagenes/DisparoArriba.png"
    	    );
    	    this.imagenAbajo = Herramientas.cargarImagen(
    	    		"Imagenes/DisparoAbajo.png"
    	    );
    	    this.imagenIzquierda = Herramientas.cargarImagen(
    	    		"Imagenes/DisparoIzquierda.png"
    	    );
    	    this.imagenDerecha = Herramientas.cargarImagen(
    	    		"Imagenes/DisparoDerecha.png"
    	    );

    	    actualizarImagen();

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
       
    

    public void mover() {
        this.x = this.x + (this.velocidad * this.dirX);
        this.y = this.y + (this.velocidad * this.dirY);
        actualizarImagen();
    }

    public void dibujar(Entorno entorno) {
        if (this.imagen != null) {
            entorno.dibujarImagen(this.imagen, this.x, this.y, 0, 0.05);
        } else {
            entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.YELLOW);
        }
    }

    public boolean salioDePantalla() {
        if (this.x < 0 || this.x > 800 || this.y < 0 || this.y > 600) {
            return true;
        }
        return false;
    }public boolean chocaConAlgunaManzana(Manzana[] manzanas) {
        for (int i = 0; i < manzanas.length; i++) {
            if (manzanas[i] != null) {
                int mX = (int) manzanas[i].getX();
                int mY = (int) manzanas[i].getY();
                int anchoM = (int) manzanas[i].getAncho();
                int altoM = (int) manzanas[i].getAlto();

                
                int izq = mX - (anchoM / 2) - (this.ancho / 2);
                int der = mX + (anchoM / 2) + (this.ancho / 2);
                int arriba = mY - (altoM / 2) - (this.alto / 2);
                int abajo = mY + (altoM / 2) + (this.alto / 2);

                if (this.x > izq && this.x < der && this.y > arriba && this.y < abajo) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getAncho() { return ancho; }
    public int getAlto() { return alto; } 


	

}
