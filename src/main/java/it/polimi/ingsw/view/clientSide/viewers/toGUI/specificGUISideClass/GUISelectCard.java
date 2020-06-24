package it.polimi.ingsw.view.clientSide.viewers.toGUI.specificGUISideClass;

import it.polimi.ingsw.view.clientSide.View;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.clientSide.viewers.cardSelection.CardSelection;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelComponent;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelImageButton;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.BackgroundPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.SubPanel;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.EndCardSelectionException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUISelectCard {

    private static CardSelection cardSelection;
    private static BackgroundPanel godImage;
    private static ImagePanel powerImage;
    private static JPanel background;
    private static JLabel godName;
    private static JLabel godEpitheth;
    private static JLabel godPower;
    private static PanelImageButton selectButton;

    private static JFrame window;


    private static String buildImageBigGodString(String godName){
        return "/img/godBigImage/big" + godName + ".png";
    }

    private static String buildImagePowerString(String godName){
        return "/img/godPowers/" + godName + ".png";
    }

    private static void changeGodImage(String godName){
        try {
            godImage.setBackgroundImg(buildImageBigGodString(godName));
            if(!godImage.isSetImg()) godImage.setBackgroundImg(buildImageBigGodString("Default"));
        }catch (Exception e){
            godImage.setBackgroundImg(buildImageBigGodString("Default"));
        }
    }

    private static void changePowerImage(String godName){
        try {
            powerImage.setBackgroundImg(buildImagePowerString(godName));
            if(powerImage.isNotSetImg()) powerImage.setBackgroundImg(buildImagePowerString("Default"));
        }catch (Exception e){
            powerImage.setBackgroundImg(buildImagePowerString("Default"));
        }
    }

    private static void init(){
        godImage = new BackgroundPanel(buildImageBigGodString("Default"));
        background = new ImagePanel(1, 1, 0, 0, "/img/background/background_select_card.png");
        godImage.add(background);






        JButton forwardButton = new JButton();
        JButton backButton = new JButton();

        JPanel forwardButtonPanel = new PanelImageButton(0.0833, 0.057, 0.9166, 0.4375, forwardButton, "/img/trappings/forward_button.png", "next");
        forwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setGod(cardSelection.next());
            }
        });
        background.add(forwardButtonPanel);

        JPanel backButtonPanel = new PanelImageButton(0.0833, 0.057, 0, 0.4375, backButton, "/img/trappings/back_button.png", "next");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setGod(cardSelection.prev());
            }
        });
        background.add(backButtonPanel);

        JButton selButton = new JButton();
        selectButton = new PanelImageButton(1, 0.097, 0, 0.903, selButton, "/img/trappings/blueButton.png", "Seleziona");
        selButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ViewCard tmp = cardSelection.getCurrent();
                if(cardSelection.isSelected(tmp)){
                    cardSelection.remove(tmp);
                }else{
                    try {
                        cardSelection.add(tmp);
                    } catch (WrongParametersException | CannotSendEventException e) {
                        if(View.debugging)
                            e.printStackTrace();
                        ViewMessage.populateAndSend(e.getMessage(), ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
                    } catch (EndCardSelectionException e) {
                        window.dispose();
                        return;
                    }
                }
                setGod(tmp);
            }
        });
        background.add(selectButton);

        SubPanel contentPanel = new SubPanel(1, 1, 0, 0);
        contentPanel.setOpaque(false);
        background.add(contentPanel);



        godName = new JLabel("");
        godName.setFont(new Font("Serif", Font.ITALIC,20));
        godName.setForeground(Color.YELLOW);

        JPanel godNamePanel = new PanelComponent(0.5, 0.8, 0.25, 0, godName);
        JPanel godNameImgPanel = new ImagePanel(0.52, 0.1215, 0.0521, 0.052, "/img/trappings/god_name.png");
        godNameImgPanel.add(godNamePanel);
        godNamePanel.setOpaque(false);
        contentPanel.add(godNameImgPanel);

        powerImage = new ImagePanel(0.3333, 0.116, 0.666, 0.6, buildImagePowerString("Default"));
        contentPanel.add(powerImage);

        godEpitheth = new JLabel("");
        godName.setFont(new Font("Serif", Font.ITALIC,20));
        godName.setForeground(Color.YELLOW);

        JPanel godEpithethPanel = new PanelComponent(0.75, 0.8, 0.125, 0, godEpitheth);
        JPanel godEpithethImgPanel = new ImagePanel(0.52, 0.09, 0.0521, 0.7153, "/img/trappings/epitheth_button.png");
        godEpithethImgPanel.add(godEpithethPanel);
        godEpithethPanel.setOpaque(false);
        contentPanel.add(godEpithethImgPanel);

        godPower = new JLabel("");
        godPower.setFont(new Font("Serif", Font.ITALIC,10));
        godPower.setForeground(Color.GREEN);
        JPanel godPowerImagePanel = new ImagePanel(0.32, 0.27778, 0.677, 0.139, "/img/background/pergamena.png");
        JPanel godPowerPanel = new PanelComponent(0.9, 0.9, 0.05, 0.05, godPower);
        godPowerPanel.setOpaque(false);
        godPowerImagePanel.add(godPowerPanel);
        contentPanel.add(godPowerImagePanel);

    }

    private static void setGod(ViewCard god){
        godName.setText(god.getName());
        godEpitheth.setText(god.getEpiteth());
        //godEpitheth.setText(god.getDescription());
        godPower.setText("<html>"+ god.getDescription() +"</html>");
        //godPower.setText( god.getEpiteth() );
        if(cardSelection.isSelected(god)){
            selectButton.setButtonImg("/img/trappings/redButton.png", "remove");
        }else{
            selectButton.setButtonImg("/img/trappings/greenButton.png", "select");
        }
        changeGodImage(god.getName());
        changePowerImage(god.getName());
    }

    public static void attivate(CardSelection _cardSelection) {
        cardSelection = _cardSelection;

        if(!cardSelection.hasNext()){
            cardSelection = null;
            return;
        }
        init();

        window = new JFrame("Select Cards");
        window.add(godImage);
        //window.setSize(450, 675);
        window.setSize(400, 600);
        window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        window.setResizable(false);
        window.setVisible(true);
        setGod(cardSelection.getCurrent());
    }



}
