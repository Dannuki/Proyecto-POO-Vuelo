/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vuelosfis.main;
import java.util.Scanner;
/**
 *
 * @author USUARIO
 */

public class Main {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=========================================");
        System.out.println("   ✈️  SISTEMA VUELOS-FIS - INICIO  ✈️");
        System.out.println("=========================================");
        
        // Simulación básica de Login (Esto lo mejorará el Miembro 1)
        System.out.println("Por favor, identifíquese:");
        System.out.print("Usuario: ");
        String user = scanner.nextLine();
        
        System.out.print("Contraseña: ");
        String pass = scanner.nextLine();
        
        if (user.equals("admin") && pass.equals("admin123")) {
            System.out.println(">> ✅ Bienvenido ADMINISTRADOR.");
            System.out.println("   [1] Gestionar Aviones");
            System.out.println("   [2] Gestionar Rutas");
        } else if (user.equals("cliente") && pass.equals("123")) {
            System.out.println(">> ✅ Bienvenido CLIENTE.");
            System.out.println("   [1] Buscar Vuelo");
            System.out.println("   [2] Ver mis Reservas");
        } else {
            System.out.println(">> ❌ Credenciales incorrectas.");
        }
    }
}
