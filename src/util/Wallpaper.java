package util;

public class Wallpaper {
	private String name;
	private String filename;
	private String options;
	/** 
	* wallpaper 的属性
	*/ 
	private String deleted;
	
	public Wallpaper(String name,String filename) {
		this(name,filename,"zoom","false");
	}
	public Wallpaper(String name,String filename, String options, String deleted) {
		this.name = name;
		this.filename = filename;
		this.options = options;
		this.deleted = deleted;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
}
