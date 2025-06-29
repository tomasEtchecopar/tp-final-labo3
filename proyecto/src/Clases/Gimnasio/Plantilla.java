package Clases.Gimnasio;

import java.util.List;

/// CLASE PLANTILLA: son las rutinas que el usuario puede elegir. Se almacenan en el JSON 'plantillas.json'.
public class Plantilla extends Rutina{
    private int id; // id que indica para qué usuario con la misma id es la rutina. si el id es 0 la rutina es para todos los usuarios.

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Plantilla() {
        super();
    }

    public Plantilla(String nombre, int id) {
        super(nombre);
        this.id = id;
    }

    public Plantilla(String nombre, List<Serie> series, int id) {
        super(nombre, series);
        this.id = id;
    }

    @Override
    public void mostrarRutina(){
        System.out.println("--" + this.getNombre());
        mostrarSeriesPorEjercicio();
    }

    @Override
    public void mostrarSeriesPorEjercicio(){
        if (this.getSeries() == null || this.getSeries().isEmpty()) {
            System.out.println("La rutina está vacía.");
        }else {
            String ejercicioAnterior = "";
            int contador=0;
            for (Serie serie : this.getSeries()) {
                contador++;
                String nombreEjercicio = serie.getEjercicio().getNombre();

                if (!nombreEjercicio.equals(ejercicioAnterior)) {
                    System.out.println("Ejercicio: " + nombreEjercicio);
                    ejercicioAnterior = nombreEjercicio;
                }

                System.out.println(contador+") "+serie.getPeso() + " KG" + " x " + serie.getRepeticiones());
            }
        }

    }
}
