package ui;

import interfaces.MenuPantalla;
import java.util.Scanner;

public abstract class MenuBase implements MenuPantalla {
    protected Scanner scanner;

    public MenuBase() {
        this.scanner = new Scanner(System.in);
    }

    // Contrato obligatorio para los hijos
    public abstract void mostrarOpciones();
    protected abstract void evaluarOpcion(int opcion);
    protected abstract int getOpcionMaxima();

    // pantalla centralizada
    @Override
    public final void ejecutar() {
        int opcionSub = -1;
        while (opcionSub != 0) {
            mostrarOpciones();

            //El rango máximo ahora viene de lo que defina el hijo
            opcionSub = capturarOpcionNumericaSegura(0, getOpcionMaxima());

            // Le avisa al hijo que eligieron el 0
            evaluarOpcion(opcionSub);

            if (opcionSub == 0) {
                break; // Rompe el bucle si eligió salir o volver
            }
        }
    }

    protected int capturarOpcionNumericaSegura(int rangoMin, int rangoMax) {
        while (true) {
            System.out.print("Seleccione una opción: ");
            try {
                int entrada = Integer.parseInt(scanner.nextLine());
                if (entrada >= rangoMin && entrada <= rangoMax) {
                    return entrada;
                }
                System.out.println("[ALERTA] Opción fuera de rango (" + rangoMin + "-" + rangoMax + "). Pruebe otra vez.");
            } catch (NumberFormatException e) {
                System.out.println("[ALERTA] Entrada inválida. Debe ingresar una opción numérica entera.");
            }
        }
    }
}