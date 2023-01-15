package args.cmd;

import env.*;

public class Options {
    final private String arg;
    //argc is 0 if no arg required
    final private int argc;
    final private EnvSetter envSetter;
    public Options(String _arg, int _argc, EnvSetter _setter)
    {
        arg = _arg;
        argc = _argc;
        envSetter = _setter;
    }
    //return -1 in case of arg != argv[i]
    //return -2 in case of setting env fail
    //otherwise return the next i
    public int testOptions(int p, String[] argv, EnvBuilder env)
    {
        if(arg.equals(argv[p]))
        {
            String[] args = new String[argc];
            System.arraycopy(argv, p + 1, args, 0, argc);
            if(envSetter.setenv(env, args))
                return p + argc + 1;
            return -2;
        }
        return -1;
    }
}
