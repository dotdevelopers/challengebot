package com.devs.chbot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class MainClass {
    final static String token;
    final static String db_Username;
    final static String db_Password;
    final static boolean isMysql; //mysql if true, mariadb if false
    final static String host;
    final static short port;
    final static String db_name;

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
            //do something.
        });
    }
}
