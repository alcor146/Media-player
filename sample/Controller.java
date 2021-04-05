package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private MediaPlayer mediaPlayer;
    private String filePath;

    @FXML
    private MediaView mediaView;

    @FXML
    private Slider slider;

    @FXML
    private Slider seekSlider;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select and mp4 file", "*.mp4"); //create the document filter for MP4 files
            fileChooser.getExtensionFilters().add(filter);
            File file = fileChooser.showOpenDialog(null);   //choose the file
            filePath = file.toURI().toString(); //open from path
            if(filePath != null) {
                Media media = new Media(filePath);
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);  //setup media
                DoubleProperty width = mediaView.fitWidthProperty();
                DoubleProperty height = mediaView.fitHeightProperty();

                width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));  //bind the dimensions of the video to the player
                height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

                slider.setValue(mediaPlayer.getVolume() * 100);
                slider.valueProperty().addListener(new InvalidationListener() {
                    @Override
                    public void invalidated(Observable observable) {
                        mediaPlayer.setVolume(slider.getValue()/100);
                    } //setup volume slider
                });

                mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {  //go to to a point in a video based on what moment you click on the duration bar
                    @Override
                    public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                        seekSlider.setValue(newValue.toSeconds());
                    }
                });

                seekSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {   //go to that point by clicking
                    @Override
                    public void handle(MouseEvent event) {
                        mediaPlayer.seek(Duration.seconds(seekSlider.getValue()));
                    }
                });

                mediaPlayer.setOnReady(new Runnable() { //setup the duration bar to be able to access the whole video
                    @Override
                    public void run() {
                        Duration total = media.getDuration();
                        seekSlider.setMax(total.toSeconds());
                    }
                });

                mediaPlayer.play();
            }

    }

    @FXML
    private void pauseVideo(ActionEvent event) {
        mediaPlayer.pause();
    }

    @FXML
    private void playVideo(ActionEvent event) {
        mediaPlayer.play();
        mediaPlayer.setRate(1);
    }

    @FXML
    private void stopVideo(ActionEvent event) {
        mediaPlayer.stop();
    }

    @FXML
    private void fastVideo(ActionEvent event) {
        mediaPlayer.setRate(1.5);
    }

    @FXML
    private void fasterVideo(ActionEvent event) {
        mediaPlayer.setRate(2);
    }

    @FXML
    private void slowVideo(ActionEvent event) {
        mediaPlayer.setRate(.75);
    }

    @FXML
    private void slowerVideo(ActionEvent event) {
        mediaPlayer.setRate(.5);
    }

    @FXML
    private void exitVideo(ActionEvent event) {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
