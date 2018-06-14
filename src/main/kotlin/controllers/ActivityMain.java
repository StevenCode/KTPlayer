package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXProgressBar;
import decoders.DecoderInterface;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.Echoer;

import java.io.File;

public class ActivityMain extends MainActivityFrameWork {
    @FXML
    private AnchorPane window;
    @FXML
    private JFXListView<String> propertiesList;
    @FXML
    private JFXListView<String> filesList;
    @FXML
    private JFXButton playButton;
    @FXML
    private JFXProgressBar progressBar;
    private DecoderInterface dekoder;

    @FXML
    protected void openFile(ActionEvent actionEvent) {
        showOpenFileDialog();
    }

    @FXML
    protected void openHelp(ActionEvent actionEvent) {
        openGitHub();
    }

    @FXML
    protected void refreshList(ActionEvent actionEvent) {
        showFilesInTheSamePath(dekoder.getPath());
    }

    @FXML
    protected void onDestroyed(ActionEvent actionEvent) {
        super.onDesTroyed();
    }

    @FXML
    protected void prevSong(ActionEvent actionEvent) {
        super.changeSong(false);
    }

    @FXML
    protected void playMusic(ActionEvent actionEvent) {
        super.playMusic();
    }

    @FXML
    protected void stopPlaying(ActionEvent actionEvent) {
        super.stopPlaying();
    }

    @FXML
    protected void nextSong(ActionEvent actionEvent) {
        super.changeSong(true);
    }

    @Override
    protected void showOpenFileDialog() {
        openFile(getChooser().showOpenDialog(window.getScene().getWindow()));
    }

    @Override
    protected void openFile(@NotNull File file) {
        super.openFile(file);
    }

    @Nullable
    @Override
    public DecoderInterface getDekoder() {
        return dekoder;
    }

    @Override
    public void setDekoder(@Nullable DecoderInterface decoderInterface) {
        dekoder = decoderInterface;
    }

    @NotNull
    @Override
    protected Echoer propertiesPrinter() {
        {
            return msg ->
                    propertiesList.getItems().add(msg);
        }
    }

    @Override
    public void clearPropertiesShown() {
        propertiesList.getItems().remove(0, propertiesList.getItems().size());
    }

    @NotNull
    @Override
    public JFXButton getPlayButton() {
        return playButton;
    }

    @Override
    protected void setProgress(long it) {
        System.out.println("it:"+it*1000.0  / dekoder.getTotalTime());
        progressBar.setProgress(it*1000.0  / dekoder.getTotalTime());
    }

    @NotNull
    @Override
    protected Echoer filesPrinter() {
        return msg -> {
            ObservableList list = filesList.getItems();
            if (!list.contains(msg)) list.add(msg);
        };
    }

    @Override
    protected void clearFilesShown() {
        filesList.getItems().remove(0, filesList.getItems().size());
    }

    @Override
    @FXML
    protected void initialize() {
        super.initialize();
    }
}
