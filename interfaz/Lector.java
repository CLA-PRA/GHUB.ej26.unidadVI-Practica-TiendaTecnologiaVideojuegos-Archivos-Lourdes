package interfaz;

import java.util.Locale;
import java.util.Scanner;

public class Lector {
    private final Scanner sc;

    public Lector() {
        this.sc = new Scanner(System.in).useLocale(Locale.US);
    }

    public String leerLinea(String prompt) {
        System.out.print(prompt);
        String s = sc.nextLine();
        while (s == null || s.isBlank()) {
            System.out.print("No vacío. " + prompt);
            s = sc.nextLine();
        }
        return s.trim();
    }

    public int leerEntero(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Ingresa un entero válido.");
            }
        }
    }

    public double leerDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Ingresa un número decimal válido (usa punto . como separador).");
            }
        }
    }
}
