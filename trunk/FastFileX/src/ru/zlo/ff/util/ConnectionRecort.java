package ru.zlo.ff.util;

public class ConnectionRecort {
	public Integer ID;
	public Boolean anonim = true;
	public Boolean isFTP = true;
	public String user = "";
	public String server = "";

	@Override
	public String toString() {
		return server;
	}
}
