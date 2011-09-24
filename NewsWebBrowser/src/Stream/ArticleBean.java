/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Stream;

import java.util.ArrayList;

import com.sun.cnpi.rss.elements.Item;

public class ArticleBean {
	private String m_sectionName;
	private Item m_Item;
	private String m_text;
	private ArrayList<String> keywords;

	public ArticleBean(){

	}

	public String get_sectionName() {
		return m_sectionName;
	}

	public void setM_sectionName(String m_sectionName) {
		this.m_sectionName = m_sectionName;
	}

	public Item get_Item() {
		return m_Item;
	}

	public void setM_Item(Item m_Item) {
		this.m_Item = m_Item;
	}

	public ArrayList<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(ArrayList<String> keywords) {
		this.keywords = keywords;
	}

	public String getM_text() {
		return m_text;
	}

	public void setM_text(String m_text) {
		this.m_text = m_text;
	}
}
