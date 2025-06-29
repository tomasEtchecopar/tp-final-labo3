package Clases.Menu.Usuario;

import Clases.Gimnasio.Entrenamiento;
import Clases.Gimnasio.Plantilla;
import Clases.Gimnasio.Serie;

import Clases.Menu.Admin.MenuRutinasAdmin;
import Clases.Menu.MainMenu;
import Clases.Menu.Utiles.LecturaTeclado;
import Clases.Menu.Utiles.Mostrado;
import Clases.Usuario.Usuario;
import Clases.manejoJSON.JSONPlantilla;
import Clases.manejoJSON.JSONUsuario;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MenuEntrenamiento {
    public static void mostrar(Scanner teclado, Usuario usuario) {
        int opcion = -1;
        while (opcion != 4) {
            MainMenu.limpiarConsola();
            System.out.println("=============================");
            System.out.println("\tAPP DE ENTRENAMIENTO ");
            System.out.println("=============================");
            System.out.println("1) Empezar entrenamiento");
            System.out.println("2) Ver catalogo de ejercicios");
            System.out.println("3) Ver catalogo de rutinas");
            System.out.println("4) Volver");
            opcion = LecturaTeclado.leerEntero(teclado, 1, 4);
            menuCaller(teclado, opcion, usuario);
        }
    }

    private static void menuCaller(Scanner teclado, int opcion, Usuario usuario) {
        MainMenu.limpiarConsola();
        switch(opcion){
            case 1->{
                usuario.entrenar(teclado);
                JSONUsuario.actualizarUsuario(usuario);
            }
            case 2 -> Mostrado.verEjercicios(teclado);
            case 3 -> {
                System.out.println("Elije una opcion: ");
                System.out.println("1) Ver plantillas disponibles para todos los usuarios");
                System.out.println("2) Ver plantillas disponibles para "+usuario.getUsuario());
                System.out.println("3) Volver");
                int eleccion = LecturaTeclado.leerEntero(teclado, 1, 3);
                switch(eleccion) {
                    case 1-> {
                        System.out.println("Plantillas disponibles para todos los usuarios: ");
                        MenuRutinasAdmin.mostrarPlantillasPorUsuario(teclado, 0);
                    }
                    case 2-> {
                        System.out.println("Plantillas disponibles para " + usuario.getUsuario() + ": ");
                        MenuRutinasAdmin.mostrarPlantillasPorUsuario(teclado, usuario.getId());
                    }
                    }
                }
            case 4->{
                return;
            }
        }
        LecturaTeclado.continuar(teclado);
    }


    private static Plantilla elegirPlantilla(Scanner teclado, Usuario usuario){
        List<Plantilla> plantillas = JSONPlantilla.leerPlantillas();

        if (plantillas.isEmpty()) {
            System.out.println("No hay plantillas disponibles actualmente.");
            return null;
        }
        plantillas.removeIf(p -> p.getId() != usuario.getId() && p.getId() != 0);

        System.out.println("----- ELIGE UNA PLANTILLA PARA TU ENTRENAMIENTO -----");
        for (int i = 0; i < plantillas.size(); i++) {
            System.out.println((i+1) + ". " + plantillas.get(i).getNombre());
        }

        System.out.println("Ingrese la plantilla que desea elegir: ");
        int numeroElegido = LecturaTeclado.leerEntero(teclado, 1, plantillas.size());
        Plantilla seleccionada = plantillas.get(numeroElegido-1);

        System.out.println("Has elegido la plantilla: ");
        seleccionada.mostrarRutina();

        return seleccionada;
    }

    public static Entrenamiento entrenarPorConsola(Scanner teclado, Usuario usuario){
        Plantilla seleccionada = elegirPlantilla(teclado, usuario);
        if(seleccionada==null){
            return null;
        }

        Entrenamiento entrenamiento = new Entrenamiento(seleccionada.getNombre(), LocalDateTime.now().format( DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        List<Serie> seriesDelEntrenamiento = seleccionada.getSeries();

        for (int i = 0; i < seriesDelEntrenamiento.size(); i++) {
            Serie serieActual = seriesDelEntrenamiento.get(i);
            String nombreEjercicio = serieActual.getEjercicio().getNombre();
            int repeticiones = serieActual.getRepeticiones();
            double peso = serieActual.getPeso();

            System.out.println("\n--"+nombreEjercicio + "\nSERIE "+(i+1)+ "/" + seriesDelEntrenamiento.size());
            System.out.println("Repeticiones sugeridas: " + repeticiones);
            System.out.println("Peso sugerido: " + peso + " kg");

            System.out.println("\nDesea ajustar el peso (" + peso + " kg) antes de hacer la serie? (s/n)");

            if(LecturaTeclado.leerBooleanSN(teclado)) {
                System.out.println("Ingrese nuevo peso: ");
                peso = LecturaTeclado.leerDouble(teclado, 0.1, 500.0);
                serieActual.setPeso(peso);
                System.out.println("Peso ajustado a " + peso + " kg");
            }

            System.out.println("Desea ajustar las repeticiones ("+repeticiones+")? (s/n)");
            if(LecturaTeclado.leerBooleanSN(teclado)){
                System.out.println("Ingrese las repeticiones: ");
                repeticiones = LecturaTeclado.leerEntero(teclado, 1, 500);
                serieActual.setRepeticiones(repeticiones);
                System.out.println("Repeticiones ajustadas a "+ repeticiones);
            }
            System.out.print("\nPresiona ENTER para continuar a la siguiente serie...");
            teclado.nextLine();


            entrenamiento.agregarSerie(serieActual);

            System.out.println("Serie completada!");
        }
        System.out.println("---ENTRENAMIENTO COMPLETO---");
        entrenamiento.mostrarRutina();

        return entrenamiento;

    }
}
