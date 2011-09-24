/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

/**
 *
 * @author Bo
 */
public class HeadlineListCellRenderer implements ListCellRenderer{
    public Component getListCellRendererComponent(JList jlist,
                                                  Object value,
                                                  int cellIndex,
                                                  boolean isSelected,
                                                  boolean cellHasFocus)
    {
       if (value instanceof ArticleListItem){
           Component component = (Component) value;
           component.setForeground (Color.white);
           component.setBackground (isSelected ? UIManager.getColor("Table.focusCellForeground") : Color.white);
           return component;
       }
        else
        {
            return new JLabel("???");
        }
    }
}
