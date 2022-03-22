/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package E9;

import java.io.IOException;
import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.util.Random;

public class Client2 {

    private static final String _IP_SERVIDOR = "25.66.109.25";
    private static final int _PUERTO = 1234;
    private static final int _TIMEOUT = 3000;
    private static final int _PAQUETES = 80;

    public static void main(String[] args) throws IOException {
// Determinado en el socket del cliente
        Socket socket = new Socket();
// Establezca el tiempo de espera de la conexión en 3000 ms
        socket.setSoTimeout(_TIMEOUT);
// Conéctese al servidor local, el número de puerto es 2000 y el tiempo de
// espera es 3000
        socket.connect(new InetSocketAddress(InetAddress.getByName(_IP_SERVIDOR), _PUERTO), _TIMEOUT);
        System.out.println("Se ha iniciado la conexion al servidor");
        System.out.println("Cliente IP: " + socket.getLocalAddress() + " Puerto: " + socket.getLocalPort());
        System.out.println("Servidor IP: " + socket.getInetAddress() + " Puerto: " + socket.getPort());
        try {
            send(socket);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error de operacion");
        }
        socket.close();
        System.out.println("Clliente: -> DESCONECTADO");
    }

    private static void send(Socket client) throws IOException, InterruptedException {
// flujo de salida de socket
        OutputStream outputStream = client.getOutputStream();
        PrintStream socketPrintStream = new PrintStream(outputStream);
        String data = "";
// Obtener flujo de entrada de socket
        InputStream inputStream = client.getInputStream();
        BufferedReader socketBufferReader = new BufferedReader(new InputStreamReader(inputStream));
        for (int i = 0; i < _PAQUETES; i++) {
// simulamos obtencion de variables
            Random random = new Random();

            int temp = 10 + random.nextInt((43 - 10) + 1);
            int humedad = 10 + random.nextInt((90 - 10) + 1);
            int co2 = 2950 + random.nextInt((3030 - 2950) + 1);

            //ENVIAR DATOS
            data = temp + "/" + humedad + "/" + co2;
            socketPrintStream.println(data);
            String echo = socketBufferReader.readLine();
            System.out.println("Cliente: " + data);
            System.out.println(echo);
            Thread.sleep(800);
        }
        System.out.println("Cliente: TOKEN 'bye'");
        socketPrintStream.println("bye");
        System.out.println("Cliente: PROCESO FINALIZADO");
    }
}
