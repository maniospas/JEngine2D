package engine.sound;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

public abstract class Playable {
	private volatile boolean loop = false;
	private volatile boolean paused = false;
	private volatile boolean playing = false;
	private volatile double volume = 1;
	private volatile double pan = 0;
	private volatile double speed = 1;
	private volatile Object lock = new Object();
	private volatile int end_silence = 0;
	private Thread thread = null;
	private volatile long bufferSize = 1024;
	protected volatile SourceDataLine line = null;
	
	protected volatile byte[] nextData = new byte[1024];
	protected volatile int nextDataSize;
	
	public volatile static int soundSources = 0;

	/**
	 * <h1>getFormat()</h1>
	 * Returns an AudioFormat that has the calculated audio format for the sound.
	 */
	protected abstract AudioFormat getFormat();

	/**
	 * <h1>initialize()</h1>
	 * Tries to perform any initializations that should be done before play().
	 * May throw exceptions if failed to do so.
	 */
	protected abstract void initialize() throws Exception;
	
	/**
	 * <h1>reload()</h1>
	 * Reloads data.
	 */
	public abstract void reload() throws Exception;

	/**
	 * <h1>process()</h1>
	 * This function is called directly after next() and is used to perform some possible
	 * processing on the sound.
	 */
	protected abstract void process() throws Exception;

	/**
	 * <h1>finish()</h1>
	 * Performs any closing operations after the sound has successfully been played.
	 */
	public abstract void finish();

	/**
	 * <h1>next(bufferSize)</h1>
	 * Assigns byte[] values to nextData and long values to nextDataSize. Returns
	 * false if no data is possible to be send. Throws exception only if some error occured.
	 * @param bufferSize: desired size for nextDataSize
	 */
	protected abstract boolean next(long bufferSize) throws Exception;

	/**
	 * <h1>getMinVolume()</h1>
	 * Volumes below that value are paused.
	 */
	public static double getMinVolume(){
		return 0.00010;
	}

	/**
	 * <h1>decodeFormat(baseFormat)</h1>
	 * Returns the decoder format.
	 */
	public AudioFormat decodeFormat(AudioFormat baseFormat){
		AudioFormat decodedFormat = new AudioFormat(
        		AudioFormat.Encoding.PCM_SIGNED,
        		(float) (baseFormat.getSampleRate()*speed),
        		16,
        		baseFormat.getChannels(),
        		baseFormat.getChannels()*2,//frame size
        		(float) (baseFormat.getSampleRate()*speed),//frame Rate
        		false //big endian
        	);
		return decodedFormat;
	}

	/**
	 * <h1>Playable()</h1>
	 * Class constructor. Initializes (but does not start) the thread.
	 */
	public Playable(double speed){
		this.speed = speed;
	}

	/**
	 * <h1>createThread()</h1>
	 * Creates the thread used by the class.
	 */
	private synchronized void createThread(){
		//final String errorString = toString();
		thread = new Thread(){
            public void run(){
            	line = null;
            	//us = 0;
            	boolean success = true;
            	do{
            	try{
            		success = false;
            		synchronized(lock){
                    	soundSources++;
	            		initialize();
	                	DataLine.Info info = new DataLine.Info(SourceDataLine.class, getFormat());
	                	line = (SourceDataLine) AudioSystem.getLine(info);
	                	line.open(getFormat());
	                	line.start();
            		}
            		do{
	            		boolean looping = next(getBufferSize());
	            		double prevPropVol = -1;
	            		double prevPropPan = 0;
	                	while(looping){
	                		if(paused){
	                			sleep(1);
	                			continue;
	                		}
	                		synchronized(lock){
	                			process();
	                			if(volume!=prevPropVol){
	                				((FloatControl)line.getControl(FloatControl.Type.MASTER_GAIN)).setValue((float) getVolumeDB());
	                				prevPropVol = volume;
	                			}
	                			if(pan!=prevPropPan){
		                			if(line.isControlSupported(FloatControl.Type.PAN))
			            				((FloatControl)line.getControl(FloatControl.Type.PAN)).setValue((float) getPan());
		                			prevPropPan = pan;
	                			}
		                		//((FloatControl)line.getControl(FloatControl.Type.SAMPLE_RATE)).setValue((float) getSampleRate()*getSpeed());
		            			line.write(nextData, 0, nextDataSize);
		            			looping = next(getBufferSize());
		            			//us = line.getMicrosecondPosition();
	                		}
	                	}
                		if(end_silence!=0)
                			sleep(end_silence);
		    			success = true;
	                	if(isLooped())
	                		reload();
            		}while(isLooped());
            		synchronized(lock){
            			line.drain();
            		}
            	}
	    		catch(Exception e){
	    			//System.err.println("Failed to initialize sound "+errorString+": "+e.toString());
	    			//e.printStackTrace();
	    		}
            	finally{
            		synchronized(lock){
            			if(line!=null)
                			line.drain();
	            		close();
	                	soundSources--;
            		}
            	}
            	}while(!success);
            };
		};
	}
	
	/**
	 * <h1>restart()</h1>
	 * Restarts playing by calling stop() and then play().
	 */
	public void restart(){
		stop();
		play();
	}
	
	
	/**
	 * <h1>play()</h1>
	 * Starts or resumes playing (creates new thread).
	 */
	public void play(){
		if(isPaused()){
			resume();
		}
		else if(!isPlaying()){
        	playing = true;
			createThread();
			thread.start();
		}
	}

	/**
	 * <h1>isPlaying()</h1>
	 * Returns weather the sound is currently playing or not (returns false if paused or stopped).
	 */
	public synchronized boolean isPlaying(){
		return /*isOpen() && */playing && !paused;
	}


	/**
	 * <h1>isOpen()</h1>
	 * Returns weather the sound is currently open or not. A sound is open if it either is playing
	 * or is paused. After calling stop() the sound is no longer open.
	 */
	public synchronized boolean isOpen(){
		return playing;
	}
	
	/**
	 * <h1>close()</h1>
	 * Performs close operations on completion or interruption of thread. It does not stop
	 * the thread itself. ALWAYS use stop() unless you understand exactly what is happening.
	 */
	private synchronized void close(){
		if(line!=null){
			line.flush();
    		//line.drain();
    		line.stop();
    		line.close();
		}
		finish();
    	playing = false;
	}

	/**
	 * <h1>resume()</h1>
	 * Resumes the thread if it is paused.
	 */
	private synchronized void resume(){
		paused = false;
		/*synchronized(lock){
			if(isPaused()){
				try{
					thread.notify();
					paused = false;
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}*/
	}
	
	/**
	 * <h1>stop()</h1>
	 * Stops playing (stops created thread)
	 */
	public synchronized void stop(){
		loop(false);
		resume();
		//synchronized(lock){
			if(isOpen()){
				if(thread.isAlive() && !thread.isInterrupted())
					thread.interrupt();
				close();
			}
		//}
	}

	/**
	 * <h1>pause()</h1>
	 * Pauses playing (pauses created thread by locking it).
	 */
	public synchronized void pause(){
		paused = true;
		/*synchronized(lock){
			if(isPlaying()){
				try{
					thread.wait();
				}
				catch(Exception e){
					e.printStackTrace();
				}
				paused = true;
			}
		}*/
	}

	/**
	 * <h1>isPaused()</h1>
	 * Returns weather sound paused.
	 */
	public synchronized boolean isPaused(){
		return playing && paused;
	}

	/**
	 * <h1>setVolume(volume)</h1>
	 * @param volume : Must be between 0 and 1.
	 */
	public synchronized void setVolume(double volume){
		this.volume = volume;
		if(volume<getMinVolume()){
			volume = getMinVolume();
			//pause();
		}
	}

	/**
	 * <h1>autoSetVolume(volume)</h1>
	 * @param volume : Must be between 0 and 1. If that value is less than getMinimumVolume(),
	 * pause() is called.
	 */
	public synchronized void autoSetVolume(double volume){
		this.volume = volume;
		if(volume<getMinVolume()){
			volume = getMinVolume();
			pause();
		}
	}

	/**
	 * <h1>getVolume()</h1>
	 * Returns the current volume in a range getMinimumVolume() to 1.
	 */
	public synchronized double getVolume(){
		return volume;
	}

	/**
	 * <h1>setVolume()</h1>
	 * Returns the current volume in DB (less than or equal to 0).
	 */
	public synchronized double getVolumeDB(){
		double ret = Math.log(volume)/Math.log(10.0)*20.0;
		if(ret<-80)
			return -80;
		return ret;
	}

	/**
	 * <h1>loop(isLooped)</h1>
	 * sets loop on or off
	 */
	public synchronized void loop(boolean isLooped){
		this.loop = isLooped;
	}

	/**
	 * <h1>isLooped()</h1>
	 * returns weather loop is turned on or of
	 */
	public synchronized boolean isLooped(){
		return loop;
	}
	
	/**
	 * <h1>setBufferSize(bufferSize)</h1>
	 * Sets the desired buffer size for each frame (default buffer size is 4096).
	 */
	public synchronized void setBufferSize(long bufferSize){
		this.bufferSize = bufferSize;
	}

	/**
	 * <h1>getBufferSize()</h1>
	 * Returns the desired buffer size (default buffer size is 4096).
	 */
	public synchronized long getBufferSize(){
		return bufferSize;
	}	

	/**
	 * <h1>setPan()</h1>
	 * Sets pan (-1 is left, +1 is right, 0 is balanced).
	 * Pan is 0 by default.
	 */
	public synchronized void setPan(double pan){
		this.pan = pan;
	}
	
	/**
	 * <h1>getPan()</h1>
	 * Returns pan (-1 is left, +1 is right, 0 is balanced).
	 * Pan is 0 by default.
	 */
	public synchronized double getPan(){
		return pan;
	}
	
	/**
	 * <h1>getSpeed()</h1>
	 * Returns relative sound speed (1 by default).
	 */
	public synchronized double getSpeed(){
		return speed;
	}
	
	/**
	 * <h1>getSampleRate()</h1>
	 * Returns the sample rate of the sound.
	 */
	public synchronized double getSampleRate(){
		return getFormat().getFrameRate()*getFormat().getFrameSize();
	}
	
	/**
	 * <h1>getElapsedTime()</h1>
	 * Returns elapsed time from when the sound has started in seconds.
	 * This is <i>NOT</i> the current time but rather measures how much continues time various parts
	 * of sound have been playing with (although if setElapsedTime() or setNextPosition() have
	 * not been called, this time is the same as the current time). To get the current time
	 * use <i>getPositionTime()*getDuration()</i> instead.
	 */
	public synchronized double getElapsedTime(){
		return line.getMicrosecondPosition()/1000.0/1000.0;
	}
	
	/**
	 * <h1>getElapsedTime()</h1>
	 * Returns duration of sound.
	 */
	public abstract double getDuration();

	/**
	 * <h1>getPositionTime()</h1>
	 * Returns the current time position in range 0 to 1 (where 1 corresponds to getDuration()).
	 */
	public abstract double getPositionTime();

	/**
	 * setNextPosition(progress)
	 * @param progress : Values between 0 and 1. Sets the start of next buffer input at the appropriate position.
	 */
	public abstract void setNextPosition(double progress);
	
	/**
	 * <h1>setElapsedTime(desiredTime)</h1>
	 * Sets the time.
	 */
	public synchronized void setElapsedTime(double desiredTime){
		setNextPosition(desiredTime/getDuration());
	}
	
	/**
	 * <h1>setEndSilence(duration)</h1>
	 * Sets a silence duration after sound ends before end of sound is recognized.
	 * @param duration in milliseconds
	 */
	public synchronized void setEndSilence(int duration) {
		end_silence = duration;
	}
}
