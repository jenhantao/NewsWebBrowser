/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import Structure.Article;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Bo
 */
public class ArticleListItem extends JPanel{
    private JLabel headline;
    private JLabel description;
    private Article displayArticle;

    public ArticleListItem(Article toDisplay){
        this.headline = new JLabel(toDisplay.getHeadline());
        this.headline.setFont(new Font("Serif", Font.BOLD, 20));
        this.headline.setVisible(true);
        this.description = new JLabel(toDisplay.getDescription());
        this.description.setVisible(true);
        this.setLayout(new BorderLayout());
        this.add(this.headline, BorderLayout.NORTH);
        this.add(this.description, BorderLayout.SOUTH);
        this.displayArticle = toDisplay;
    }

    public ArticleListItem(String headLine, String description){
        this.headline = new JLabel(headLine);
        this.headline.setFont(new Font("Serif", Font.BOLD, 20));
        this.headline.setVisible(true);
        this.description = new JLabel(description);
        this.description.setVisible(true);

        this.setLayout(new BorderLayout());
        this.add(this.headline, BorderLayout.NORTH);
        this.add(this.description, BorderLayout.SOUTH);
    }

    public String getHeadLine(){
        return this.headline.getText();
    }

    public String getDescription(){
        return this.description.getText();
    }

    public Article getDisplayArticle(){
        return this.displayArticle;
    }
}
