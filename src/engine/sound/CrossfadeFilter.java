package engine.sound;

public class CrossfadeFilter implements TrackFilter{
	private double targetVolume;
	private double speed;
	
	public CrossfadeFilter(double targetVolume, double speed){
		this.targetVolume = targetVolume;
		this.speed = speed;
	}
	
	public void setTargetVolume(double targetVolume){
		this.targetVolume = targetVolume;
	}
	public void filter(Track track) {
		long size = track.nextDataSize;
		if(size==-1)
			return;
		double inc = speed*size/track.getSampleRate();
		double vol = track.getVolume();
		if(vol>targetVolume){
			vol -= inc;
			if(vol<targetVolume)
				vol = targetVolume;
		}
		else if(vol<targetVolume){
			vol += inc;
			if(vol>targetVolume)
				vol = targetVolume;
		}
		else
			//remove filter if target volume is reached
			track.setFilter(null);
		track.setVolume(vol);
		if(vol==0)
			track.stop();
	}

}
