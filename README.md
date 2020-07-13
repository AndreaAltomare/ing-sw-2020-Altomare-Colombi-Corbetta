![Grade A+](https://img.shields.io/badge/Grade-A%2B-green?color=009F00)
# Prova Finale Ingegneria del Software 2020
# ing-sw-2020-Altomare-Colombi-Corbetta

This is one of the three project for BSc thesis at **Politecnico di Milano**.  
The related course for this project is _"Ingegneria del Software"_ (i.e. Software Engineering).

Aim of this project is to design, develop, test and deploy a distributed application: a digital version of the board game "_Santorini_" from Roxley Games.

**IMPORTANT:** WE DO NOT OWN ANY OF THE RESOURCES USED TO REPRODUCE THIS GAME. Every resource, from the rule set to the images, belong to **Roxley Games** and the game creators.  
What we own is the code we wrote the software with.

More info about the software design and architecture can be found here: [documentation](Deliverables/Communication_protocol.pdf)  
Other project resources such as UMLs, Javadoc, test coverage report can be found into **Deliverables** folder.

## Gruppo AM42


- ###   10608961    Andrea Altomare ([@AndreaAltomare](https://github.com/AndreaAltomare))<br>andrea1.altomare@mail.polimi.it
- ###   10631973    Marco Colombi ([@MarcoColombi](https://github.com/MarcoColombi))<br>marco3.colombi@mail.polimi.it
- ###   10570080    Giorgio Corbetta ([@giocor98](https://github.com/giocor98))<br>giorgio1.corbetta@mail.polimi.it

| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Complete rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Socket | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| GUI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| CLI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Multiple games | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Persistence | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Advanced Gods | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Undo | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |

<!--
[![RED](https://placehold.it/15/f03c15/f03c15)](#)
[![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#)
[![GREEN](https://placehold.it/15/44bb44/44bb44)](#)
-->

## Project lifecycle

This is a **Maven project**, so its lifecycle can be entirely run with Maven and its plugins.

To execute (the main) Maven goals, just run the following commands:

- Clean: 
```
mvn clean
```
- Compile:
```
mvn compile
```
- Test:
```
mvn test
```
- Package creation:
```
mvn clean compile assembly:single
```

Bear in mind that to create JAR package from the source code, you first need to consider which part do you want to build, so you need to change the POM (`pom.xml` file) and the MANIFEST (`MANIFEST.MF` file) accordingly.
You can find the related files into the **Resources_for_packages** folder into the root of this project.

1. Server
    - POM: Just change the content of the already existent `pom.xml` file with the content of `pom.txt` file you can find into `Resources_for_packages/Server`.
    - MANIFEST: Do the same with the `MANIFEST.MF` file into `src/main/java/META-INF`, change its content with the one of `MANIFEST.TXT` file you can find into `Resources_for_packages/Server` as well.
2. Client
    - POM: Just change the content of the already existent `pom.xml` file with the content of `pom.txt` file you can find into `Resources_for_packages/Client`.
    - MANIFEST: Do the same with the `MANIFEST.MF` file into `src/main/java/META-INF`, change its content with the one of `MANIFEST.TXT` file you can find into `Resources_for_packages/Client` as well.


## How to run the application

This is a cross-platform application, so the following instructions works with all the major operating systems.

**NOTE:** You need to be in `Executables/Server` or `Executables/Client` folder when performing the instructions to run the application.

**WARNING:**
The application was built with JDK *14.0.1*, so you will probably need the corresponding JRE version (or higher) to correctly execute it.

### 1. Start the Server

First you need to start the Server.

You can do it on your computer, on a machine in your LAN, or on a machine connected to the Internet.
Since the Client-Server communication is based on **TCP protocol** you need to specify the PORT on which the connection to your Server will occur (and be sure that you can actually connect to *that specific* port).
You can do this by two ways: either from command line or modifying the `server_settings.config` file into `connection_settings` folder:
```
{
  "ip": "",
  "port": 9999
}
```
By default, if no port is specified nor connection setting file is found, the Server will listen to PORT `9999`.

Now you can run the Server just by typing this command on your terminal:
```
java -jar SantoriniCore.jar [port_number]
```

### 2. Start Client(s)

After the first step, you can run one or more Clients to start a game.

You need to know the Server's IP address and PORT on which the application is listening.
You can specify IP and PORT you want either by command line or by modifying the `client_settings.config` file into `connection_settings` folder:
```
{
  "ip": "127.0.0.1",
  "port": 9999
}
```
By default, if no IP address and no port are specified nor connection setting file is found, the Client will connect to the loopback address on 9999 port: `127.0.0.1:9999`.

You can run a Client in two ways:
1. Double-click on `SantoriniClient.jar` will open the GUI.
2. Open the terminal and type
```
java -jar SantoriniClient.jar [--cli]
                              [--terminal]
                              [--gui]
```
These commands will respectively open the CLI, the Terminal, and the GUI version of the Client.
If no *interface-type* argument is specified, the GUI will be opened by default.
You can also modify the IP address and PORT to connect to by typing `-ip` and `-port` arguments.

Usage below:
```
java -jar SantoriniClient.jar [-ip ip_address] [-port port]
```

If you want both specify a certain type of interface and a different IP address and port to connect to, you can just compose the arguments:
```
java -jar SantoriniClient.jar [--cli] [-ip ip_address] [-port port]
                              [--terminal] [-ip ip_address] [-port port]
                              [--gui] [-ip ip_address] [-port port]
```

#### Parameters

- `--cli`: runs Client as a *Command Line Interface*.
- `--terminal`: runs Client as a *Command Line Interface* compatible with Windows Command Prompt.
- `--gui`: runs Client as a *Graphical User Interface* (same as *double-click*).
- `-ip`: lets the user choose the IP address of the Server to connect to.
- `-port`: lets the user choose the Port of the Server to connect to.

**NOTE:** If you want to choose a different IP and/or a different Port to connect to, you need to specify both `-ip` and `-port` arguments anyway.

---

### Final grade: 30 cum Laude / 30
###### Academic Year: 2019/2020
