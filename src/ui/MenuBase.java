package ui;

import java.util.Scanner;

public abstract class MenuBase {
    protected Scanner scanner;

    public MenuBase() {
        this.scanner = new Scanner(System.in);
    }

    // cada submenú se verá obligado a implementar sus opciones
    public abstract void mostrarOpciones();

    // Método heredable y reutilizable: centraliza el try-catch de opciones numéricas
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
