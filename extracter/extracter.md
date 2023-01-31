# extracter

Extracters are small programs for extracting resources from game data files to common formats (png, ogg, mp3, etc.). These programs receive the data file of the origin game and a extracter script from the script file.

The recommend file structer of a extracter program is (assume ```$EXT``` is the name of the extracter):

```json
|- extracter
   |- $EXT
      |- src //source code for the extracter
      |- build.md //how to build the program(helping me build a release package)
      |- script.md //how to write a correct extracter script
      |- use.md //where to put the data file of the game, telling players how use it
      |- other stuff
```

For a player release package, only the binary executable and use.md will be included. The other two document will only be included in a scripter release package.

When the client or server(rare) needs to extract something, it generates a extracter script, assuming its path is ```$SCRIPT_PATH```, and call

```bash
..\extracter\$EXT\$EXT $SCRIPT_PATH
```

In a line of extracter command, the target file name must be given, scripters only need to give out the releative path to ```client/dynamic/```, the client program will extend it into an absolute path automatically.
