
package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Mercado {

    private double x;
    private double y;
    private Image imagen;

    private static Image[] imagenesMercados = {
        Herramientas.cargarImagen("imagenes/mercado1.png"),
        Herramientas.cargarImagen("imagenes/mercado2.png"),
        Herramientas.cargarImagen("imagenes/mercado3.png"),	
    };

    public Mercado(double x, double y) {
        this.x = x;
        this.y = y;

        int indice = (int) (Math.random() * imagenesMercados.length);
        this.imagen = imagenesMercados[indice];
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarImagen(imagen, x, y, 0, 0.11);
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
