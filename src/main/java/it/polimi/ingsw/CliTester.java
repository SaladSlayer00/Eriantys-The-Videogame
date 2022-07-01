package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.SocketServer;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.gui.scenes.GuiJavaFX;
import javafx.application.Application;

import java.util.Scanner;
import java.util.logging.Level;

import static java.lang.System.out;

public class CliTester {
//    public static void main(String[] args) {
//
//        boolean cliParam = false; // default value
//        out.print("Scegliere CLI");
//        Scanner in = new Scanner(System.in);
//        String answer = in.nextLine();
//        for (String arg : args) {
//            if (arg.equals("--cli") || arg.equals("-c")) {
//                cliParam = true;
//                break;
//            }
//        }
//
//        if (answer.equals("cli")) {
//            Client.LOGGER.setLevel(Level.WARNING);
//            Cli view = new Cli();
//            ClientController clientcontroller = new ClientController(view);
//            view.addObserver(clientcontroller);
//            view.init();
//        } else {
//            //Application.launch(GuiJavaFX.class);
//        }
//    }

    public static void main(String[] args) {

        int cliParam = 0; // default value
        for (String arg : args) {
            if (arg.equals("--cli") || arg.equals("-c")) {
                cliParam = 1;
                break;
            }
            else if(arg.equals("--gui")){
                cliParam=2;
                break;
            }
        }

        if (cliParam == 1) {
            Client.LOGGER.setLevel(Level.WARNING);
            Cli view = new Cli();
            ClientController clientcontroller = new ClientController(view);
            view.addObserver(clientcontroller);
            view.init();
        } else if(cliParam==2) {
            Application.launch(GuiJavaFX.class);
        }
        else{
            int serverPort = 16847; // default value
            GameController gameController = new GameController();
            Server server = new Server(gameController);

            SocketServer socketServer = new SocketServer(server, serverPort);
            Thread thread = new Thread(socketServer, "socketserver_");
            thread.start();
        }
    }

}
