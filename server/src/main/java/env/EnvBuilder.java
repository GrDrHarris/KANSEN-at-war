package env;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class EnvBuilder {
    private int ManagerPort;
    public void setManagerPort(int port) {
        ManagerPort = port;
    }

    private String ManagerToken;
    public void setManagerToken(String s) {
        ManagerToken = s;
    }

    private int ClientPort;
    public void setClientPort(int port) {
        ClientPort = port;
    }
    private int WaitingThreads;
    public void setWaitingThreads(int count) {
        WaitingThreads = count;
    }

    private File accessFile;
    private Set<String> knownAccess;
    public boolean setAccessFile(String filename) {
        try{
            accessFile = new File(filename);
            if(!accessFile.exists())
                accessFile.createNewFile();
            BufferedReader reader = new BufferedReader(new FileReader(accessFile));
            knownAccess = new HashSet<>();
            String str = null;
            while((str = reader.readLine()) != null)
            {
                knownAccess.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private File Blacklist;
    private Set<String> knownBlacklist;
    public boolean setBlacklist(String filename) {
        try{
            Blacklist = new File(filename);
            if(!Blacklist.exists())
                Blacklist.createNewFile();
            BufferedReader reader = new BufferedReader(new FileReader(Blacklist));
            knownBlacklist = new HashSet<>();
            String str = null;
            while((str = reader.readLine()) != null)
            {
                knownBlacklist.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public GameEnv build()
    {
        return new GameEnv(ManagerPort, ManagerToken, ClientPort, WaitingThreads, accessFile, knownAccess, Blacklist, knownBlacklist);
    }
}
