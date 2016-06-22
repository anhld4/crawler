package com.anhld.crawler.images;

public class ImageInput {

	String url;
	int numberOfCrawlers;
	int minimunImgSize;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getNumberOfCrawlers() {
		return numberOfCrawlers;
	}
	public void setNumberOfCrawlers(int numberOfCrawlers) {
		this.numberOfCrawlers = numberOfCrawlers;
	}
	public int getMinimunImgSize() {
		return minimunImgSize;
	}
	public void setMinimunImgSize(int minimunImgSize) {
		this.minimunImgSize = minimunImgSize;
	}
	public ImageInput() {
		super();
		this.numberOfCrawlers =10;
		this.minimunImgSize = 30;//kb
	}
}
