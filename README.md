# harbor

A diceware-like password generator for your command prompt.  
__Note:__ This is a _hobbyist_ project, I do not guarantee the passwords generated are crypto-secure or anything like that.

## Installation

1. Generate a JAR using the leiningen `lein uberjar` command.  
2. Move the jar into a static location (I use ~/bin/harbor)  
3. Add the following line to the .bashrc file in your home directory: `harbor(){ java -jar ~/bin/harbor/harbor.jar "$1"; }`

## Usage

The `harbor` command takes a single argument, the number of words you want to generate.
Using `harbor 2` will generate a 2 word passphrase like _moore roar_.

## Options

## Examples

### Bugs

## License
