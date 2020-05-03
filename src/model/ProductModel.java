package model;

import entities.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javax.imageio.ImageIO;

import com.google.gson.Gson;

public class ProductModel {	
	public Product find(String json) throws IOException {
		Gson gson = new Gson();
		
		Tile tile = gson.fromJson(json, Tile.class);
		
		return new Product(json, getHeights(tile.getYtile(),tile.getXtile(),tile.getZoom()));
	}
	/*
	public List<Product> findAll() throws IOException{
		List<Product> result = new ArrayList<Product>();
		result.add(new Product("p01", getHeights()));
		result.add(new Product("p02", getHeights()));
		result.add(new Product("p03", getHeights()));
		return result;
	}
	*/
	private int[] getHeights(int ytile, int xtile, int zoom) throws IOException {
		/*
		System.out.println("lat:" + lat + " long:" + lon + " zoom:" + zoom);
		
		int xtile = (int)Math.floor( (lon + 180) / 360 * (1<<zoom) ) ;
		int ytile = (int)Math.floor( (1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1<<zoom) ) ;
		if (xtile < 0)
			xtile=0;
		if (xtile >= (1<<zoom))
			xtile=((1<<zoom)-1);
		if (ytile < 0)
			ytile=0;
		if (ytile >= (1<<zoom))
			ytile=((1<<zoom)-1);
		*/
		System.out.println(xtile + ", " + ytile);

		String urlString = "https://s3.amazonaws.com/elevation-tiles-prod/terrarium/" + zoom + "/" + xtile + "/" + ytile + ".png";
		System.out.println(urlString);
		URL url = new URL(urlString);
		BufferedImage img = ImageIO.read(url);
		
		//BufferedImage img = joinBufferedImage(xtile, ytile, zoom);
		int k = 0;
		int size = img.getWidth();
		
		int pix[][] = new int[img.getWidth()][img.getHeight()];
		int heights2D[][] = new int[img.getWidth()][img.getHeight()];
		int heights[] = new int[img.getWidth()*img.getHeight()];
		int [][][] colors = new int[img.getWidth()][img.getHeight()][4];
		
		for(int i=0; i<size; i++) {
			for(int j=0; j<size; j++) {
				Color myColor = new Color(img.getRGB(i,j));
				colors[i][j][0] = myColor.getRed();
				colors[i][j][1] = myColor.getGreen();
				colors[i][j][2] = myColor.getBlue();
				colors[i][j][3] = myColor.getAlpha();
				
				//System.out.println("red: " + colors[i][j][0] + " green: " + colors[i][j][1] + " blue: " + colors[i][j][2]);
				int height = (colors[i][j][0] * 256 + colors[i][j][1] + colors[i][j][2] / 256) - 32768;
				heights[k] = height;
				k++;
				//System.out.println(height);
			}
		}
		return heights;
	}
	
	  public BufferedImage joinBufferedImage(int x, int y, int zoom) throws IOException {
		  	x--; y++;
		  	int k = 0;
		  	String[] imgUrlString = new String[9];
		  	URL[] url = new URL[9];
		  	BufferedImage[] img = new BufferedImage[9];
		  	
		  	for(int j = y; j > (y-3); j--) {
		  		for(int i = x; i < (x+3); i++) {
		  			imgUrlString[k] = "https://s3.amazonaws.com/elevation-tiles-prod/terrarium/" + zoom + "/" + i + "/" + j + ".png";
		  			url[k] = new URL(imgUrlString[k]);
		  			img[k] = ImageIO.read(url[k]);
		  			k++;
		  		}
		  	}
		  	
		    int offset = 0;
		    int width = img[0].getWidth() + img[1].getWidth() + img[2].getWidth() + offset;
		    int height = img[0].getHeight() + img[3].getHeight() + img[6].getHeight() + offset;
		    BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		    Graphics2D g2 = newImage.createGraphics();
		    Color oldColor = g2.getColor();
		    g2.setPaint(Color.BLACK);
		    g2.fillRect(0, 0, width, height);
		    g2.setColor(oldColor);
		    
		    g2.drawImage(img[6], null, 0, 0);
		    g2.drawImage(img[7], null, img[0].getWidth() + offset, 0);
		    g2.drawImage(img[8], null, img[0].getWidth() + img[1].getWidth() + offset, 0);
		    
		    g2.drawImage(img[3], null, 0, img[0].getHeight() + offset);
		    g2.drawImage(img[4], null, img[3].getWidth() + offset, img[1].getHeight() + offset);
		    g2.drawImage(img[5], null, img[3].getWidth() + img[4].getWidth() + offset, img[3].getHeight() + offset);
		    
		    g2.drawImage(img[0], null, 0, img[0].getHeight() + img[3].getHeight() + offset);
		    g2.drawImage(img[1], null, img[6].getWidth() + offset, img[1].getHeight() + img[4].getHeight() + offset);
		    g2.drawImage(img[2], null, img[6].getWidth() + img[7].getWidth() + offset, img[2].getHeight() + img[5].getHeight() + offset);
		    
		    g2.dispose();
		    return newImage;
	  	}
}
