package env;

public interface EnvSetter {
    //returns true if success
    boolean setenv(EnvBuilder e, String[] cmds);
}
