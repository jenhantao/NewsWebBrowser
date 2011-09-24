package Structure;

import Stream.ArticleBean;
import java.util.ArrayList;
import java.util.Iterator;

import com.sun.cnpi.rss.elements.Item;
import java.util.StringTokenizer;

public class Article {
	public static final int NUMBER_OF_TOP_ARTICLES = 5;
	private ArticleBean bean;
	private Topic parentTopic;
	private int seenBefore;
	private int relativeWeight;
	private ArrayList<Article> relatedArticles;


	/*Constructor
	 * @param: ArticleBean
	 */
	public Article(ArticleBean b, Topic parentTopic){

		seenBefore = 1;
		this.bean = b;
		this.parentTopic = parentTopic;
		relativeWeight = 0;
		relatedArticles = new ArrayList<Article>();
	}

	/*Returns the name of the topic to which the article belongs*/
	public String getSectionName(){
		return bean.get_sectionName();
	}

	/*returns the item object that belongs to the article*/
	public Item getItem(){
		return bean.get_Item();
	}

        public ArticleBean getBean(){
            return this.bean;
        }

        public String getHeadline(){
            return this.bean.get_Item().getTitle().toString();
        }

        public String getDescription(){
            //May need a bit of tweaking
            return this.bean.get_Item().getDescription().toString();
        }

	/*returns the array of keywords of the article*/
	public ArrayList<String> getKeywords() {
		return bean.getKeywords();
	}

	/*Method that contains this article with another*/
	public int compareTo(Article art){
		return 0;
		//TODO
	}
	/*Returns parent topic*/
	public Topic getParentTopic(){
		return parentTopic;
	}

	/*Returns 1 or -1 if not seen or seen respectively*/
	public int seenBefore(){
		return seenBefore;
	}
	/*Marks as seen*/
	public void markAsSeen(){
		seenBefore = -1;
	}
	/*Sets relative weight*/
	public void incrementWeight(){
		relativeWeight++;
	}
	/*Multiply weight by 1 or -1, for the purpose of making the weight
	 * negative if it has been seen before, therefore decreasing its relevance
	 */
	public void multWeightBySeen(){
		relativeWeight = relativeWeight * seenBefore();
	}
	/*returns relative weights*/
	public int getWeight(){
		return relativeWeight;
	}
	/*generated the arraylist of related articles*/
	public void generateWeights(){
		Article current = null;
		Iterator<Article> relativeIterator = parentTopic.getArticles().iterator();
		ArrayList<String> keywords = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(bean.get_Item().getDescription().toString());
		//Tokenize the description into arrayList, remove garbage keywords(e.g the)
		while(tokenizer.hasMoreTokens()){
			String next = tokenizer.nextToken();
			if(next != "the" && next != "a" && next != "and" && next != "for"){
				keywords.add(next);
			}
		}
		while(relativeIterator.hasNext()){
			current = (Article) relativeIterator.next();
			ArrayList<String> keywordsArticle2 = new ArrayList<String>();
			StringTokenizer tokenizer2 = new StringTokenizer(bean.get_Item().getDescription().toString());
			while(tokenizer2.hasMoreTokens()){
				String next = tokenizer2.nextToken();
				if(next != "the" && next != "a" && next != "and" && next != "for"){
					keywordsArticle2.add(next);
				}
			}
			for(int i = 0; i < keywordsArticle2.size();i++){
				for(int k = 0;k < keywords.size();k++){
					if(keywords.get(k) == keywordsArticle2.get(i)){
						current.incrementWeight();
					}
				}
			}
			//current.relativeWeight += 2 - 2/current.parentTopic.returnPopularity();
			current.multWeightBySeen();
		}
		sortIntoRelated();
	}
	public ArrayList<Article> sortIntoRelated(){
		ArrayList<Article> art = this.parentTopic.getArticles();
		Iterator<Article> it = art.iterator();
		Article current = it.next();
		relatedArticles.add(it.next());

		while (it.hasNext()){
			int size = relatedArticles.size();
			for (int i = 0; i < size; i++){
				if(current.getWeight() > relatedArticles.get(i).getWeight()){
					relatedArticles.add(i,current); //adding at the right spot so the list stays sorted
				}
				if (i+1==size){
					relatedArticles.add(current);//adding to the end of the list
				}
			}
			current = it.next();
		}
		ArrayList<Article> toReturn = new ArrayList<Article>();
		for (int i = 0; i<NUMBER_OF_TOP_ARTICLES; i++){
			toReturn.add(relatedArticles.get(i)); //populate the list of the top #NUMBER_OF_TOP_ARTICLES articles
		}
		return toReturn;

	}

}
