package models;

public class Photo {
	public long id;
	public String title;
	public String serverid;
	public String secret;
	
	public Photo(long id, String title) {
		this.id = id;
		this.title = title;
	}

	public Photo(long id, String title, String serverid, String secret) {
		this.id = id;
		this.title = title;
		this.serverid = serverid;
		this.secret = secret;
	}
	
	public String src() {
		return "http://farm1.static.flickr.com/" + serverid + "/" + id + "_" + secret + "_m.jpg";
	}
	
	@Override
	public String toString() {
		return "[" + id + "] " + title;
	}
}
