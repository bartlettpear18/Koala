package com.company;

/*
You need the display to show without the server having connected.
After that tune the movement of the mouse.
You may also want to play around with scrolling and zooming, but that's last.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static java.lang.Thread.sleep;
import static javafx.geometry.Pos.CENTER;

/**
 * Created by Joel.Bartlett18 on 10/12/2017.
 */
public class Main extends Application {

    private Text address = new Text();
    private VBox vbox = new VBox();
    private Font font = new Font(50);
    static Server server = null;
    static Mouse mouse = null;


    public static void main(String[] args) throws IOException, InterruptedException, AWTException {

        Background obj = new Background();
        Thread thread = new Thread(obj);
        thread.setDaemon(true);
        thread.start();
        launch(args);

    }

    //Initiates server connection in worker thread
    static class Background implements Runnable{
        @Override
        public void run() {
            try {
                server = new Server();
                mouse = new Mouse();
                server.init();

                while(true) {

                    //Store transmitted data
                    boolean storeLeft = server.getBoolState();
                    System.out.println("" + storeLeft);
                    boolean storeRight = server.getBoolState();
                    double storeX = server.getDoubleDisplacement() * 100;
                    double storeY = server.getDoubleDisplacement() * 100;

                    //Handle left
                    if(storeLeft) { mouse.pressLeft(); } else { mouse.releaseLeft(); }
//
//                    //Handle right
//                    if(storeRight) { mouse.pressRight(); } else { mouse.pressRight(); }
//
                    //Handle displacememnts
//                    System.out.println("x: " + storeX + " y: " + storeY);
                    Thread.sleep(10);
//                    mouse.move((int) storeX, (int) storeY);
                }
            } catch (IOException | AWTException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void init() throws UnknownHostException {
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(CENTER);
        vbox.setStyle("-fx-background-color: #007991;");

        address.setTextAlignment(TextAlignment.CENTER);
        address.setText("Device IP Address: \n" + getIp());
        address.setFont(font);
        address.setFill(Paint.valueOf("FFFFFF"));

        vbox.getChildren().add(address);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Scene scene = new Scene(vbox, 600, 300);
        stage.setTitle("Koala Mouse");

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });

        stage.setScene(scene);
        stage.show();

    }

    /**
     * Find IP address of device
     * @return
     * @throws UnknownHostException
     */
    private String getIp() throws UnknownHostException {
        String tmp = String.valueOf(InetAddress.getLocalHost());
        String ip = "";
        for (int i= 0; i < tmp.length(); i++) {
            if (Character.isDigit(tmp.charAt(i)) || Character.toString(tmp.charAt(i)).equals(".")) {
                ip += tmp.charAt(i);
            }
        }

        return ip;
    }
}

