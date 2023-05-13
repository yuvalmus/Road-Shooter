package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public abstract class GifStuff implements DrawImageInterface 
{
	protected int x;
	protected int y;
	protected int currFrame;
	protected Thread gifThread;
	
	protected GifStuff(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.currFrame = 0;
	}
	
	public void startThread()
	{
		try {
			this.gifThread.start();
		} catch (Exception e) {}
	}
	
	public abstract void draw(Graphics g);
	
	// took from url: https://community.oracle.com/tech/developers/discussion/1357765/animated-gif-image-gets-distorted-while-playing
	public static BufferedImage[] loadAllFrames(String filepath)
	{ 
		File fileToLoad = new File(filepath);

		//obtain the appropriate image reader
		ImageInputStream stream = null;
		try {
			stream = ImageIO.createImageInputStream(fileToLoad);
		} catch (IOException e1) {
		}
		ImageReader reader = ImageIO.getImageReaders(stream).next();
	
		reader.setInput(stream, true, false);
	
		//read the images
		List<BufferedImage> framesTmp = new java.util.ArrayList<BufferedImage>(8);
	    int index = 0;
	    while (true) {
	        try {
	            framesTmp.add(reader.read(index++));
	        } catch(IndexOutOfBoundsException | IOException e) {
	            break;
	        }
	    }
	    BufferedImage[] frames = framesTmp.toArray(new BufferedImage[]{});

	    //clean up
	    reader.dispose();
	    try {
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return frames;
	}
	
	public static BufferedImage[] loadAllImages(String dirPath) {
		BufferedImage[] frames = new BufferedImage[63];
		try {
			int i = 1;
			while (i < 64) {				
		        BufferedImage bi = ImageIO.read(new File(dirPath + "/" + i + ".png"));
		        frames[i - 1] = bi;
		        i++;
			}
		} catch (IOException e) {
			System.out.println("oof");
		}
		return frames;
	}
}
