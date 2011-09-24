/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newswebbrowser;

import java.awt.Container;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author jenhan
 */
public class NewsWebBrowser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Container content = frame.getContentPane();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DisplayGraph dg = new DisplayGraph();
        content.add(dg);
        dg.addArticle("Sports", "tennis");
        dg.addArticle("Sports", "boxing");
        dg.addArticle("Sports", "rowing");
        frame.pack();
        frame.setVisible(true);
    }
}
