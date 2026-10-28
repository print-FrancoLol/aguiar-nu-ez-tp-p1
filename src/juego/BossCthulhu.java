package juego;

import entorno.Entorno;
import entorno.Herramientas;
import java.awt.Image;


public class BossCthulhu {
    private double x;
    private double y;
    private int vidas;
    private Image imagen;
    

    public BossCthulhu(double x, double y) {
        this.x = x;
        this.y = y;
        this.vidas = 20; 
        this.imagen = Herramientas.cargarImagen("imagenes/cthulhu.png");
        
    }


    public void moverHacia(double targetX, double targetY) {
        if (this.x < targetX) this.x += 0.5;
        if (this.x > targetX) this.x -= 0.5;
        if (this.y < targetY) this.y += 0.5;
        if (this.y > targetY) this.y -= 0.5;
    }

    public void dibujar(Entorno entorno) {
       
        
        
        if (this.imagen != null) {
          
            entorno.dibujarImagen(this.imagen, this.x, this.y, 0, 0.15);
        } 
    }

    public boolean chocaConRepartidor(Repartidor r) {

        return Math.abs(this.x - r.getX()) < 75 && Math.abs(this.y - r.getY()) < 75;
    }

    public boolean chocaConDisparo(Disparo d) {
        return Math.abs(this.x - d.getX()) < 75 && Math.abs(this.y - d.getY()) < 75;
    }

    public void restarVida() {
        this.vidas--;
    }

    public int getVidas() {
        return this.vidas;
    }
}