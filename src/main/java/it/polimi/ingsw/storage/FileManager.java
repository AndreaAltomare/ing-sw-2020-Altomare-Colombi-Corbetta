package it.polimi.ingsw.storage;

import java.io.*;

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
     * Get a Card's information just by specifying its name
     *
     * @param cardName (Card's name)
     * @return jsonData (JSON data read from File)
     */
    public String getCard(String cardName) {
        String jsonData = "";
        filePath = cardName + ".config";
        File cardFile;
        FileReader fr;
        BufferedReader br;

        try {
            cardFile = new File(filePath);
            fr = new FileReader(cardFile);
            br = new BufferedReader(fr);

            jsonData = br.readLine();
            br.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        return jsonData;
    }
}
