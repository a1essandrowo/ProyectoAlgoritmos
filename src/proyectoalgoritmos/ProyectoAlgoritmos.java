package proyectoalgoritmos;

import igu.Pantalla;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.table.DefaultTableModel;

/**
 * @date 18/10/24
 * @author angelo chamale
 */
public class ProyectoAlgoritmos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Pantalla panta = new Pantalla();
        panta.setVisible(true);
        panta.setLocationRelativeTo(null);

    }

    private static final String ARCHIVO_CATEGORIAS = "categorias.txt";

    // Método para guardar las categorías en el archivo de texto
    public static void guardarCategoriaEnArchivo(String id, String nombre, String descripcion) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_CATEGORIAS, true))) {
            writer.write(id + "|" + nombre + "|" + descripcion);
            writer.newLine(); // Saltar a la nueva línea para la próxima categoría
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para cargar las categorías desde un archivo de texto
    public void cargarCategoriasDesdeArchivo(DefaultTableModel model) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_CATEGORIAS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length == 3) {
                    model.addRow(new Object[]{datos[0], datos[1], datos[2]});
                }
            }
            System.out.println("Categorías cargadas exitosamente.");
        } catch (FileNotFoundException e) {
            System.err.println("El archivo de categorías no existe.");
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de categorías: " + e.getMessage());
        }
    }

    // Método para editar una categoría en el archivo de texto
    public void editarCategoriaEnArchivo(DefaultTableModel model, int filaSeleccionada, String nuevoNombre, String nuevaDescripcion) {
        if (filaSeleccionada == -1) {
            System.err.println("No hay una fila seleccionada para editar.");
            return;
        }

        model.setValueAt(nuevoNombre, filaSeleccionada, 1);
        model.setValueAt(nuevaDescripcion, filaSeleccionada, 2);
        // Actualizar el archivo después de la edición
        List<String[]> categorias = obtenerCategoriasDesdeModelo(model);
        sobrescribirArchivoConCategorias(categorias);
        System.out.println("Categoría editada exitosamente.");
    }

    // Método para eliminar una categoría en el archivo de texto
    public void eliminarCategoriaEnArchivo(DefaultTableModel model, int filaSeleccionada) {
        if (filaSeleccionada == -1) {
            System.err.println("No hay una fila seleccionada para eliminar.");
            return;
        }

        model.removeRow(filaSeleccionada);
        // Actualizar el archivo después de la eliminación
        List<String[]> categorias = obtenerCategoriasDesdeModelo(model);
        sobrescribirArchivoConCategorias(categorias);
        System.out.println("Categoría eliminada exitosamente.");
    }

    // Método para leer todas las categorías del archivo de texto
    public static List<String[]> leerCategoriasDelArchivo() {
        List<String[]> categorias = new ArrayList<>();
        File archivo = new File("categorias.txt");

        // Si el archivo no existe, se crea uno vacío
        if (!archivo.exists()) {
            try {
                archivo.createNewFile(); // Crear el archivo vacío
            } catch (IOException e) {
                e.printStackTrace();
            }
            return categorias; // Retornar lista vacía
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Separar por el carácter '|'
                String[] datos = linea.split("\\|");
                categorias.add(datos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return categorias;
    }

    // Método para sobreescribir el archivo de texto (usado para editar y eliminar)
    public static void sobrescribirArchivoConCategorias(List<String[]> categorias) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_CATEGORIAS))) {
            for (String[] categoria : categorias) {
                writer.write(categoria[0] + "|" + categoria[1] + "|" + categoria[2]);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Método para obtener categorías desde el modelo de la tabla
    private List<String[]> obtenerCategoriasDesdeModelo(DefaultTableModel model) {
        List<String[]> categorias = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            String id = model.getValueAt(i, 0).toString();
            String nombre = model.getValueAt(i, 1).toString();
            String descripcion = model.getValueAt(i, 2).toString();
            categorias.add(new String[]{id, nombre, descripcion});
        }
        return categorias;
    }
}
