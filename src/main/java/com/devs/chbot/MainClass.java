package com.devs.chbot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class MainClass {
    public static void main(String[] args) {
        String token = null;
        try {
            char[] tokens;
            token = new BufferedReader(new FileReader("token")).readLine(); //only take the 0th line to deal with trailing newlines
        }catch (IOException e) {
            System.out.println("please create a readable file called \"token\".");
        }
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        api.addMessageCreateListener(event -> {
            //do something.
        });
    }
}
