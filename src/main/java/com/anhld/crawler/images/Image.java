package com.anhld.crawler.images;

import java.util.ArrayList;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

//By default, Jackson 2 will only work with with fields that are either public, or have a public getter methods 
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Image implements Comparable<Image>{

	String name;
	String url;
	Long date;
	public Image(String name, String url, long date) {
		super();
		this.name = name;
		this.url = url;
		this.date = date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	
	@Override
	public int compareTo(Image img) {
		// TODO Auto-generated method stub
		return this.date.compareTo(img.getDate());
	}
	
	public static void main(String[] args) {
		/*Image img1 = new Image("1", "1", 1);
		Image img2 = new Image("2", "2", 2);
		java.util.List<Image> test = new ArrayList<Image>();
		test.add(img2);
		test.add(img1);
		Collections.sort(test);
		System.out.println(test.get(0).getName());
		System.out.println(test.get(1).getName());*/
		String testUrl = "http://thiendia.com/diendan/threads/gia-lam-v-u8x-co-tiep-vien-vna-xinh-xan.1008735/";
		System.out.println(testUrl.substring(0, testUrl.indexOf(".")));
		System.out.println(testUrl.indexOf("."));
	}
}
