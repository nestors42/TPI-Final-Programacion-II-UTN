package ui;

import interfaces.MenuPantalla;
import java.util.Scanner;

public abstract class MenuBase implements MenuPantalla {
    protected Scanner scanner;

    public MenuBase() {
        this.scanner = new Scanner(System.in);
    }

    // 🌟 Los hijos solo tendrán que preocuparse por estas dos cosas:
    public abstract void mostrarOpciones();
    protected abstract void evaluarOpcion(int opcion); // El switch de cada hijo


    @Override
    public final void ejecutar() {
        int opcionSub = -1;
        while (opcionSub != 0) {
            mostrarOpciones(); // Llamará al texto del hijo actual
            opcionSub = capturarOpcionNumericaSegura(0, 4);

            if (opcionSub == 0) {
                break; // Si es 0, sale del bucle limpiamente y vuelve atrás
            }

            evaluarOpcion(opcionSub); // Le pasa la opción limpia al switch del hijo
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