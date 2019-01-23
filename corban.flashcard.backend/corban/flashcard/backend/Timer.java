package corban.flashcard.backend;

public class Timer {
	private long startTime = 0;
	private long sec = 0;
	private long min = 0;
	private long hour = 0;
	private long mills = 0;
	
	private boolean rolling;
	
	public Timer(){
		
	}
	
	public void start(){
		rolling = true;
		startTime = System.currentTimeMillis();
		sec = 0;
		min = 0;
		hour = 0; 
		mills = 0;
	}
	
	public void update(){
		if(rolling){
			mills = System.currentTimeMillis() - startTime;
			sec = (mills / 1000) % 60;
			min = (mills / (1000 * 60)) % 60;
			hour = (mills / (1000 * 60 * 60)) % 24;
		}
	}
	
	public void stop(){
		update();
		rolling = false;
	}
	
	public void resume(){
		rolling = true;
	}
	
	public void restart(){
		start();
	}
	
	public String toString(){
		update();
		return String.format("%02d:%02d:%02d.%01d", hour, min, sec, mills % 1000);
	}
	
	public static String millToString(long mills){
		long sec = (mills / 1000) % 60;
		long min = (mills / (1000 * 60)) % 60;
		long hour = (mills / (1000 * 60 * 60)) % 24;
		long day = (mills / (1000 * 60 * 60 * 24));
		if(day != 0) return String.format("%01d:%02d:%02d:%02d.%03d", day, hour, min, sec, mills % 1000);
		if(hour != 0) return String.format("%02d:%02d:%02d.%03d", hour, min, sec, mills % 1000);
		if(min != 0) return String.format("%02d:%02d.%03d", min, sec, mills % 1000);
		return String.format("%02d.%03d", sec, mills % 1000);
	}
	
	public static String millToStringSentence(long mills){
		long sec = (mills / 1000) % 60;
		long min = (mills / (1000 * 60)) % 60;
		long hour = (mills / (1000 * 60 * 60)) % 24;
		long day = (mills / (1000 * 60 * 60 * 24));
		if(day != 0) return String.format("%01d day %02d hour %02d min %02d sec and %03d mil", day, hour, min, sec, mills % 1000);
		if(hour != 0) return String.format("%02d:%02d:%02d.%03d", hour, min, sec, mills % 1000);
		if(min != 0) return String.format("%02d:%02d.%03d", min, sec, mills % 1000);
		return String.format("%02d.%03d", sec, mills % 1000);
	}
}
