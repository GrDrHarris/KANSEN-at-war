package manager;

import env.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class RunManager implements Runnable {
    GameEnv gameEnv;
    public RunManager(GameEnv _gameEnv) {
        gameEnv = _gameEnv;
    }
    public void run() {
        try {
            ServerSocket server = new ServerSocket(gameEnv.getManagerPort());
            while(true) {
                System.out.println("waiting for a manage connection.");
                Socket socket = server.accept();
                System.out.println("manager connected, verifying token.");
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String token = gameEnv.getManagerToken();
                int sleepCount = 0, dst = 0;
                char[] read = new char[token.length()];
                while(dst + 1 < token.length() && sleepCount < 150)
                {
                    int n = reader.read(read, dst, token.length() - dst);
                    dst += n;
                    sleepCount++;
                    Thread.sleep(20);
                }
                String s = new String(read);
                if(s.equals(token))
                {
                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                    writer.println("verified");
                    System.out.println("Enter manage function.");
                    do_manage(reader, writer);
                }
                System.out.println("manage socket closing.");
                socket.close();
            }
        } catch (Exception e) {
            System.err.println("manager encountered problem");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void do_manage(BufferedReader reader, PrintWriter writer) {
    }
}
