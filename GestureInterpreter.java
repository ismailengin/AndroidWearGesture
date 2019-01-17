package com.example.ksi.androidweargesture;

/**
 * Created by Ksi on 1.12.2018.
 */

public class GestureInterpreter {
    private static GestureInterpreter gestureInterpreter;


    public static GestureInterpreter getInstance() {
        if (gestureInterpreter == null) {
            gestureInterpreter = new GestureInterpreter();
        }

        return gestureInterpreter;
    }

    private GestureInterpreter() {
    }

    public void setGesture(int gesture) {
        if (gesture == 0) {
            return;
        }

        Queue.getInstance().disable();

        System.out.println("GESTURE: " + gesture);

        if (gesture == 1) {
            MainActivity.writeBtSocket("1");
            MainActivity.ChangeText("Flick In");
        } else if (gesture == 2) {
            MainActivity.writeBtSocket("2");
            MainActivity.ChangeText("Flick out");
        } else if (gesture == 9 ) {
            MainActivity.writeBtSocket("4");
            MainActivity.ChangeText("Sag");
        System.out.println("Swipe actions " + gesture);
        } else if (gesture == 13) {
            MainActivity.writeBtSocket("3");
            MainActivity.ChangeText("Yukari");
        }

        Queue.getInstance().enable();
    }

}

