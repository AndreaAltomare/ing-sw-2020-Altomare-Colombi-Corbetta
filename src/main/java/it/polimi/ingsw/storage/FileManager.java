package it.polimi.ingsw.storage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class's aim is to provide a unique entry point
 * by which the software is able to properly communicate
 * with the File System and the persistent data themselves
 *
 * A Singleton Pattern is applied to enable access control to
 * resources which could be shared among different parts of
 * the application
 *
 * The use of this class is to be intended when enabling
 * multi-user games: access to persistent resources
 * needs to be carefully controlled.
 *
 * @author AndreaAltomare
 */
public class FileManager {
    private static FileManager fileManagerIstance; // may this class be synchronized upon this static attribute
    private String filePath;

    private FileManager() {}

    /**
     * Get the single istance by using the single
     * entry point to access the object resource
     *
     * @return fileManagerIstance (Singleton istance)
     */
    public static FileManager getIstance() {
        if(fileManagerIstance == null)
            fileManagerIstance = new FileManager();

        return fileManagerIstance;
    }

    /**
     * Save a new Card into file by passing a JSON-formatted
     * string
     *
     * @param cardName (Card's name)
     * @param jsonData (JSON data for the Card's power, to write on File)
     */
    public void saveNewCard(String cardName, String jsonData) {
        filePath = cardName + ".config";
        File cardFile;
        FileWriter fw;
        BufferedWriter bw;

        // create a new file and write on it
        try {
            cardFile = new File(filePath);
            fw = new FileWriter(cardFile);
            bw = new BufferedWriter(fw);
            bw.write(jsonData);
            bw.flush();
            bw.close();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Special method to get a Card's information just by specifying its name
     *
     * @param cardName (Card's name)
     * @return JSON data read from File
     */
    public String getCard(String cardName) {
        String jsonData = "";
        filePath = cardName + ".config";

        try {
            jsonData = getFileContent(filePath);
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        return jsonData;
    }

    /**
     * General method to get information from File
     *
     * @param fileName (File name)
     * @return File stored information
     * @throws FileNotFoundException (Exception thrown when specified file does not exist)
     */
    public String getFileContent(String fileName) throws FileNotFoundException {
        String data = "";
        File file;
        FileReader fr;
        BufferedReader br;

        try {
            file = new File(fileName);
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            data = br.readLine();
            br.close();
        }
        catch (FileNotFoundException ex) {
            throw new FileNotFoundException();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        return data;
    }

    /**
     * This method is used when a File content is split up into
     * more than one line.
     * So this method returns a List containing every line in a
     * given File.
     *
     * @param fileName (File name)
     * @return File stored information (every line in a List of String)
     * @throws FileNotFoundException (Exception thrown when specified file does not exist)
     */
    public List<String> getFileRecords(String fileName) throws FileNotFoundException {
        List<String> fileLines = new ArrayList<>();
        String data = "";
        File file;
        FileReader fr;
        BufferedReader br;

        try {
            file = new File(fileName);
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            // Read every line of the File until the end od file
            while((data = br.readLine()) != null)
                fileLines.add(data);

            br.close();
        }
        catch (FileNotFoundException ex) {
            throw new FileNotFoundException();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        return fileLines;
    }
}
