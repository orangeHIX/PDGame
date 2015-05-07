package utils;

import java.awt.Image;

public class NamedImage {
	public String name;
	public Image image;
	
	public NamedImage(String name, Image image) {
		super();
		this.name = name;
		this.image = image;
	}

	public int getID(){
		return Integer.parseInt(name.substring(name.indexOf("_")+1, name.indexOf(".jpg")));
	}
}