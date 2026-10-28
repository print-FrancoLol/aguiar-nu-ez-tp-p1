
package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Casa {

    private double x;
    private double y;
    private Image imagen;

    private static Image[] imagenesCasas = {
        Herramientas.cargarImagen("imagenes/Casa1(nueva).png"),
        Herramientas.cargarImagen("imagenes/Casa2(nueva).png"),
        Herramientas.cargarImagen("imagenes/Casa3(nueva).png"),
        Herramientas.cargarImagen("imagenes/Casa4(nueva).png"),
        Herramientas.cargarImagen("imagenes/Casa5(nueva).png"),
        Herramientas.cargarImagen("imagenes/Casa6(nueva).png"),};

    public Casa(double x, double y) {
        this.x = x;
        this.y = y;


        int indice = (int) (Math.random() * imagenesCasas.length);
        this.imagen = imagenesCasas[indice];
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarImagen(imagen, x, y, 0, 0.1	);
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
