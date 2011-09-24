/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import Structure.Article;
import java.util.ArrayList;
import javax.swing.JList;
import javax.swing.SwingWorker;

/**
 *
 * @author Bo
 */
public class ArticleListWorker extends SwingWorker{
    private Article article;
    private JList list;

    public ArticleListWorker(Article a, JList articleList){
        this.article = a;
        this.list = articleList;
    }

    @Override
    protected Object doInBackground() throws Exception {
        this.article.generateWeights();
        ArrayList<Article> top = this.article.sortIntoRelated();
        Object[] newArticles = new Object[top.size()];
        for (int j = 0; j < top.size(); j++){
            newArticles[j] = new ArticleListItem(top.get(j));
        }

        this.list.setModel(null);
        this.list.setListData(newArticles);
        this.list.repaint();

        return null;
    }

}
