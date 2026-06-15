package ui;

import interfaces.MenuPantalla;

import java.util.Scanner;

// 🌟 Ahora la clase abstracta implementa el contrato de la interfaz
public abstract class MenuBase implements MenuPantalla {
    protected Scanner scanner;

    public MenuBase() {
        this.scanner = new Scanner(System.in);
    }

    // Método abstracto que cada hijo usará para imprimir su propio texto
    public abstract void mostrarOpciones();

    // 🌟 Reutilizamos la firma de la interfaz: cada pantalla definirá su bucle aquí
    @Override
    public abstract void ejecutar();

    // El método utilitario con try-catch que ya tenías y que protege todo el sistema
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