package util;

class Starttime{
	private String year = "2009";
	private String month = "08";
	private String day = "04";
	private String hour = "00";
	private String minute = "00";
	private String second = "00";
}

class Static{
	private double duration = 1795.0;
	private String file;
	public Static(double duration,String file) {
		this.duration = duration;
		this.file = file;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public double getDuration() {
		return duration;
	}
	public void setDuration(double duration) {
		this.duration = duration;
	}
}

class Trasition{
	private double duration;
	private String from;
	private String to;
	public Trasition(String from, String to) {
		this.duration = 5.0;
		this.from = from;
		this.to = to;
	}
	public Trasition(double duration, String from, String to) {
		this.duration = duration;
		this.from = from;
		this.to = to;
	}
	public double getDuration() {
		return duration;
	}
	public void setDuration(double duration) {
		this.duration = duration;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
}

class Bind{
	private Static static1;
	private Trasition trasition;
	public Bind(Static static1,Trasition trasition){
		this.static1 = static1;
		this.trasition = trasition;
	}
	public Static getStatic1() {
		return static1;
	}
	public void setStatic1(Static static1) {
		this.static1 = static1;
	}
	public Trasition getTrasition() {
		return trasition;
	}
	public void setTrasition(Trasition trasition) {
		this.trasition = trasition;
	}
}
public class Background {
//	private Starttime starttime;
	private Bind[] binds;
	
	public Bind[] getBinds() {
		return binds;
	}
	public void setBinds(Bind[] binds) {
		this.binds = binds;
	}
}
