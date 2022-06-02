package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.view.cli.Cli;

import java.util.logging.Level;

import static java.lang.System.out;

// Java program to demonstrate working of Scanner in Java
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) {

        boolean cliParam = false; // default value
        out.print("Scegliere CLI");
        Scanner in = new Scanner(System.in);
        String answer = in.nextLine();
        for (String arg : args) {
            if (arg.equals("--cli") || arg.equals("-c")) {
                cliParam = true;
                break;
            }
        }
        
        if (answer.equals("cli")) {
            Client.LOGGER.setLevel(Level.WARNING);
            Cli view = new Cli();
            ClientController clientcontroller = new ClientController(view);
            view.addObserver(clientcontroller);
            view.init();
        } else {
            //Application.launch(JavaFXGui.class);
        }
    }
}
