import args.cmd.*;

public class Main {
    private static final Options[] opts= {
        new Options("-mp", 1, (e, s)->{
            e.setManagerPort(Integer.parseInt(s[0]));
            return true;
        }),
        new Options("-mt", 1, (e, s)->{
            e.setManagerToken(s[0]);
            return true;
        }),
        new Options("-p", 1, (e, s)->{
            e.setClientPort(Integer.parseInt(s[0]));
            return true;
        }),
        new Options("-j", 1, (e, s)->{
            e.setWaitingThreads(Integer.parseInt(s[0]));
            return true;
        }),
        //in access file and blacklist, each token is divided by \n
        //access file has higher priority than blacklist
        //if neither is given, then anyone can log in to the server
        new Options("--access", 1, (e, s)->{
            return e.setAccessFile(s[0]);
        }),
        new Options("-bl", 1, (e, s)->{
            return e.setBlacklist(s[0]);
        })
    };
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}