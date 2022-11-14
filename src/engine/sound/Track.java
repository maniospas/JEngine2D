package engine.sound;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.apache.commons.io.IOUtils;

public class Track extends Playable{
	private volatile String path;
	private volatile AudioInputStream inputStream = null;
	private volatile AudioInputStream decodedStream = null;
	private volatile AudioFormat decodedFormat = null;
	private double duration = 0;
	//private long size = 0;
	private TrackFilter filter = null;

	public Track(String path, double speed) throws Exception{
		super(speed);
    	this.path = path;
    	//if(!inputFile.exists())
    	//	inputFile = new File(Track.class.getResource("/"+path).getPath());
    	(new Thread(){
    		@Override
    		public void run(){
    			try {
					reload();
				}
    			catch (Exception e) {
					e.printStackTrace();
				}
    		}
    	}).start();
	}
	
	public Track(String path)  throws Exception{
		this(path, 1);
	}

	/**
	 * <h1>load()</h1>
	 * Loads track streams but only if not already present. It calls reload() for the actual loading.
	 */
	public synchronized void load() throws Exception{
		if(decodedStream==null)//decodedStream is the last generated data
			reload();
	}
	
	@Override
	public synchronized void reload() throws Exception{
		decodedStream = null;
		inputStream = null;
		decodedFormat = null;
		byte [] bufferArray = fileBuffer.get(path);
		if(bufferArray==null){
			InputStream fis = new File(path).exists()?new FileInputStream(new File(path)):getResourceStream(path);
			if(fis==null) {
				inputStream = null;
				System.out.println("Failed to load sound file: "+path);
				return;
			}
			bufferArray = IOUtils.toByteArray(fis);
			fis.read(bufferArray);
			fileBuffer.put(path, bufferArray);
			fis.close();
			System.out.println("Loaded new sound file: "+path);
		}
		inputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(bufferArray));
		
		
		decodedFormat = decodeFormat(inputStream.getFormat());
    	decodedStream = AudioSystem.getAudioInputStream(decodedFormat, inputStream);
    	loadProperties();
	}
	private InputStream getResourceStream(String path) {
		InputStream ret = Track.class.getResourceAsStream("/"+path);
		if(ret==null)
			ret = Track.class.getResourceAsStream("/src/"+path);
		return ret;
	}
	private static HashMap<String, byte []> fileBuffer = new HashMap<String, byte []>();
	
	/**
	 * <h1>loadProperties()</h1>
	 * Loads track properties from file.
	 */
	private void loadProperties(){
		if(!new File(path).exists())
			return;
		//size = inputFile.length();
	}
	
	// OVERRIDED FUNCTIONS
	@Override
	public double getDuration(){
		return duration;
	}
	
	@Override
	protected AudioFormat getFormat() {
		return decodedFormat;
	}
	
	@Override
	public synchronized void initialize() throws Exception{
		reload();
	}

	@Override
	protected synchronized void process() throws Exception{
		
	}

	@Override
	public synchronized void finish(){
		try{
			if(inputStream!=null)
				inputStream.close();
			if(decodedStream!=null)
				decodedStream.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected synchronized boolean next(long bufferSize) throws Exception{	
		nextDataSize = decodedStream.read(nextData, 0, nextData.length);
		if(filter!=null)
			filter.filter(this);
		//if(nextDataSize!=-1)
			//currentPosition += nextDataSize;/// todo: fix this to be correct
		return nextDataSize != -1;
	}

	@Override
	public synchronized void setNextPosition(double progress){
		/*try{
			reload();
			double toSkip = progress*size;
			currentPosition = (long)Math.floor(toSkip/getBufferSize())*getBufferSize();
			decodedStream.skip(currentPosition);
		}
		catch(Exception e){
			System.out.println("Streams must have been created first: "+e.toString());
		}*/
	}
	
	@Override
	public synchronized double getPositionTime(){
		return -1;//(double)currentPosition/size;
	}
	
	
	@Override
	public String toString(){
		return new File(path).getName();
	}
	
	public TrackFilter getFilter(){
		return filter;
	}
	public void setFilter(TrackFilter filter){
		this.filter = filter;
	}
}
