# harbor

A [diceware](http://world.std.com/~reinhold/diceware.html)-like password generator for your command prompt.  
__Disclaimer:__ This is a _hobbyist_ project, I do not guarantee the passwords generated are crypto-secure or anything like that.

## Installation

### Option 1, Download A Release
1. Head over to the [releases](https://github.com/SalvatoreTosti/harbor/releases) page.
2. Download *harbor\_bin.zip*
3. Extract it, then move the binary file into a directory on your PATH. (I use ~/bin/harbor)  
If you downloaded the *harbor\_bin.zip*, extract and move the JAR into a directory on your PATH.
Then add the following line to the .bashrc file in your HOME directory: `harbor(){ java -jar PATH_TO_JAR_HERE  "$@"; }`
4. Set sail, you're done! :boat:

### Option 2, Generate A Binary File
1. Generate a binary file using the leiningen `lein bin` command (requires [lein-bin](https://github.com/Raynes/lein-bin)).
2. Move the binary file into a directory on your PATH.
3. Cast off, you're done! :boat:

### Option 3, Generate A JAR
1. Generate a JAR using the [Leiningen](https://leiningen.org/) `lein uberjar` command.
2. Move the jar into a directory on your PATH
3. Add the following line to the .bashrc file in your HOME directory: `harbor(){ java -jar PATH_TO_JAR_HERE  "$@"; }`
4. Weigh anchor, you're done! :boat:

## Usage

The `harbor` command takes a single argument, the number of words you want to generate.
Using `harbor 2` will generate a 2 word passphrase like _moore roar_.  
If no argument is specified, harbor will default to a 5 word passphrase.

## Options

- `--help` Help information
- `--version` Version information
- `--length LENGTH` Length of passphrase sequence
- `--repeat COUNT` Generates COUNT number of sequences
- `--special COUNT` Inserts a COUNT number of special characters into a sequence
- `--number COUNT` Inserts a given number of numeric characters into a sequence
- `--capital COUNT` Capitalizes a given number of characters in the generated sequence
- `--wait COUNT` Specifies the number of seconds a generated password will remain in the clipboard

Have an idea for an option? File an [issue](https://github.com/SalvatoreTosti/harbor/issues)!

## License

MIT License
