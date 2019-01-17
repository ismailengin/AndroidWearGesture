package com.example.ksi.androidweargesture;

import android.util.Log;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

/**
 * Created by Ksi on 1.12.2018.
 */

class Infer {
    private static Infer infer;

    private final Queue queue = Queue.getInstance();
    private final State state = State.getInstance();
    private final TensorFlowInferenceInterface inferenceInterface;


    private Infer() {
        inferenceInterface = new TensorFlowInferenceInterface(MainActivity.getAssetManager(), "file:///android_asset/frozen_model.pb");
    }

    static Infer getInstance() {
        if(infer == null) {
            infer = new Infer();
        }

        return infer;
    }

    void inferGesture() {
        //System.out.println("Gesture Inference ediliyor.");
        float[] outputScores = new float[15];
        float[] input = queue.getRecord();
        //System.out.println(input);
        int predicted = 0;
        if(input != null) {
            inferenceInterface.feed("input_input_1", input, 1, Queue.MAX_ELEMENTS);
            inferenceInterface.run(new String[]{"output_1/Softmax"});
            inferenceInterface.fetch("output_1/Softmax", outputScores);
            float prob = 0.f;
            for (int i = 0; i < outputScores.length; i++) {
                if (prob < outputScores[i]) {
                    predicted = i;
                    prob = outputScores[i];
                }
            }
        }
        if(predicted == 7 || predicted == 8) {
            return;
        }
        if(predicted != 0 ) {
            Log.i("Predicted value:", Integer.toString(predicted));
        }
        state.toState(predicted);
    }


}
