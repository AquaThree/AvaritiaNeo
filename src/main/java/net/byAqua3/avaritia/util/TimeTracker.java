package net.byAqua3.avaritia.util;

public class TimeTracker {
	private long lastTime = System.currentTimeMillis();

	public boolean hasDelayPassed(int delay) {
		long currentTime = System.currentTimeMillis();
		if (this.lastTime + delay <= currentTime) {
			this.lastTime = currentTime;
			return true;
		}
		return false;
	}

	public void markTime() {
		this.lastTime = System.currentTimeMillis();
	}
}
