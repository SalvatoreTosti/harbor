# harbor

A ![diceware](http://world.std.com/~reinhold/diceware.html)-like password generator for your command prompt.  
__Disclaimer:__ This is a _hobbyist_ project, I do not guarantee the passwords generated are crypto-secure or anything like that.

## Installation

### Option 1, Generate A JAR
1. Generate a JAR using the ![leiningen](https://leiningen.org/) `lein uberjar` command.  
2. Move the jar into a static location (I use ~/bin/harbor)  
3. Add the following line to the .bashrc file in your HOME directory: `harbor(){ java -jar ~/bin/harbor/harbor.jar "$1"; }`

### Option 2, Generate A Binary File
1. Generate a binary file using the leiningen `lein bin` command (requires ![lein-bin](https://github.com/Raynes/lein-bin)).
2. Move the binary file into a directory on your PATH.
3. You're done!  :boat:

## Usage

The `harbor` command takes a single argument, the number of words you want to generate.
Using `harbor 2` will generate a 2 word passphrase like _moore roar_.

## Options
There are none right now, file an issue if you have an idea for one! 

## License

MIT License
