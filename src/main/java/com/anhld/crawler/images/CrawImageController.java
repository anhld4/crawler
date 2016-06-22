package com.anhld.crawler.images;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.anhld.crawler.images.utils.Constants;
import com.anhld.crawler.samples.TestBean;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

@Controller
public class CrawImageController {

	private static final Logger logger = LoggerFactory.getLogger(CrawImageController.class);
	@Autowired
	TestBean test;
	
	@RequestMapping(value = "/crawimages", method=RequestMethod.POST)
	public String CrawImages(@ModelAttribute ImageInput imgInput, Model model) {
		test.print();
		
		String url = imgInput.getUrl();
		//String folderName = imgInput.getFolder();
		int numberOfCrawlers = imgInput.getNumberOfCrawlers();
		int minimunImgSize = imgInput.getMinimunImgSize();
		
		if(null != url){
			/*if (null != folderName) { // create folder
				// TODO: read folder location from config file
				String imageFolder = Constants.FOLDER_PREFIX + folderName;
				String metaDataFolder = Constants.FOLDER_PREFIX + folderName + "/metadata";
				File file = new File(imageFolder);
				if (!file.exists()) {
					if (file.mkdir()) {
						System.out.println("Directory is created!");
						new File(metaDataFolder).mkdir();// make meta data folder
					} else {
						System.out.println("Failed to create directory!");
						logger.info("### Failed to create directory!");
					}
				}
				logger.info("### crawler starting...");
				crawImages(url, imageFolder, metaDataFolder, numberOfCrawlers, minimunImgSize);	
				return getImagesCrawler(imageFolder);
			} else {

				
			}*/
			// craw images to default folder
			// TODO: read default folder from config file
			logger.info("### crawler starting...");
			File fileImages = new File(Constants.DEFAULT_FOLDER_IMG);
			File fileMeta = new File(Constants.DEFAULT_FOLDER_METADATA);
			if(!fileImages.exists()){
				if(!fileImages.mkdir() || !fileMeta.mkdirs()){
					model.addAttribute("images", Collections.emptyList());
					return "imagecrawlerresult";
				}
			}
			crawImages(url, Constants.DEFAULT_FOLDER_IMG, Constants.DEFAULT_FOLDER_METADATA, numberOfCrawlers, minimunImgSize);
			SortedSet<Image> images = getImagesCrawler(Constants.DEFAULT_FOLDER_IMG);
			model.addAttribute("images", images);
			return "imagecrawlerresult";
		}else {
			model.addAttribute("images", Collections.emptyList());
			return "imagecrawlerresult";
		}
	}
	
	@RequestMapping(value = "/crawimages", method=RequestMethod.GET)
	public String inputForm(Model model){
		model.addAttribute("imageInput", new ImageInput());
		return "imagecrawlerinput";
	}

	private void crawImages(String url, String imageStorageFolder, String metaDataFolder, int numCraw, int minimunImgSize) {
		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(metaDataFolder);
		//config.setMaxDepthOfCrawling(2);
		/*
		 * Since images are binary content, we need to set this parameter to
		 * true to make sure they are included in the crawl.
		 */
		config.setIncludeBinaryContentInCrawling(true);

		String[] crawlDomains = { url };

		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller;
		try {
			controller = new CrawlController(config, pageFetcher, robotstxtServer);
			for (String domain : crawlDomains) {
				controller.addSeed(domain);
			}

			ImageCrawler.configure(crawlDomains, imageStorageFolder, minimunImgSize);

			controller.start(ImageCrawler.class, numCraw);
			
			System.out.println("Done craw Images!");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private SortedSet<Image> getImagesCrawler(String path){
		List<Image> images = new ArrayList<>();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if(!listOfFiles[i].isDirectory() && System.currentTimeMillis() - listOfFiles[i].lastModified() <= 3 * 60 * 1000)
				images.add(new Image(listOfFiles[i].getName(), Constants.IMAGE_URL_PREFIX + listOfFiles[i].getName(), listOfFiles[i].lastModified()));
		}
		return new TreeSet<Image>(images);
	}
	@ResponseBody
	@RequestMapping(value = "/image", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@RequestParam(value="name", defaultValue="World") String name) {
		
		try {
            InputStream is = new FileInputStream(Constants.DEFAULT_FOLDER_IMG + Constants.SPLASH + name);
            BufferedImage img = ImageIO.read(is);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", bos);
            return bos.toByteArray();
        } catch (FileNotFoundException e) {
            return null; //todo: return safe photo instead
        } catch (IOException e) {
            return null;  //todo: return safe photo instead
        }
    }
	
	@RequestMapping(value = "/viewresults", method=RequestMethod.GET)
	public String viewResults(Model model){
		SortedSet<Image> images = getImagesCrawler(Constants.DEFAULT_FOLDER_IMG);

		model.addAttribute("images", images);
		return "imagecrawlerresult";
	}
}
