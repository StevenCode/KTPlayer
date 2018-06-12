package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ActivityMain extends MainActivityFrameWork{
    @FXML
    private AnchorPane window;

    @FXML
    protected void openFile(ActionEvent actionEvent) {
        showOpenFileDialog();
    }

    @FXML
    protected void openHelp(ActionEvent actionEvent) {

    }
    @FXML
    protected void refreshList(ActionEvent actionEvent) {

    }

    @FXML
    protected void onDestroyed(ActionEvent actionEvent) {

    }

    @FXML
    protected void prevSong(ActionEvent actionEvent) {

    }

    @FXML
    protected void playMusic(ActionEvent actionEvent) {

    }

    @FXML
    protected void stopPlaying(ActionEvent actionEvent) {

    }

    @FXML
    protected void nextSong(ActionEvent actionEvent) {

    }

    @FXML
    protected void showOpenFileDialog() {
        openFile(getChooser().showOpenDialog(window.getScene().getWindow()));
    }

    @Override
    protected void openFile(@NotNull File file) {super.openFile(file);}
}
