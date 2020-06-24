package it.polimi.ingsw.view.clientSide.viewers.toGUI.fonts;

import it.polimi.ingsw.view.clientSide.View;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


//It's not used but it's left here because will be useful in future updates.
/**
 * Class to make it possible to import Font from outer files.
 *
 * The files containing the fonts mus be into the ./src/main/resources/font/
 */
public abstract class MyFont {

    /**
     * Method to return the searched font (imported from outer files).
     *
     * @param fontNameFile ((String) name of the file searched).
     * @return The font searched
     */
    public static Font getFont(String fontNameFile){
        InputStream is = MyFont.class.getResourceAsStream("/font/" + fontNameFile); // TestFont.ttf
        Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            if(View.debugging)
                e.printStackTrace();
            return null;
        }
        return font.deriveFont(12f);
    }

}
