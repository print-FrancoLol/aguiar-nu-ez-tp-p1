package juego;


import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class BombasEnemigos {
    private int x;
    private int y;
    private Image FuegoVertical;
    private Image FuegoHorizontal;
    private Image explosivo;


    private int tiempoDeVida; 
    private int tiempoExplosion; 
    private boolean explotando;
    private boolean yaDanoAlRepartidor = false;
    private boolean horizontal;
    private boolean cruz;

    public BombasEnemigos(int x, int y, boolean horizontal) {
        this.x = x;
        this.y = y;
        this.horizontal = horizontal;
        this.cruz = estaEnInterseccion();
        this.tiempoDeVida = 100;
        this.tiempoExplosion = 20;
        this.explotando = false;
        this.explosivo = Herramientas.cargarImagen("Imagenes/Explosivo.png");
        this.FuegoVertical = Herramientas.cargarImagen("Imagenes/FuegoVertical.png");
        this.FuegoHorizontal = Herramientas.cargarImagen("Imagenes/FuegoHorizontal.png");
    }   

    public void tick() {
        if (this.tiempoDeVida > 0) {
            this.tiempoDeVida--; 
        } else if (this.tiempoDeVida == 0 && !this.explotando) {
            this.explotando = true; 
        } else if (this.explotando && this.tiempoExplosion > 0) {
            this.tiempoExplosion--; }
    }


public void dibujar(Entorno entorno) {
	if (!this.explotando) {

	    double escala = 0.05;

	    if (this.tiempoDeVida <= 30) {
	        if ((this.tiempoDeVida / 5) % 2 == 0) {
	            escala = 0.06;}
	    }
      entorno.dibujarImagen(
	        this.explosivo,
	        this.x,
	        this.y,
	        0,
	        escala);

	} 
	else if (this.tiempoExplosion > 0) {
        if (this.cruz){

	        entorno.dibujarImagen(
	            this.FuegoHorizontal,
	            this.x, this.y,
	            0, 0.5);

	        entorno.dibujarImagen(
	            this.FuegoVertical,
	            this.x, this.y,
	            0, 0.5);
   } 
	    else if (this.horizontal){

	        entorno.dibujarImagen(
	            this.FuegoHorizontal,
	            this.x, this.y,
	            0, 0.5);
} 
	    else{

	        // Calle vertical.
	        entorno.dibujarImagen(
	            this.FuegoVertical,
	            this.x, this.y,
	            0, 0.5);
	    }
	}
}
    public boolean terminoLaExplosion() {
        return this.explotando && this.tiempoExplosion == 0;}

    public boolean chocaConRepartidor(Repartidor repartidor) {

        if (!this.explotando || this.tiempoExplosion <= 0
                || this.yaDanoAlRepartidor) {
            return false;
        }

        int rX = repartidor.getX();
        int rY = repartidor.getY();

        int rAncho = 20;
        int rAlto = 20;

        int rangoExplosion = 140;

        boolean chocaHorizontal = false;
        boolean chocaVertical = false;

        if (this.cruz || this.horizontal) {
            chocaHorizontal =
                Math.abs(rX - this.x)
                    < (rAncho + rangoExplosion) / 2.0
                && Math.abs(rY - this.y)
                    < (rAlto + 40) / 2.0;
        }

        if (this.cruz || !this.horizontal) {
            chocaVertical =
                Math.abs(rX - this.x)
                    < (rAncho + 50) / 2.0
                && Math.abs(rY - this.y)
                    < (rAlto + rangoExplosion) / 2.0;
        }

        if (chocaHorizontal || chocaVertical) {
            this.yaDanoAlRepartidor = true;
            return true;
        }

        return false;
    }
private boolean estaEnInterseccion() {
    return estaCercaDeCalleVertical()
        && estaCercaDeCalleHorizontal();
}

private boolean estaCercaDeCalleVertical() {
    return Math.abs(this.x - 210) <= 25
        || Math.abs(this.x - 400) <= 25
        || Math.abs(this.x - 590) <= 25;
}

private boolean estaCercaDeCalleHorizontal() {
    return Math.abs(this.y - 205) <= 25
        || Math.abs(this.y - 335) <= 25
        || Math.abs(this.y - 465) <= 25;
}


    public int getX() { return x; }
    public int getY() { return y; }
    
}
   

