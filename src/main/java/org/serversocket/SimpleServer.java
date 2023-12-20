package org.serversocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SimpleServer {
    public static void main(String[] args) {

        int portNumber = 8081;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server is listening on port " + portNumber);

            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out.println("Hello from server!");

            String clientMessage = in.readLine().trim();
            do {
                out.println("Я з Вами привітався! Будь ласка, введіть щось:");
                clientMessage = in.readLine().trim();
            } while (clientMessage.isEmpty());

            System.out.println("Client says: " + clientMessage);
            if (containsRussianLetters(clientMessage)) {
                out.println("Що таке паляниця?");
                String clientAnswer = in.readLine();

                if (clientAnswer.equalsIgnoreCase("хліб")) {
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = currentDateTime.format(formatter);
                    out.println("Поточна дата і час: " + formattedDateTime);
                    System.out.println("Client answered correctly. Sent current date and time.");
                    out.println("Дякуємо за обмін повідомленнями. До побачення!");
                    System.out.println("Server is saying goodbye.");

                } else {
                    out.println("Неправильна відповідь. Ви відключені.");
                    System.out.println("Client provided incorrect answer. Disconnecting.");
                }

            } else {
                out.println("Добре, що Ви не використовуєте російські літери");
                out.println("До побачення!");
            }
            clientSocket.close();

        } catch (IOException e) {
            System.err.println("Error in the server: " + e.getMessage());
        }
    }

    private static boolean containsRussianLetters(String str) {
        String russianAlphabet = "ЁЪЫЭёъыэ";
        for (char c : str.toCharArray()) {
            if (russianAlphabet.contains(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }

}
