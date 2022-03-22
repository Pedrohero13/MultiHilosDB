/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package E9;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {
// Configuramos la IP y el Puerto para el servidor

    private static final String _IP = "25.66.109.25";
    // private static final String _IP = "192.168.1.68";
    private static final int _PUERTO = 1234;
    private static final int _BACKLOG = 50;
// Formato de salida por consola
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CLEAR = "\033[H\033[2J";

    public static void main(String[] args) throws IOException {
        InetAddress ip = InetAddress.getByName(_IP);
        ServerSocket serverSocket = new ServerSocket(_PUERTO, _BACKLOG, ip);
        try {
            System.out.println(ANSI_CLEAR);
            System.out.println(ANSI_GREEN + "Servidor iniciado . . . OK" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "información del servidor: " + ANSI_RESET
                    + ANSI_YELLOW + serverSocket.getInetAddress() + ":" + serverSocket.getLocalPort()
                    + ANSI_RESET);
            System.out.println("--------------------------------------------------------------------");
            for (;;) {
                Socket client = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(client);

                clientHandler.start();
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            serverSocket.close();
        }
    }

    private static class ClientHandler extends Thread {

// Recibe un socket de una solicitud
        private Socket socket;
// Bandera de terminación
        private boolean flag;
// Recibe un conector para la construcción y establece el indicador de ciclo en
// verdadero

        ClientHandler(Socket socket) {
            this.socket = socket;
            flag = true;
        }

        // Método de ejecución de reescritura
        @Override
        public void run() {
            super.run();
            System.out.println("Cliente: " + socket.getInetAddress() + ":" + socket.getPort()
                    + ANSI_GREEN + " -> CONECTADO" + ANSI_RESET);
            try {
                String nombreHilo = this.currentThread().getName();
                // Secuencia de impresión, utilizada para que el servidor envíe datos
                PrintStream socketOutput = new PrintStream(socket.getOutputStream());
                // Obtener el flujo de entrada para recibir datos
                BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // Spin, termina cuando recibe la cadena "bye" del cliente
                while (flag) {
                    // Recibir una línea de requestBody enviada por el cliente
                    String entrada = socketInput.readLine();
                    String salida = "";
                    int puertoRemitente = socket.getPort();
                    InetAddress ipRemitente = socket.getInetAddress();

                    if (entrada != null) {
                        // Si el cliente envía "adiós", establezcala bandera en falso y cierre el
                        // recurso
                        if ("bye".equalsIgnoreCase(entrada)) {
                            flag = false;
                            socketOutput.println("Cliente: " + ANSI_YELLOW + socket.getInetAddress() + " "
                                    + "Termino la conexion" + ANSI_RESET);
                        } else {
                            String split[] = entrada.split("/");
                            int temp = Integer.parseInt(split[0]);
                            int hume = Integer.parseInt(split[1]);
                            int co2 = Integer.parseInt(split[2]);
                            DAOLog dao = new DAOLog();

                            if (temp < 15 || temp > 39 || hume > 80 || co2 > 3000) {
                                salida = "Se genero un evento";
                                Log log = guardar(ipRemitente.toString(), puertoRemitente, entrada, true);
                                dao.guardar(log);
                                socketOutput.println("Servidor: Evento ?" + ANSI_BLUE + salida + ANSI_RESET+ "\tData: "
                                    + ANSI_BLUE + entrada + " "+ ANSI_RESET);
                            } else {
                                Log log = guardar(ipRemitente.toString(), puertoRemitente, entrada, false);
                                dao.guardar(log);
                                salida = "No se genero evento";
                                socketOutput.println("Servidor: Evento ?" + ANSI_RED + salida + ANSI_RESET+ "\tData: "
                                    + ANSI_BLUE + entrada + " "+ ANSI_RESET);
                            }
                            // Salida de la información y los caracteres enviados por el cliente en la// consola
                            System.out.println("Servidor: " + ANSI_GREEN + "IP: " + socket.getInetAddress() + ":"
                                    + socket.getPort() + ANSI_RESET + ANSI_YELLOW + "\tdata: " + entrada + ANSI_RESET
                                    + ANSI_CYAN + "\thilo: " + nombreHilo);
                            // Devuelve respuesta al cliente
                            
                            
                        }
                    }
                }
                // Cerrar IO
                socketInput.close();
                socketOutput.close();
                socket.close();
            } catch (SocketException ex) {
                System.out.println(ANSI_RED + "Error: " + ex.getMessage() + ANSI_RESET);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Conexión anormal");
            } finally {
                try {
                    // Cerrar la conexión del  socket
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Cliente: " + socket.getInetAddress()
                            + ANSI_RED + " -> DESCONECTADO" + ANSI_RESET);
                }
            }
        }

        public static Log guardar(String ip, int puerto, String entrada, boolean evento) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyy");
            SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss");
            formatter.format(new Date());

            Log log = new Log();
            log.setFecha(formatter.format(new Date()));
            log.setHora(hora.format(new Date()));
            log.setIpRemitente(ip);
            log.setPuerto(puerto);
            log.setEntrada(entrada);
            log.setEvento(evento);
            return log;

        }
    }
}
