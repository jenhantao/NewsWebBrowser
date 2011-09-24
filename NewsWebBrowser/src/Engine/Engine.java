/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Engine;

import GUI.ArticleListItem;
import GUI.ArticleListWorker;
import GUI.NewsWebBrowserMainWindow;
import Stream.ArticleBean;
import Stream.RSSDataParser;
import Structure.Article;
import Structure.Topic;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javax.swing.text.html.ParagraphView;

/**
 *
 * @author Bo
 */
public class Engine {
    private NewsWebBrowserMainWindow mainWindow;
    private RSSDataParser parser;
    private ArrayList<Topic> topics;
    private ArrayList<String> m_topicNames;

    public Engine(ArrayList<String> topicNames){
        this.parser = new RSSDataParser();
        this.topics = new ArrayList<Topic>();
        this.mainWindow = new NewsWebBrowserMainWindow();
        this.m_topicNames = topicNames;
        setupDataModel();
        System.out.println("1");
        populateDataStructure();
        System.out.println("2");
        //insertDummyValues();
        addArticlesToList();
        System.out.println("3");
        setupActionListeners();
        System.out.println("4");
    }

    public void run(){
        java.awt.EventQueue.invokeLater(new MainWindowRunnable(this.mainWindow));
    }

    private void setupDataModel(){
        for (String topic : this.m_topicNames){
            this.topics.add(new Topic(topic));
        }
    }

    private void populateDataStructure(){
        ArrayList<ArticleBean> result = this.parser.readFile();

        for (ArticleBean current : result){
            String topicName = current.get_sectionName();

            for (int i = 0; i < this.topics.size(); i++){
                Topic currentTopic = this.topics.get(i);
                if (currentTopic.getTopicName().equals(topicName)){
                    currentTopic.addArticle(new Article(current, currentTopic));
                }
            }
        }        
    }

    private void addArticlesToList(){
        int total = 0;

        for (int i = 1; i < this.topics.size(); i+=0){
            Topic currentTopic = this.topics.get(i);
            
            Article currentArticle = currentTopic.getArticles().get(total);
            ArticleListItem currentItem = new ArticleListItem(currentArticle);
            currentItem.setVisible(true);
            this.mainWindow.addElementToList(currentItem);

            total++;

            if (total >= 5){
                break;
            }
        }
        
    }

    private void updateDataModel(){
        ///TO DO
    }

    private void setupActionListeners(){
        JList list = this.mainWindow.getList();
        ListSelectionModel lsm = list.getSelectionModel();
        lsm.addListSelectionListener(new ArticleListSelectionListener(this.mainWindow.getMainDisplay(), this.mainWindow.getList()));
    }

    private void insertDummyValues(){
        ArticleListItem item1 = new ArticleListItem("Entertainment", "asdf");
        item1.setVisible(true);
        this.mainWindow.addElementToList(item1);
        ArticleListItem item2 = new ArticleListItem("Business", "fdeq");
        item2.setVisible(true);
        this.mainWindow.addElementToList(item2);
        ArticleListItem item3 = new ArticleListItem("Sports", "fdeq");
        item3.setVisible(true);
        this.mainWindow.addElementToList(item3);
        ArticleListItem item4 = new ArticleListItem("World News", "fdeq");
        item4.setVisible(true);
        this.mainWindow.addElementToList(item4);
        ArticleListItem item5 = new ArticleListItem("Tech", "fdeq");
        item5.setVisible(true);
        this.mainWindow.addElementToList(item5);
    }

    public static void main(String[] args){
        ArrayList<String> topicNames = new ArrayList<String>();
        topicNames.add("entertainment");
        topicNames.add("business");
        topicNames.add("sports");
        topicNames.add("world news");
        topicNames.add("tech");

        Engine e = new Engine(topicNames);
        e.run();
    }

    class ArticleListSelectionListener implements ListSelectionListener {
        private JPanel mainDisplay;
        private JList articleList;

        public ArticleListSelectionListener(JPanel mainPanel, JList list){
            this.mainDisplay = mainPanel;
            this.articleList = list;
        }

        @Override
        public void valueChanged(ListSelectionEvent lse) {
            mainWindow.switchView();
            ListSelectionModel lsm = (ListSelectionModel)lse.getSource();
            Object[] newElements = new Object[this.articleList.getModel().getSize()];

            for (int i = 0; i < this.articleList.getModel().getSize(); i++){
                if (lsm.isSelectedIndex(i)){
                    ArticleListItem listItem = (ArticleListItem)this.articleList.getModel().getElementAt(i);
                    Article a = listItem.getDisplayArticle();
                   
                    JTextArea textArea = new JTextArea();

                    textArea.setText(parseParagraphTags(listItem.getDisplayArticle().getBean().getM_text()));
                    textArea.setEditable(false);
                    textArea.setLocation(5, 5);
                    textArea.setSize(this.mainDisplay.getSize().width - 10, this.mainDisplay.getSize().height - 10);
                    textArea.setVisible(true);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);

                    this.mainDisplay.removeAll();
                    this.mainDisplay.add(textArea);
                    this.mainDisplay.repaint();

                    a.generateWeights();
                    Article exchange = a.sortIntoRelated().get(i);

                    newElements[i] = new ArticleListItem(exchange);
                }
                else{
                    ArticleListItem listItem = (ArticleListItem)this.articleList.getModel().getElementAt(i);
                    newElements[i] = listItem;
                }
            }

            this.articleList.setModel(new DefaultListModel());
            this.articleList.setListData(newElements);

            this.articleList.repaint();
        }

        public String parseParagraphTags(String taggedString){
            String toRet = taggedString;
            toRet = toRet.replaceAll("<p>", "");
            toRet = toRet.replaceAll("</p>", "\n");

            return toRet;
        }

        public Topic topTopic(){
    	Topic best = null;
    	Iterator<Topic> iter = topics.iterator();
    	while(iter.hasNext()){
    		Topic current = iter.next();
    		if(best == null){
    			best = current;
    		}else{
    			if(current.returnPopularity() > best.returnPopularity()){
    				best = current;
    			}
    		}
    	}
    	return best;
    }
    }
}
