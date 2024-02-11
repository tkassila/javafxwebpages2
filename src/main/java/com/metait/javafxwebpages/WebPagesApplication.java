package com.metait.javafxwebpages;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This javaFx application gets web address from clibboard and is storing those address, keywords
 * and title into a application files. You can also reopen in webview component or in default browser
 * etc.
 */
public class WebPagesApplication extends Application {
    private Stage m_primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WebPagesApplication.class.getResource("webpages-view.fxml"));
        WebPagesController controller = new WebPagesController();
        m_primaryStage = stage;
        controller.setPrimaryStage(m_primaryStage);
        fxmlLoader.setController(controller);
        Parent loadedroot = fxmlLoader.load();
        Scene scene = new Scene(loadedroot, 1000, 714);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                controller.handleKeyEvent(event);
            }
        });
        stage.setTitle("List saved webpages of web browsers");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}