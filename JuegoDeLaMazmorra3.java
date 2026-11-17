import java.util.InputMismatchException;
import java.util.Scanner;

public class JuegoDeLaMazmorra3 {

    static final int FILAS = 10;
    static final int COLUMNAS = 20;

    static final char PUNTO = '.';
    static final char JUGADOR = 'P';
    static final char TRAMPA = 'X';
    static final char ENEMIGO = 'E';
    static final char VIDA_EXTRA = 'V';
    static final char LLAVE = 'K';
    static final char COFRE = 'C';

    static char[][] tablero = new char[FILAS][COLUMNAS];

    static int filaJugador = 0;
    static int colJugador = 0;

    static int filaCofre = 0;
    static int colCofre = 0;

    static final int CANTIDAD_TRAMPAS = 10;
    static final int CANTIDAD_ENEMIGOS = 8;
    static final int CANTIDAD_VIDAS_EXTRA = 3;
    static final int RECOMPENSA = 1000;

    static int vidasConfig = 10;
    static int vidas = vidasConfig;
    static boolean tieneLlave = false;
    static boolean cofreAbierto = false;
    static int dinero = 0;

    // Controla el menú y las partidas.
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean salirPrograma = false;

        while (!salirPrograma) {
            mostrarMenuPrincipal();
            int opcionMenu = -1;

            try {
                opcionMenu = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("\nError: introduce un número válido.");
                sc.nextLine();
                continue;
            }

            switch (opcionMenu) {
                case 1:
                    // Permite volver a jugar.
                    boolean jugarOtravez = true;
                    while (jugarOtravez) {
                        ejecutarPartida(sc);

                        System.out.println("\n¿Desea jugar otra vez?");
                        System.out.println("1. Sí");
                        System.out.println("2. No");
                        System.out.print("Seleccione una opción: ");

                        int opcionRejugar = -1;
                        try {
                            opcionRejugar = sc.nextInt();
                            sc.nextLine();
                        } catch (InputMismatchException e) {
                            sc.nextLine();
                            opcionRejugar = 2;
                        }

                        if (opcionRejugar != 1) {
                            jugarOtravez = false;
                        }
                    }
                    break;

                case 2:
                    System.out.println("\n--- CONFIGURACIÓN ---");
                    System.out.println("Vidas iniciales: " + vidasConfig);
                    break;

                case 3:
                    System.out.println("\n--- INSTRUCCIONES ---");
                    System.out.println("Usa W, A, S, D para moverte.");
                    System.out.println("Busca la llave (K) y luego el cofre (C).");
                    break;

                case 4:
                    System.out.println("\n--- CRÉDITOS ---");
                    System.out.println("Creado para el examen de programación.");
                    break;

                case 5:
                    System.out.println("\nSaliendo...");
                    salirPrograma = true;
                    break;

                default:
                    System.out.println("\nOpción no válida. Pon un número del 1 al 5.");
                    break;
            }
        }
        sc.close();
    }

    // Ejecuta una partida.
    static void ejecutarPartida(Scanner sc) {
        vidas = vidasConfig;
        dinero = 0;
        tieneLlave = false;
        cofreAbierto = false;

        inicializarTablero();
        colocarElementos();

        boolean salirPartida = false;

        while (!salirPartida && vidas > 0) {
            mostrarTablero();

            if (cofreAbierto) {
                System.out.println("¡Has ganado! Encontraste el tesoro.");
                break;
            }

            System.out.println("Mover: W S A D | Q (salir)");
            System.out.print("Opción: ");

            String opcion = sc.nextLine().trim().toLowerCase();

            switch (opcion) {
                case "w":
                    mover(-1, 0, sc);
                    break;
                case "s":
                    mover(1, 0, sc);
                    break;
                case "a":
                    mover(0, -1, sc);
                    break;
                case "d":
                    mover(0, 1, sc);
                    break;
                case "q":
                    salirPartida = true;
                    System.out.println("\nHas abandonado la partida.");
                    break;
                default:
                    System.out.println("Movimiento inválido.");
            }
        }

        if (vidas <= 0) {
            System.out.println("GAME OVER: Te has quedado sin vidas.");
            mostrarTableroFinal();
        }

        System.out.println("Dinero total: " + dinero + " euros\n");
    }

    // Muestra el menú principal.
    static void mostrarMenuPrincipal() {
        System.out.println("\n=== LA MAZMORRA INFINITA ===");
        System.out.println("1. Nueva Partida");
        System.out.println("2. Configuración");
        System.out.println("3. Instrucciones");
        System.out.println("4. Créditos");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opción: ");
    }

    // Prepara el tablero y coloca al jugador.
    static void inicializarTablero() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                tablero[i][j] = PUNTO;
            }
        }
        filaJugador = FILAS / 2;
        colJugador = COLUMNAS / 2;
        tablero[filaJugador][colJugador] = JUGADOR;
    }

    // Coloca los elementos al azar.
    static void colocarElementos() {
        int trampas = 0, enemigos = 0, vidasExtra = 0;
        boolean llave = false, cofre = false;

        while (trampas < CANTIDAD_TRAMPAS || enemigos < CANTIDAD_ENEMIGOS
                || vidasExtra < CANTIDAD_VIDAS_EXTRA || !llave || !cofre) {

            int f = (int) (Math.random() * FILAS);
            int c = (int) (Math.random() * COLUMNAS);

            if (tablero[f][c] == PUNTO) {
                if (trampas < CANTIDAD_TRAMPAS) {
                    tablero[f][c] = TRAMPA;
                    trampas++;
                } else if (enemigos < CANTIDAD_ENEMIGOS) {
                    tablero[f][c] = ENEMIGO;
                    enemigos++;
                } else if (vidasExtra < CANTIDAD_VIDAS_EXTRA) {
                    tablero[f][c] = VIDA_EXTRA;
                    vidasExtra++;
                } else if (!llave) {
                    tablero[f][c] = LLAVE;
                    llave = true;
                } else if (!cofre) {
                    tablero[f][c] = COFRE;
                    filaCofre = f;
                    colCofre = c;
                    cofre = true;
                }
            }
        }
    }

    // Muestra el tablero con los elementos ocultos.
    static void mostrarTablero() {
        System.out.println("\n--- Vidas: " + vidas + " | Dinero: " + dinero + " ---");

        if (tieneLlave) {
            System.out.println("¡Tienes la llave! Busca el cofre.");
        }

        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (tablero[i][j] == TRAMPA || tablero[i][j] == ENEMIGO
                        || tablero[i][j] == VIDA_EXTRA || tablero[i][j] == LLAVE) {
                    System.out.print(PUNTO + " ");
                } else {
                    System.out.print(tablero[i][j] + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    // Revela todo el tablero.
    static void mostrarTableroFinal() {
        System.out.println("\n--- TABLERO FINAL ---");
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                System.out.print(tablero[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Mueve al jugador y aplica los efectos de la casilla.
    static void mover(int df, int dc, Scanner sc) {
        int nf = filaJugador + df;
        int nc = colJugador + dc;

        if (nf < 0 || nf >= FILAS || nc < 0 || nc >= COLUMNAS) {
            System.out.println("No te puedes salir del tablero.");
            return;
        }

        if (filaJugador == filaCofre && colJugador == colCofre && !cofreAbierto) {
            tablero[filaJugador][colJugador] = COFRE;
        } else {
            tablero[filaJugador][colJugador] = PUNTO;
        }

        filaJugador = nf;
        colJugador = nc;

        char actual = tablero[filaJugador][colJugador];
        switch (actual) {
            case TRAMPA:
                vidas--;
                System.out.println("¡Has pisado una trampa (-1 vida)!");
                break;
            case ENEMIGO:
                System.out.println("¡Lucha contra un enemigo!");
                jugarPiedraPapelTijera(sc);
                break;
            case VIDA_EXTRA:
                vidas++;
                System.out.println("¡Encontraste una vida extra (+1)!");
                break;
            case LLAVE:
                tieneLlave = true;
                System.out.println("¡Has cogido la llave!");
                break;
            case COFRE:
                if (tieneLlave) {
                    cofreAbierto = true;
                    tieneLlave = false;
                    dinero += RECOMPENSA;
                    System.out.println("¡Has abierto el cofre!");
                } else {
                    System.out.println("El cofre está cerrado, necesitas la llave.");
                }
                break;
        }

        tablero[filaJugador][colJugador] = JUGADOR;
    }

    // Resuelve el combate con el enemigo.
    static void jugarPiedraPapelTijera(Scanner sc) {
        String[] opts = {"piedra", "papel", "tijera"};
        boolean fin = false;

        while (!fin) {
            System.out.println("Elige: 1. Piedra | 2. Papel | 3. Tijera");
            System.out.print("Opción: ");
            String entrada = sc.nextLine().trim();

            if (!entrada.equals("1") && !entrada.equals("2") && !entrada.equals("3")) {
                System.out.println("Opción incorrecta");
                continue;
            }

            int jugador = Integer.parseInt(entrada) - 1;
            int maquina = (int) (Math.random() * 3);

            System.out.println("Tú: " + opts[jugador] + " | Enemigo: " + opts[maquina]);

            if (jugador == maquina) {
                System.out.println("Empate, otra vez.");
            } else if ((jugador + 1) % 3 == maquina) {
                vidas -= 3;
                System.out.println("Has perdido (-3 vidas).");
                fin = true;
            } else {
                vidas += 2;
                System.out.println("Has ganado (+2 vidas).");
                fin = true;
            }
        }
    }
}