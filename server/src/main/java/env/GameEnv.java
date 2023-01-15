package env;

import manager.*;

import java.io.*;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GameEnv {
    private int ManagerPort;
    private String ManagerToken;
    private int ClientPort;
    private int WaitingThreads;
    private File accessFile;
    private Set<String> knownAccess;
    private ReadWriteLock accLock;
    private boolean accessChanged;
    private File Blacklist;
    private Set<String> knownBlacklist;
    private ReadWriteLock blkLock;
    private boolean blacklistChanged;
    public GameEnv(int _ManagerPort, String _ManagerToken, int _ClientPort, int _WaitingThreads, File _accessFile, Set<String> _knownAccess,
                    File _Blacklist, Set<String> _knownBlacklist)
    {
        ManagerPort = _ManagerPort;
        ManagerToken = _ManagerToken;
        ClientPort = _ClientPort;
        WaitingThreads = _WaitingThreads;
        accessFile = _accessFile;
        knownAccess = _knownAccess;
        Blacklist = _Blacklist;
        knownBlacklist = _knownBlacklist;

        accLock = new ReentrantReadWriteLock();
        blkLock = new ReentrantReadWriteLock();

        Thread manager = new Thread(new RunManager(this));
    }
    public boolean checkAccess(String token)
    {
        boolean valid = true;
        if(knownAccess != null)
        {
            accLock.readLock().lock();
            valid = knownAccess.contains(token);
            accLock.readLock().unlock();
        }
        if(!valid)
            return false;
        if(knownBlacklist != null)
        {
            blkLock.readLock().lock();
            valid = !knownBlacklist.contains(token);
            blkLock.readLock().unlock();
        }
        return valid;
    }
    public void addAccess(String token)
    {
        accLock.writeLock().lock();
        accessChanged = true;
        knownAccess.add(token);
        accLock.writeLock().unlock();
    }
    public void removeAccess(String token)
    {
        accLock.writeLock().lock();
        accessChanged = true;
        knownAccess.remove(token);
        accLock.writeLock().unlock();
    }
    public void addBlack(String token)
    {
        blkLock.writeLock().lock();
        blacklistChanged = true;
        knownBlacklist.add(token);
        blkLock.writeLock().unlock();
    }
    public void removeBlack(String token)
    {
        blkLock.writeLock().lock();
        blacklistChanged = true;
        knownBlacklist.remove(token);
        blkLock.writeLock().unlock();
    }
    public void saveList()
    {
        BufferedWriter w = null;
        try {
            if (accessChanged) {
                accLock.writeLock().lock();
                 w = new BufferedWriter(new FileWriter(accessFile));
                 for(String s : knownAccess)
                 {
                     w.write(s);
                     w.newLine();
                 }
                 w.close();
                 accLock.writeLock().unlock();
            }
            if(blacklistChanged) {
                blkLock.writeLock().lock();
                w = new BufferedWriter(new FileWriter(Blacklist));
                for(String s : knownBlacklist)
                {
                    w.write(s);
                    w.newLine();
                }
                w.close();
                blkLock.writeLock().unlock();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public int getManagerPort() {
        return ManagerPort;
    }
    public String getManagerToken() {
        return ManagerToken;
    }
}
