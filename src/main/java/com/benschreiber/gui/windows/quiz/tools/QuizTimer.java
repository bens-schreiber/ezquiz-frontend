package com.benschreiber.gui.windows.quiz.tools;

import com.benschreiber.gui.windows.quiz.QuizHelper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.io.IOException;

public class QuizTimer {

    private final Label label;
    private int seconds;

    public QuizTimer(Label label, int time) {
        this.label = label;
        this.seconds = time;
    }

    public void startTimer() {

        Timeline time = new Timeline();

        time.setCycleCount(Timeline.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.seconds(1), event -> {

            if (seconds == -1) {

                time.stop();

                try {
                    QuizHelper.endQuiz();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {

                int hours;
                int minutes;
                int second;
                hours = seconds / 3600;
                minutes = (seconds % 3600) / 60;
                second = seconds % 60;

                //Format in hours:minutes:seconds
                label.setText(String.format("%02d:%02d:%02d", hours, minutes, second));
            }

            seconds--;

        });

        time.getKeyFrames().add(frame);
        time.playFromStart();

    }


}
