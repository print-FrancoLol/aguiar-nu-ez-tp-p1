package juego;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Enemigo {
     private int x;
     private int y;
     private int velocidad;
     private int dirX;
     private int dirY;
     private int vidas;
     private int ancho;
     private int alto;
     private Image imagen;
     private int variante;
     private Image[][] sprites = new Image[3][4];
    
     public Enemigo(int x, int y, int dirX, int dirY) {
    	    this.x = x;
    	    this.y = y;
    	    this.ancho = 20;
    	    this.alto = 20;
    	    this.velocidad = 2;
    	    this.dirX = dirX;
    	    this.dirY = dirY;
    	    this.vidas = 1;
    	    
    	 
    	    sprites[0][0] = Herramientas.cargarImagen("Imagenes/Enemigo1(Arriba).png");
    	    sprites[0][1] = Herramientas.cargarImagen("Imagenes/Enemigo1(Abajo).png");
    	    sprites[0][2] = Herramientas.cargarImagen("Imagenes/Enemigo1(Izquierda).png");
    	    sprites[0][3] = Herramientas.cargarImagen("Imagenes/Enemigo1(Derecha).png");

    	
    	    sprites[1][0] = Herramientas.cargarImagen("Imagenes/Enemigo2(Arriba).png");
    	    sprites[1][1] = Herramientas.cargarImagen("Imagenes/Enemigo2(Abajo).png");
    	    sprites[1][2] = Herramientas.cargarImagen("Imagenes/Enemigo2(Izquierda).png");
    	    sprites[1][3] = Herramientas.cargarImagen("Imagenes/Enemigo2(Derecha).png");

    	
    	    sprites[2][0] = Herramientas.cargarImagen("Imagenes/Enemigo3(Arriba).png");
    	    sprites[2][1] = Herramientas.cargarImagen("Imagenes/Enemigo3(Abajo).png");
    	    sprites[2][2] = Herramientas.cargarImagen("Imagenes/Enemigo3(Izquierda).png");
    	    sprites[2][3] = Herramientas.cargarImagen("Imagenes/Enemigo3(Derecha).png");
    	    
    	    
    	    
    	    this.variante = (int) (Math.random() * 3);

    	    actualizarImagen();
    	    }
      
     private void actualizarImagen() {
    	    int direccion;

    	    if (this.dirX == 0 && this.dirY == -1) {
    	        direccion = 0; 
    	    } else if (this.dirX == 0 && this.dirY == 1) {
    	        direccion = 1;
    	    } else if (this.dirX == -1 && this.dirY == 0) {
    	        direccion = 2; 
    	    } else {
    	        direccion = 3; 
    	    }

    	    this.imagen = sprites[this.variante][direccion];
    	}
    public void dibujar(Entorno entorno) {
    	    if (this.imagen != null) {
    	        entorno.dibujarImagen(
    	            this.imagen,
    	            this.x,
    	            this.y,
    	            0,
    	            0.09
    	        );
    	    } else {
    	        entorno.dibujarRectangulo(
    	            this.x,
    	            this.y,
    	            this.ancho,
    	            this.alto,
    	            0,
    	            Color.RED
    	        );
    	    }
    	}

    public boolean chocaConDisparo(Disparo disparo) {
        int dX = disparo.getX();
        int dY = disparo.getY();
        int dAncho = disparo.getAncho(); 
        int dAlto = disparo.getAlto();

        int izq = dX - (dAncho / 2) - (this.ancho / 2);
        int der = dX + (dAncho / 2) + (this.ancho / 2);
        int arriba = dY - (dAlto / 2) - (this.alto / 2);
        int abajo = dY + (dAlto / 2) + (this.alto / 2);

        return (this.x > izq && this.x < der && this.y > arriba && this.y < abajo);
    }
    public boolean chocaConRepartidor(Repartidor repartidor) {
        int rX = repartidor.getX();
        int rY = repartidor.getY();
        
  
        int rAncho = 20;
        int rAlto = 20;

        int izq = rX - (rAncho / 2) - (this.ancho / 2);
        int der = rX + (rAncho / 2) + (this.ancho / 2);
        int arriba = rY - (rAlto / 2) - (this.alto / 2);
        int abajo = rY + (rAlto / 2) + (this.alto / 2);

        return (this.x > izq && this.x < der && this.y > arriba && this.y < abajo);
    }    
    
    public void restarVida() {
        this.vidas = this.vidas - 1;
    }
    public void mover() {
        this.x = this.x + (this.velocidad * this.dirX);
        this.y = this.y + (this.velocidad * this.dirY);

 
        if (this.x >= 790) {
            this.dirX = -1;
        }
        if (this.x <= 10) {
            this.dirX = 1;
        }

    
        if (this.y >= 590) {
            this.dirY = -1;
        }
        if (this.y <= 80) {
            this.dirY = 1;
        }
        actualizarImagen();    
        

       
        
    }
    public int getVidas() {return this.vidas;}
    public int getX() { return this.x;}
    public int getY() { return this.y;}
    public int getDirX() {return this.dirX;}
    public int getDirY() {return this.dirY;}
}    