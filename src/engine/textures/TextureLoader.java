package engine.textures;

import java.util.HashMap;

public class TextureLoader {
	private static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	
	public static Texture get(String path) {
		Texture texture = textures.getOrDefault(path, null);
		if(texture==null) 
			textures.put(path, texture = new Texture(path));
		return texture;
	}
}
