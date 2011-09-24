package Stream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.sun.cnpi.rss.elements.Item;
import com.sun.cnpi.rss.elements.Rss;
import com.sun.cnpi.rss.parser.RssParser;
import com.sun.cnpi.rss.parser.RssParserException;
import com.sun.cnpi.rss.parser.RssParserFactory;

public class RSSDataParser {

	private BufferedReader reader;
	private RssParser parser;

	public RSSDataParser(){
		try {
			this.reader = new BufferedReader(new FileReader(new File("feeds")));
			parser = RssParserFactory.createDefault();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RssParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private BufferedReader readFromURL(String url) throws Exception{
		/*
		return new BufferedReader(
			new InputStreamReader(
				new URL(url).openStream()));
		 */
		URL murl = new URL(url);
		//Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyStr, port));
		URLConnection uc = murl.openConnection();

		return new BufferedReader(new InputStreamReader(uc.getInputStream()));
	}

	private ArrayList<String> parseResultString(String resultString){
		int last = 0;
		ArrayList<String> toReturn = new ArrayList<String>();
		for (int i = 0; i < resultString.length(); i++){
			if (resultString.charAt(i) == ','){
				toReturn.add(resultString.substring(last, i - 1));
				last = i + 1;
			}
		}

		return toReturn;
	}

	public ArrayList<ArticleBean> readFile(){
		String line;
		ArrayList<ArticleBean> toReturn = new ArrayList<ArticleBean>();
                int i = 0;
		try {
			line = reader.readLine();
                        System.out.println(i);
			while (line != null){
				int index = line.indexOf("<->");
				String url = line.substring(0, index);
				String category = line.substring(index+3);

				Rss rss = this.parser.parse(new URL(url));
				Collection items = rss.getChannel().getItems();
				Iterator iter = items.iterator();

				ArrayList<String> keywords = new ArrayList<String>();

				while (iter.hasNext()){
					Item currentItem = (Item)iter.next();
					String text = "";
					ArticleBean currentBean = new ArticleBean();
					currentBean.setM_Item(currentItem);
					currentBean.setM_sectionName(category);

                                        String newUrl = currentBean.get_Item().getLink().toString();
					BufferedReader urlReader = this.readFromURL(currentBean.get_Item().getLink().toString());

                                        String urlLine = "";

                                        //Test
                                        for (int j = 0; j < 178; j++){
                                            urlLine = urlReader.readLine();
                                        }

					while (urlLine != null){
						//int indexOfKeywords = urlLine.indexOf("name=\"keywords\"");
                                                int indexOfText = urlLine.indexOf("<div class=\"yog-col yog-11u\"><div class=\"yom-mod yom-art-content\"><div class=\"bd\">");
                                                int indexOfEnd = urlLine.indexOf("</div></div>");

                                                if (indexOfText >= 0 && indexOfEnd > indexOfText + 82){
                                                    text = urlLine.substring(indexOfText + 82, indexOfEnd);
                                                    currentBean.setM_text(text);
                                                    break;
                                                }
                                                else if (indexOfText >= 0 && indexOfEnd < 0){
                                                    String append = urlLine.substring(indexOfText + 82);
                                                    append = append.trim();

                                                    text += append;

                                                    while (urlLine != null){
                                                        urlLine = urlReader.readLine();

                                                        indexOfEnd = urlLine.indexOf("</div></div>");
                                                        if (indexOfEnd < 0){
                                                            text += urlLine;
                                                        }
                                                        else{
                                                            text += urlLine.substring(0, indexOfEnd);
                                                            currentBean.setM_text(text.trim());
                                                            break;
                                                        }
                                                    }

                                                    break;
                                                }
                                                else{
                                                    break;
                                                }
					}

					currentBean.setKeywords(keywords);
                                        if (currentBean != null && currentBean.getM_text() != null && !currentBean.getM_text().equals("")){
                                            toReturn.add(currentBean);
                                        }
				}

				line = reader.readLine();
                                i++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RssParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return toReturn;
	}
}
