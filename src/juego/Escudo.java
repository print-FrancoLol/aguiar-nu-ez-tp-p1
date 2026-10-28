package juego;

import entorno.Entorno;
import entorno.Herramientas;
import java.awt.Image;
import java.awt.Color;

public class Escudo {
    private double x;
    private double y;
    private Image imagen;
    private int animacionFlote;

    public Escudo(double x, double y) {
        this.x = x;
        this.y = y;
        this.imagen = Herramientas.cargarImagen("imagenes/escudo.png");
        this.animacionFlote = 0;
    }

    public void dibujar(Entorno entorno) {
        animacionFlote++;
        double offsetY = Math.sin(animacionFlote * 0.1) * 3;
        if (this.imagen != null) {
            entorno.dibujarImagen(this.imagen, this.x, this.y + offsetY, 0, 0.02); 
        } else {
            entorno.dibujarCirculo(this.x, this.y, 25, Color.CYAN);
        }
    }
    public boolean chocaConRepartidor(Repartidor r) {
        return Math.abs(this.x - r.getX()) < 30 && Math.abs(this.y - r.getY()) < 30;
    }
}