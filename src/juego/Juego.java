package juego;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;
import java.awt.Image;
import java.awt.Color; 

public class Juego extends InterfaceJuego {

    private Entorno entorno;
    private Manzana[] manzanas;
    private Repartidor repartidor;
    private Enemigo[] enemigos;
    private Disparo disparo;
    private BombasEnemigos bombasEnemigos;
    private Pedido pedidoActual;
    private Escudo escudoItem; 

    // --- EASTER EGG BOSS ---
    private BossCthulhu cthulhu;
    private int estadoEasterEgg = 0; 
    private boolean murioPorCthulhu = false; 
    private boolean vencioACthulhu = false; 

    
    private Image hud;
    private Image fondoMenu;
    private Image fondoGameOver; 
    private Image fondoGameOverCthulhu; 
    private Image fondoVictoria; 
    private Image fondoVictoriaCthulhu; 
    private Image logo;
    
    
    private final int ESTADO_MENU = 0;
    private final int ESTADO_JUGANDO = 1;
    private final int ESTADO_GAMEOVER = 2; 
    private final int ESTADO_VICTORIA = 3; 
    private int estadoJuego = ESTADO_MENU;
    
    private int opcionSeleccionada = 0; 
    private int opcionGameOver = 0;     
    private int opcionVictoria = 0;     
    
    private int contadorTicks = 0;
    private int tiempoMinutos = 0;
    private int tiempoSegundos = 0;
    private int pedidosEntregados = 0;
    private String pedidosEnReparto = "Sin pedido";
    private int pedidosPerdidos = 0;
    private int contadorBajas = 0;
    private int[] contadorRespawn = new int[6];
    private int ticksInvulnerabilidad = 0;
    
    private final int[][] spawnsVerticales = {
        {210, 80},
        {400, 80},
        {590, 80}
    };

    private final int[][] spawnsHorizontales = {
        {45, 205},
        {45, 335},
        {45, 465}
    };

    Juego() {
        
        hud = Herramientas.cargarImagen("imagenes/HUD.png");
        fondoMenu = Herramientas.cargarImagen("imagenes/fondoMenu.png");
        fondoGameOver = Herramientas.cargarImagen("imagenes/gameOver.png"); 
        fondoGameOverCthulhu = Herramientas.cargarImagen("imagenes/gameOverCthulhu.png"); 
        fondoVictoria = Herramientas.cargarImagen("imagenes/fondoVictoria.png"); 
        fondoVictoriaCthulhu = Herramientas.cargarImagen("imagenes/victoriaCthulhu.png"); // <--- Cargamos la imagen de victoria del Boss
        logo = Herramientas.cargarImagen("imagenes/logo.png");

        entorno = new Entorno(this, "Proyecto para TP", 800, 600);

        manzanas = new Manzana[16];
        crearMapa();

        repartidor = new Repartidor(385, 590);

        crearEnemigosIniciales();

        entorno.iniciar();
    }
    
    private void crearMapa() {
        int filas = 4;
        int columnas = 4;
        int anchoManzana = 140;
        int altoManzana = 90;
        int separacionX = 50;
        int separacionY = 40;
        int margenInicialX = 115;
        int margenInicialY = 140;

        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                int x = margenInicialX + col * (anchoManzana + separacionX);
                int y = margenInicialY + fila * (altoManzana + separacionY);
                manzanas[fila * columnas + col] = new Manzana(x, y, anchoManzana, altoManzana);
            }
        }
    }

    private void crearEnemigosIniciales() {
        enemigos = new Enemigo[6];
        contadorRespawn = new int[6];
        for (int i = 0; i < 3; i++) {
            enemigos[i] = new Enemigo(spawnsVerticales[i][0], spawnsVerticales[i][1], 0, 1);
        }
        for (int i = 0; i < 3; i++) {
            enemigos[i + 3] = new Enemigo(spawnsHorizontales[i][0], spawnsHorizontales[i][1], 1, 0);
        }
    }

    private Enemigo crearEnemigoVertical() {
        int indice = (int) (Math.random() * spawnsVerticales.length);
        return new Enemigo(spawnsVerticales[indice][0], spawnsVerticales[indice][1], 0, 1);
    }

    private Enemigo crearEnemigoHorizontal() {
        int indice = (int) (Math.random() * spawnsHorizontales.length);
        return new Enemigo(spawnsHorizontales[indice][0], spawnsHorizontales[indice][1], 1, 0);
    }

    private Casa elegirCasaAleatoria() {
        int indiceManzana = (int) (Math.random() * manzanas.length);
        Manzana manzana = manzanas[indiceManzana];
        int indiceCasa = (int) (Math.random() * manzana.getCasas().length);
        return manzana.getCasas()[indiceCasa];
    }

    private Mercado elegirMercadoAleatorio() {
        int indiceManzana = (int) (Math.random() * manzanas.length);
        return manzanas[indiceManzana].getMercado();
    }

    private boolean repartidorEn(double x, double y) {
        return Math.abs(repartidor.getX() - x) <= 45 && Math.abs(repartidor.getY() - y) <= 45;
    }

    private void reiniciarPartida() {
        estadoJuego = ESTADO_JUGANDO; 
        repartidor.reiniciar();
        disparo = null;
        bombasEnemigos = null;
        pedidoActual = null;
        escudoItem = null; 
        
        cthulhu = null;
        estadoEasterEgg = 0;
        murioPorCthulhu = false;
        vencioACthulhu = false; 

        pedidosEnReparto = "Sin pedido";
        contadorTicks = 0;
        tiempoMinutos = 0;
        tiempoSegundos = 0;
        pedidosEntregados = 0;
        pedidosPerdidos = 0;
        contadorBajas = 0;
        ticksInvulnerabilidad = 0;
        crearEnemigosIniciales();
    }

    private void procesarMenu() {
        double escalaZoom = 1.05 + Math.sin(contadorTicks * 0.02) * 0.02;
        if (this.fondoMenu != null) {
            entorno.dibujarImagen(this.fondoMenu, 400, 300, 0, escalaZoom);
        }
        entorno.dibujarRectangulo(400, 300, 800, 600, 0, new Color(0, 0, 0, 150));

        double offsetY = Math.sin(contadorTicks * 0.05) * 15; 
        if (this.logo != null) {
            entorno.dibujarImagen(this.logo, 400, 180 + offsetY, 0, 0.35); 
        } 

        if (entorno.sePresiono('w') || entorno.sePresiono('W') || entorno.sePresiono(entorno.TECLA_ARRIBA)) {
            opcionSeleccionada = 0;
        }
        if (entorno.sePresiono('s') || entorno.sePresiono('S') || entorno.sePresiono(entorno.TECLA_ABAJO)) {
            opcionSeleccionada = 1;
        }

        if (opcionSeleccionada == 0) {
            entorno.cambiarFont("Arial", 26, Color.YELLOW);
            entorno.escribirTexto("> INICIAR JUEGO <", 285, 380);
        } else {
            entorno.cambiarFont("Arial", 22, Color.WHITE);
            entorno.escribirTexto("  INICIAR JUEGO  ", 295, 380);
        }

        if (opcionSeleccionada == 1) {
            entorno.cambiarFont("Arial", 26, Color.YELLOW);
            entorno.escribirTexto("> SALIR DEL JUEGO <", 270, 440);
        } else {
            entorno.cambiarFont("Arial", 22, Color.WHITE);
            entorno.escribirTexto("  SALIR DEL JUEGO  ", 280, 440);
        }

        if (entorno.sePresiono(entorno.TECLA_ENTER)) {
            if (opcionSeleccionada == 0) {
                reiniciarPartida();
            } else {
                System.exit(0); 
            }
        }
    }


    private void procesarGameOver() {
        if (murioPorCthulhu && this.fondoGameOverCthulhu != null) {
            entorno.dibujarImagen(this.fondoGameOverCthulhu, 400, 300, 0, 0.57); 
        } else if (!murioPorCthulhu && this.fondoGameOver != null) {
            entorno.dibujarImagen(this.fondoGameOver, 400, 300, 0, 0.6); 
        } else {
            entorno.dibujarRectangulo(400, 300, 800, 600, 0, Color.BLACK);
            entorno.cambiarFont("Arial", 40, Color.RED);
            entorno.escribirTexto(murioPorCthulhu ? "CTHULHU TE DEVORÓ" : "HAS PERDIDO", 250, 250);
        }

        if (!murioPorCthulhu) {
            double offsetY = Math.sin(contadorTicks * 0.05) * 8; 
            if (this.logo != null) {
                entorno.dibujarImagen(this.logo, 140, 500 + offsetY, 0, 0.1); 
            }
        }

        if (entorno.sePresiono('w') || entorno.sePresiono('W') || entorno.sePresiono(entorno.TECLA_ARRIBA)) {
            opcionGameOver = 0;
        }
        if (entorno.sePresiono('s') || entorno.sePresiono('S') || entorno.sePresiono(entorno.TECLA_ABAJO)) {
            opcionGameOver = 1;
        }

        if (opcionGameOver == 0) {
            entorno.cambiarFont("Arial", 24, Color.YELLOW);
            entorno.escribirTexto("> REINICIAR <", 500, 480);
        } else {
            entorno.cambiarFont("Arial", 20, Color.WHITE);
            entorno.escribirTexto("  REINICIAR  ", 510, 480);
        }

        if (opcionGameOver == 1) {
            entorno.cambiarFont("Arial", 24, Color.YELLOW);
            entorno.escribirTexto("> CERRAR JUEGO <", 470, 530);
        } else {
            entorno.cambiarFont("Arial", 20, Color.WHITE);
            entorno.escribirTexto("  CERRAR JUEGO  ", 480, 530);
        }

        if (entorno.sePresiono(entorno.TECLA_ENTER)) {
            if (opcionGameOver == 0) {
                reiniciarPartida();
            } else {
                System.exit(0);
            }
        }
    }


    private void procesarVictoria() {
     
        if (vencioACthulhu && this.fondoVictoriaCthulhu != null) {
            entorno.dibujarImagen(this.fondoVictoriaCthulhu, 400, 300, 0, 0.57); 
        } else if (!vencioACthulhu && this.fondoVictoria != null) {
            entorno.dibujarImagen(this.fondoVictoria, 400, 300, 0, 0.57); 
        } else {
            entorno.dibujarRectangulo(400, 300, 800, 600, 0, Color.BLACK);
            entorno.cambiarFont("Arial", 40, Color.GREEN);
            entorno.escribirTexto(vencioACthulhu ? "¡CTHULHU DERROTADO!" : "¡VICTORIA!", 260, 250);
        }

        double offsetY = Math.sin(contadorTicks * 0.05) * 8; 
        if (this.logo != null) {
            entorno.dibujarImagen(this.logo, 140, 500 + offsetY, 0, 0.1); 
        }

        if (entorno.sePresiono('w') || entorno.sePresiono('W') || entorno.sePresiono(entorno.TECLA_ARRIBA)) {
            opcionVictoria = 0;
        }
        if (entorno.sePresiono('s') || entorno.sePresiono('S') || entorno.sePresiono(entorno.TECLA_ABAJO)) {
            opcionVictoria = 1;
        }

        if (opcionVictoria == 0) {
            entorno.cambiarFont("Arial", 24, Color.YELLOW);
            entorno.escribirTexto("> JUGAR DE NUEVO <", 470, 480);
        } else {
            entorno.cambiarFont("Arial", 20, Color.WHITE);
            entorno.escribirTexto("  JUGAR DE NUEVO  ", 480, 480);
        }

        if (opcionVictoria == 1) {
            entorno.cambiarFont("Arial", 24, Color.YELLOW);
            entorno.escribirTexto("> CERRAR JUEGO <", 490, 530);
        } else {
            entorno.cambiarFont("Arial", 20, Color.WHITE);
            entorno.escribirTexto("  CERRAR JUEGO  ", 500, 530);
        }

        if (entorno.sePresiono(entorno.TECLA_ENTER)) {
            if (opcionVictoria == 0) {
                reiniciarPartida();
            } else {
                System.exit(0);
            }
        }
    }

    public void tick() {
        contadorTicks++;

        if (estadoJuego == ESTADO_MENU) {
            procesarMenu();
            return;
        } else if (estadoJuego == ESTADO_GAMEOVER) {
            procesarGameOver();
            return;
        } else if (estadoJuego == ESTADO_VICTORIA) {
            procesarVictoria();
            return;
        }
        
      
        if (estadoEasterEgg == 0 && entorno.sePresiono('c')) {
            estadoEasterEgg = 1;
        } else if (estadoEasterEgg == 1 && entorno.sePresiono('t')) {
            estadoEasterEgg = 2;
        } else if (estadoEasterEgg == 2 && entorno.sePresiono('h')) {
            estadoEasterEgg = 3;
            cthulhu = new BossCthulhu(400, 100); 
            for (int i = 0; i < enemigos.length; i++) enemigos[i] = null;
            bombasEnemigos = null;
        }
        

        if (contadorTicks % 60 == 0) {
            tiempoSegundos++;
        }
        if (tiempoSegundos == 60) {
            tiempoMinutos ++;
            tiempoSegundos = 0;
        }

        
        if (this.hud != null) {
        	entorno.dibujarImagen(this.hud, 400, 22, 0, 0.81);
        }

        for (int i = 0; i < manzanas.length; i++) {
            if (manzanas[i] != null) {
                manzanas[i].dibujar(entorno);
            }
        }

        if (cthulhu != null) {
            entorno.dibujarRectangulo(400, 300, 800, 600, 0, new Color(255, 0, 0, 80));
        }

        if (ticksInvulnerabilidad > 0) {
            ticksInvulnerabilidad--;
            entorno.dibujarCirculo(repartidor.getX(), repartidor.getY(), 45, new Color(0, 255, 255, 120));
        }

        repartidor.mover(entorno, manzanas);
        repartidor.dibujar(entorno);

        if (escudoItem != null) {
            escudoItem.dibujar(entorno);
            
            if (escudoItem.chocaConRepartidor(repartidor)) {
                ticksInvulnerabilidad = 300; 
                escudoItem = null;
            }
        }

        if (pedidoActual == null) {
            if (contadorTicks % 300 == 0) {
                Casa casa = elegirCasaAleatoria();
                Mercado mercado = elegirMercadoAleatorio();
                pedidoActual = new Pedido(casa, mercado, contadorTicks);
                pedidosEnReparto = "Realizado";
            }
        } else {
            pedidoActual.actualizar(contadorTicks);
            pedidoActual.dibujar(entorno);

            if (pedidoActual.estaEsperando()) {
                pedidosEnReparto = "Esperando";
            } else if (pedidoActual.estaEnCamino()) {
                pedidosEnReparto = "En camino";
            }

            if (pedidoActual.estaEsperando() && repartidorEn(pedidoActual.getMercado().getX(), pedidoActual.getMercado().getY()) && entorno.sePresionoBoton(entorno.BOTON_DERECHO)) {
                pedidoActual.recoger();
                pedidosEnReparto = "En camino";
            } else if (pedidoActual.estaEnCamino() && repartidorEn(pedidoActual.getCasaDestino().getX(), pedidoActual.getCasaDestino().getY()) && entorno.sePresionoBoton(entorno.BOTON_DERECHO)) {
                pedidoActual.entregar();
                pedidosEntregados++;
                pedidosEnReparto = "Entregado";
                pedidoActual = null;
            }

            if (pedidoActual != null && ((pedidoActual.estaTerminado() && pedidoActual.sePerdio()))) {
                pedidosPerdidos++;
                pedidosEnReparto = "Perdido";
                pedidoActual = null;
            }
        }

        entorno.cambiarFont("Arial", 13, Color.BLACK);
        entorno.escribirTexto("Tiempo: "+tiempoMinutos+ " m"+ tiempoSegundos + " s", 60, 25);
        entorno.escribirTexto("Vidas: " + repartidor.getVidas(), 260, 25);
        entorno.escribirTexto("Entregados: " + pedidosEntregados, 400, 25);
        entorno.escribirTexto("Bajas: " + contadorBajas, 560, 25);
        entorno.escribirTexto("Perdidos: " + pedidosPerdidos, 715, 25);
        entorno.escribirTexto(pedidosEnReparto, 400,10);
        if (cthulhu != null) {
            entorno.cambiarFont("Arial", 22, Color.RED);
            entorno.escribirTexto("HP DEL BOSS: " + cthulhu.getVidas(), 320, 580);
        }

   
        if (cthulhu == null) {
            for (int i = 0; i < enemigos.length; i++) {
                Enemigo enemigo = enemigos[i];
                if (enemigo == null) continue;

                enemigo.mover();
                enemigo.dibujar(entorno);

                if (ticksInvulnerabilidad == 0 && enemigo.chocaConRepartidor(repartidor)) {
                    repartidor.restarVida();
                    repartidor.respawn();
                    ticksInvulnerabilidad = 200; 

                    if (pedidoActual != null && pedidoActual.estaEnCamino()) {
                        pedidosPerdidos++;
                        pedidosEnReparto = "Perdido";
                        pedidoActual = null;
                    }
                }

                if (disparo != null && enemigo.chocaConDisparo(disparo)) {
                    if (escudoItem == null && Math.random() < 0.25) {
                        escudoItem = new Escudo(enemigo.getX(), enemigo.getY());
                    }

                    enemigos[i] = null;
                    disparo = null;
                    contadorBajas++;
                    contadorRespawn[i] = 0;
                }
            }

            for (int i = 0; i < enemigos.length; i++) {
                if (enemigos[i] == null) {
                    contadorRespawn[i]++;
                    if (contadorRespawn[i] >= 300) {
                        if (i < 3) {
                            enemigos[i] = crearEnemigoVertical();
                        } else {
                            enemigos[i] = crearEnemigoHorizontal();
                        }
                        contadorRespawn[i] = 0;
                    }
                }
            }
 
            if (bombasEnemigos == null) {
                for (int i = 0; i < enemigos.length; i++) {
                    Enemigo enemigo = enemigos[i];
                    if (enemigo == null || enemigo.getVidas() <= 0) continue;

                    int rangoVision = 50;
                    boolean mismaCalleVertical = Math.abs(enemigo.getX() - repartidor.getX()) < 30;
                    boolean mismaCalleHorizontal = Math.abs(enemigo.getY() - repartidor.getY()) < 30;
                    boolean cercaEnVertical = Math.abs(enemigo.getY() - repartidor.getY()) < rangoVision;
                    boolean cercaEnHorizontal = Math.abs(enemigo.getX() - repartidor.getX()) < rangoVision;

                    if ((mismaCalleVertical && cercaEnVertical) || (mismaCalleHorizontal && cercaEnHorizontal)) {
                        bombasEnemigos = new BombasEnemigos(enemigo.getX(), enemigo.getY(), enemigo.getDirX() != 0);
                        break;
                    }
                }
            }

            if (bombasEnemigos != null) {
                bombasEnemigos.tick();
                bombasEnemigos.dibujar(entorno);

                if (bombasEnemigos.chocaConRepartidor(repartidor)) {
                    estadoJuego = ESTADO_GAMEOVER;
                }

                if (bombasEnemigos.terminoLaExplosion()) {
                    bombasEnemigos = null;
                }
            }
            
        } else {

            cthulhu.moverHacia(repartidor.getX(), repartidor.getY());
            cthulhu.dibujar(entorno);

            if (ticksInvulnerabilidad == 0 && cthulhu.chocaConRepartidor(repartidor)) {
                repartidor.restarVida();
                repartidor.respawn();
                ticksInvulnerabilidad = 200;
                
                if (repartidor.getVidas() <= 0) {
                    murioPorCthulhu = true;
                }
            }

            if (disparo != null && cthulhu.chocaConDisparo(disparo)) {
                cthulhu.restarVida();
                disparo = null;
                if (cthulhu.getVidas() <= 0) {
                    vencioACthulhu = true;
                    estadoJuego = ESTADO_VICTORIA; 
                }
            }
        }

        if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO) && disparo == null) {
            disparo = new Disparo(repartidor.getX(), repartidor.getY(), repartidor.getDirX(), repartidor.getDirY());
        }
        if (disparo != null) {
            disparo.mover();
            disparo.dibujar(entorno);
            if (disparo.salioDePantalla() || disparo.chocaConAlgunaManzana(manzanas)) {
                disparo = null;
            }
        }


        if (repartidor.getVidas() <= 0 || pedidosPerdidos >= 3) {
            estadoJuego = ESTADO_GAMEOVER;
        } else if (pedidosEntregados >= 10 && cthulhu == null) { 
            estadoJuego = ESTADO_VICTORIA;
        }
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Juego juego = new Juego();
    }
}