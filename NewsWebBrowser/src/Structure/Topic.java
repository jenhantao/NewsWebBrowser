package Structure;

import java.util.ArrayList;

public class Topic {
        private String topicName;
	private ArrayList<Article> articles;
	private int popularity;

	public Topic(){
		popularity = 0;
		articles = new ArrayList<Article>();
	}

        public Topic(String topic) {
            this();
            this.topicName = topic;
        }

	/*Adds article to article arrayList*/
	public void addArticle(Article article){
		articles.add(article);
	}

	/*Populates top article choices in this topic*/
	public ArrayList<Article> populateTopArticles(){
		ArrayList<Article> toReturn = new ArrayList<Article>();
		for(int i = 0;i < 10;i++){
			toReturn.add(articles.get(i));
		}
		return articles;
	}


	/*Returns arrayList with articles*/
	public ArrayList<Article> getArticles(){
		return articles;
	}

	/*returns popularity count*/
	public int returnPopularity(){
		return popularity;
	}

	/*Increments popularity count by one*/
	public void incrementPopularity(){
		popularity++;
	}

        public String getTopicName(){
            return this.topicName;
        }
}
