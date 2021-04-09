package sample;

import java.util.ArrayList;

public class ResPredict {


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public String getConfidence_score() {
        return confidence_score;
    }

    public void setConfidence_score(String confidence_score) {
        this.confidence_score = confidence_score;
    }

    public String message;

    public ArrayList<String> getPrediction() {
        return prediction;
    }

    public void setPrediction(ArrayList<String> prediction) {
        this.prediction = prediction;
    }

    public ArrayList<String> prediction;
    public String confidence_score;
    public String error;

}
