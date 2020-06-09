package it.polimi.ingsw.view.clientSide.viewers.toGUI.fonts;

import it.polimi.ingsw.view.clientSide.View;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class MyFont {

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
