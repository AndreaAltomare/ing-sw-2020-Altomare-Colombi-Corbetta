package it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses;

import it.polimi.ingsw.view.clientSide.View;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton.BodyPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton.TitlePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GUIPlaying extends GUIStatusViewer {

    StatusViewer myStatusViewer;

    public GUIPlaying(StatusViewer statusViewer) {
        myStatusViewer = statusViewer;
    }

    public boolean hasJPanel() {
        return false;
    }

    public boolean hasDirectFrameManipulation(){ return true; }

    public void directFrameManipulation() {
        JFrame frame = new JFrame("Playing board");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel content = (PlayingPanel)getJPanel();

        frame.add(content);
        frame.setSize(425, 200);
        frame.setVisible(true);

    }

    public JPanel getJPanel(){
        return new PlayingPanel();
    }

    public class LayerItem extends JLabel{

        public LayerItem(String s){
            super(s);
        }

        public LayerItem(){
            this.addMouseMotionListener(new MouseAdapter(){
                @Override
                public void mouseDragged(MouseEvent evt){
                    lblMouseDragged(evt);
                }
            });
        }

        public void lblMouseDragged(MouseEvent evt){
            System.out.println("Here");
        }
    }

    private class PlayingPanel extends JPanel{

        JPlayer jPlayer = new JPlayer();


        private Dimension hResizeTo(double originalH, double originalW, double newDimH, Dimension dimension){
            Dimension ret = new Dimension();
            double h, w;

            h = dimension.getHeight()*newDimH;
            w = h * (originalW / originalH);
            if(w<=dimension.getWidth()){
                ret.setSize(w, h);
            }else{
                w = dimension.getWidth();
                h = w*(originalH/originalW);
                ret.setSize(w, h);
            }
            return ret;
        }

        public PlayingPanel(){
            super();
            new TitlePanel(this);
            add(new BodyPanel());
        }

        public void paintComponent(Graphics g){
            super.paintComponent(g);
            //Dimension dimension = myFrame.getContentPane().getSize();
            Dimension dimension = getSize();

            Graphics2D g2d = (Graphics2D) g;

            //background
            try {
                BufferedImage backgroundImg;
                backgroundImg = ImageIO.read(getClass().getResource("/img/background/mainBeckground.png"));
                g2d.setPaint(new TexturePaint(backgroundImg, new Rectangle(dimension.width, dimension.height)));
                g2d.fill(new Rectangle2D.Double(0,0,dimension.width, dimension.height));
            } catch (IOException e) {
                if(View.debugging)
                    e.printStackTrace();
            }




            //board
            try {
                BufferedImage board;
                board = ImageIO.read(getClass().getResource("/img/board/boardScalata.png"));
                Dimension boardDim = hResizeTo(board.getHeight(), board.getWidth(), 0.6, dimension);
                g2d.drawImage(board, (int) (dimension.getWidth() - boardDim.getWidth()) / 2, (int)(dimension.getHeight() - boardDim.getHeight())/2, (int) boardDim.getWidth(), (int) boardDim.getHeight(), this);
                jPlayer.setBounds((int)(dimension.getWidth()+boardDim.getWidth())/2, (int)(dimension.getHeight() - boardDim.getHeight())/2, (int)(dimension.getWidth()-boardDim.getWidth())/2, (int) boardDim.getHeight());
                //add(jPlayer);
            } catch (IOException e) {
                if(View.debugging)
                    e.printStackTrace();
            }





            /*g2d.setPaint( new GradientPaint(5, 30, Color.BLUE, 35, 100, Color.YELLOW, true));
            g2d.fill(new Ellipse2D.Double(dimension.width/2, dimension.height/2, 65, 100));*/
        }
    }

    class JPlayer extends JPanel{

        private final int myBorders = 5;

        public void paintComponent(Graphics g){
            super.paintComponent(g);

            setOpaque(false);

            Dimension dimension = getSize();

            Graphics2D g2d = (Graphics2D) g;

            g2d.setPaint(Color.DARK_GRAY);
            g2d.fill(new RoundRectangle2D.Double(myBorders, myBorders, dimension.getWidth(), dimension.getHeight(), 50, 50));



        }
    }
}
