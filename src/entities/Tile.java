package entities;

public class Tile {
	private int ytile; // lat
	private int xtile; // lon
	private int zoom;
	
	public Tile(int ytile, int xtile, int zoom) {
		this.ytile = ytile;
		this.xtile = xtile;
		this.zoom = zoom;
	}
	
	public int getYtile() {
		return ytile;
	}
	
	public int getXtile() {
		return xtile;
	}
	
	public int getZoom() {
		return zoom;
	}
	
	public void setYtile(int ytile) {
		this.ytile = ytile;
	}
	
	public void setXtile(int xtile) {
		this.xtile = xtile;
	}
	
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
}
