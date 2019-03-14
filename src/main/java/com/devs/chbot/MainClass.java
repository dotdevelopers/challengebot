package com.devs.chbot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;

public class MainClass {
    private final static String token;
    private final static String db_Username;
    private final static String db_Password;
    private final static boolean isMysql; //mysql if true, mariadb if false
    private final static String host;
    private final static short port;
    private final static String db_name;

    static {
        token = System.getenv("chbot_token");
        db_Username = System.getenv("chbot_SQL_name");
        host = System.getenv("chbot_SQL_host");
        db_name = System.getenv("chbot_SQL_db");

        String portTMP = System.getenv("chbot_SQL_port");
        String isMysqlTMP = System.getenv("chbot_mysql");
        String db_PasswordTMP = System.getenv("chbot_SQL_pass");

        db_Password = db_PasswordTMP == null ? "" : db_PasswordTMP;
        port = Short.parseShort(portTMP);
        isMysql = isMysqlTMP.equalsIgnoreCase("true") || isMysqlTMP.equalsIgnoreCase("t") || isMysqlTMP.equalsIgnoreCase("y") || isMysqlTMP.equalsIgnoreCase("yes");

        if (token == null || db_Username == null || host == null || db_name == null) {
            System.out.println("at least one of the environment variables could not be found. If you're on a unix system, check for capitalisation.");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        final Connection c;
        try {
            c = DriverManager.getConnection("jdbc:" + (isMysql ? "mysql" : "mariadb") + "://" + host + ":" + port + "/" + db_name, db_Username, db_Password);
        }catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        api.addMessageCreateListener(event -> {
            String command = event.getMessageContent();
            if (command.toLowerCase().startsWith("!submit")) {
                String[] arguments = command.split("\\s+", 2);
                if (arguments.length == 2) { //first element is !submit, second is the submitted text.
                    try {
                        PreparedStatement ps = c.prepareStatement("INSERT IGNORE INTO pendingsubmissions(Username, Text) VALUES(?, ?)");
                        ps.setString(1, event.getMessageAuthor().getDisplayName());
                        ps.setString(2, arguments[1]);
                        ps.execute();
                        sendFeedbackMessage("successfully submitted!", event.getChannel());
                    }catch (Exception e) {
                        sendFeedbackMessage("Unfortunately, something went wrong while trying to insert your submission into the database.", event.getChannel());
                    }
                }else {
                    sendFeedbackMessage("Usage: ```\n!submit <text or link to submit>```", event.getChannel());
                }
                new Thread(new DeleteMessageAfterTime(3000, event.getMessage())).start();
                return;
            }
        });
    }

    private static void sendFeedbackMessage(String s, TextChannel c) {
        c.sendMessage(s).whenCompleteAsync((msg, exception) -> new Thread(new DeleteMessageAfterTime(3500, msg)).run());
    }
}
