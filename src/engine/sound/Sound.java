package engine.sound;


import java.util.HashMap;

import engine.sound.CrossfadeFilter;
import engine.sound.Track;

public class Sound {
	private static float SFXvolume = 1;
	private static float volume = 1;
	private static HashMap<String, Track> tracks = new HashMap<String, Track>();
	private static Track music = null;
	
	public static Track get(String path) {
		try {
			Track ret = tracks.get(path);
			if(ret==null)
				tracks.put(path, ret = new Track(path));
			return ret;
		}
		catch(Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
	public static void forcePlay(String path) {
		Track track = get(path);
		if(track!=null && SFXvolume!=0) {
			track.restart();
			track.autoSetVolume(SFXvolume);
		}
	}
	public static void play(String path) {
		Track track = get(path);
		if(track!=null && SFXvolume!=0 && !track.isPlaying()) {
			track.restart();
			track.autoSetVolume(SFXvolume);
			track.setEndSilence(500);
		}
	}
	public static void playMusic(String path) {
		Track nextMusic = get(path);
		if(nextMusic==null || music==nextMusic)
			return;
		if(music!=null)
			music.setFilter(new CrossfadeFilter(0, 0.3));
		music = nextMusic;
		music.setVolume(0);
		music.loop(true);
		music.setFilter(new CrossfadeFilter(volume, 0.3));
		music.play();
	}
	public static void setVolume(float vol) {
		if(music!=null) {
			music.setVolume(vol);
			music.setFilter(null);
			if(volume==0)
				music.restart();
		}
		volume = vol;
	}
	public static float getVolume() {
		return volume;
	}
	public static void setSFXVolume(float vol) {
		SFXvolume = vol;
	}
	public static float getSFXVolume() {
		return SFXvolume;
	}
}
