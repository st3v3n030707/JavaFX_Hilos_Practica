package ni.edu.uam.practicahilos.practicahilosjavafx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class ProcesadorController {

    // Controles de Clientes
    @FXML private Label lblEstadoClientes;
    @FXML private Label lblPorcentajeClientes;
    @FXML private ProgressBar barraClientes;
    @FXML private Button btnIniciarClientes;
    @FXML private Button btnDetenerClientes;

    // Controles de Productos
    @FXML private Label lblEstadoProductos;
    @FXML private Label lblPorcentajeProductos;
    @FXML private ProgressBar barraProductos;
    @FXML private Button btnIniciarProductos;
    @FXML private Button btnDetenerProductos;

    // Botones Generales
    @FXML private Button btnSimularAmbos;
    @FXML private Button btnReiniciar;

    // Los dos objetos Thread independientes
    private Thread hiloClientes;
    private Thread hiloProductos;

    @FXML
    private void simularAmbos() {
        iniciarClientes();
        iniciarProductos();
    }

    @FXML
    private void iniciarClientes() {
        // Requerimiento 8: Evitar iniciar un proceso dos veces simultáneamente
        if (hiloClientes != null && hiloClientes.isAlive()) {
            return;
        }

        barraClientes.setProgress(0);
        lblPorcentajeClientes.setText("0% (0/100)");
        lblEstadoClientes.setText("Procesando clientes...");
        btnIniciarClientes.setDisable(true);
        btnDetenerClientes.setDisable(false);
        actualizarEstadoBotonesGenerales();

        // Requerimiento 1 y 2: Runnable para procesar 100 registros de clientes
        Runnable tareaClientes = () -> {
            final int totalRegistros = 100;
            try {
                for (int i = 1; i <= totalRegistros; i++) {
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }
                    // Requerimiento 4: Tiempo de ejecución diferente (80 ms)
                    Thread.sleep(80);
                    final int actual = i;
                    final int porcentaje = (actual * 100) / totalRegistros;

                    Platform.runLater(() -> {
                        barraClientes.setProgress((double) actual / totalRegistros);
                        lblPorcentajeClientes.setText(porcentaje + "% (" + actual + "/" + totalRegistros + ")");
                    });
                }
                Platform.runLater(() -> {
                    lblEstadoClientes.setText("Proceso finalizado");
                    btnIniciarClientes.setDisable(false);
                    btnDetenerClientes.setDisable(true);
                    actualizarEstadoBotonesGenerales();
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Platform.runLater(() -> {
                    lblEstadoClientes.setText("Proceso detenido");
                    btnIniciarClientes.setDisable(false);
                    btnDetenerClientes.setDisable(true);
                    actualizarEstadoBotonesGenerales();
                });
            }
        };

        hiloClientes = new Thread(tareaClientes);
        hiloClientes.setName("Hilo-Clientes");
        hiloClientes.setDaemon(true);
        hiloClientes.start();
        actualizarEstadoBotonesGenerales();
    }

    @FXML
    private void iniciarProductos() {
        // Requerimiento 8: Evitar iniciar un proceso dos veces simultáneamente
        if (hiloProductos != null && hiloProductos.isAlive()) {
            return;
        }

        barraProductos.setProgress(0);
        lblPorcentajeProductos.setText("0% (0/50)");
        lblEstadoProductos.setText("Procesando productos...");
        btnIniciarProductos.setDisable(true);
        btnDetenerProductos.setDisable(false);
        actualizarEstadoBotonesGenerales();

        // Requerimiento 1 y 3: Runnable para procesar 50 registros de productos
        Runnable tareaProductos = () -> {
            final int totalRegistros = 50;
            try {
                for (int i = 1; i <= totalRegistros; i++) {
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }
                    // Requerimiento 4: Tiempo de ejecución diferente (150 ms)
                    Thread.sleep(150);
                    final int actual = i;
                    final int porcentaje = (actual * 100) / totalRegistros;

                    Platform.runLater(() -> {
                        barraProductos.setProgress((double) actual / totalRegistros);
                        lblPorcentajeProductos.setText(porcentaje + "% (" + actual + "/" + totalRegistros + ")");
                    });
                }
                Platform.runLater(() -> {
                    lblEstadoProductos.setText("Proceso finalizado");
                    btnIniciarProductos.setDisable(false);
                    btnDetenerProductos.setDisable(true);
                    actualizarEstadoBotonesGenerales();
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Platform.runLater(() -> {
                    lblEstadoProductos.setText("Proceso detenido");
                    btnIniciarProductos.setDisable(false);
                    btnDetenerProductos.setDisable(true);
                    actualizarEstadoBotonesGenerales();
                });
            }
        };

        hiloProductos = new Thread(tareaProductos);
        hiloProductos.setName("Hilo-Productos");
        hiloProductos.setDaemon(true);
        hiloProductos.start();
        actualizarEstadoBotonesGenerales();
    }

    // Requerimiento 7: Permitir detener cada hilo individualmente
    @FXML
    private void detenerClientes() {
        if (hiloClientes != null && hiloClientes.isAlive()) {
            btnDetenerClientes.setDisable(true);
            lblEstadoClientes.setText("Solicitando detención...");
            hiloClientes.interrupt();
        }
    }

    @FXML
    private void detenerProductos() {
        if (hiloProductos != null && hiloProductos.isAlive()) {
            btnDetenerProductos.setDisable(true);
            lblEstadoProductos.setText("Solicitando detención...");
            hiloProductos.interrupt();
        }
    }

    @FXML
    private void reiniciarProceso() {
        boolean clientesActivo = hiloClientes != null && hiloClientes.isAlive();
        boolean productosActivo = hiloProductos != null && hiloProductos.isAlive();
        if (clientesActivo || productosActivo) {
            return;
        }

        barraClientes.setProgress(0);
        lblPorcentajeClientes.setText("0% (0/100)");
        lblEstadoClientes.setText("Esperando inicio");

        barraProductos.setProgress(0);
        lblPorcentajeProductos.setText("0% (0/50)");
        lblEstadoProductos.setText("Esperando inicio");

        actualizarEstadoBotonesGenerales();
    }

    private void actualizarEstadoBotonesGenerales() {
        boolean clientesActivo = hiloClientes != null && hiloClientes.isAlive();
        boolean productosActivo = hiloProductos != null && hiloProductos.isAlive();

        btnReiniciar.setDisable(clientesActivo || productosActivo);
        btnSimularAmbos.setDisable(clientesActivo && productosActivo);
    }

    public void detenerAlCerrar() {
        if (hiloClientes != null && hiloClientes.isAlive()) {
            hiloClientes.interrupt();
        }
        if (hiloProductos != null && hiloProductos.isAlive()) {
            hiloProductos.interrupt();
        }
    }
}