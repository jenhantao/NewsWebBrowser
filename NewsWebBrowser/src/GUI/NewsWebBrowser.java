/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Container;
import javax.swing.JFrame;

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

        content.add(new DisplayGraph());
        frame.pack();
        frame.setVisible(true);    }
}
