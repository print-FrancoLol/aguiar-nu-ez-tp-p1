
package juego;
import java.awt.Image;
import java.awt.Color;
import entorno.Entorno;
import entorno.Herramientas;

public class Manzana {

    private double x;
    private double y;
    private double ancho;
    private double alto;
    private Image manzana;
    

    private Casa[] casas;
    private Mercado mercado;

    public Manzana(double x, double y, double ancho, double alto) {

        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.manzana = Herramientas.cargarImagen("imagenes/manzana.png");

        this.casas = new Casa[3];

        int sectorNegocio = (int) (Math.random() * 4);

        int indiceCasa = 0;

      
        for (int sector = 0; sector < 4; sector++) {

           
            double edificioX = x - ancho / 4.0
                    + (sector % 2) * (ancho / 2.0);

           
            double edificioY = y - alto / 4.0
                    + (sector / 2) * (alto / 2.0);

           
            if (sector == sectorNegocio) {

                this.mercado = new Mercado(
                    edificioX, edificioY
                );

            } else {

              
                casas[indiceCasa] = new Casa(
                    edificioX, edificioY
                );

                indiceCasa++;
            }
        }
    }

    public void dibujar(Entorno entorno) {
    	
    	entorno.dibujarImagen(manzana, x, y, 0, 0.6);
         

        
        for (int i = 0; i < casas.length; i++) {
            casas[i].dibujar(entorno);
        }

        mercado.dibujar(entorno);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAncho() {
        return ancho;
    }

    public double getAlto() {
        return alto;
    }
    public Casa[] getCasas() {
        return casas;
    }

    public Mercado getMercado() {
        return mercado;
    }
}
