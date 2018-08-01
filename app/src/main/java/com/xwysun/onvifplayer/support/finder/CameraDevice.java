package com.xwysun.onvifplayer.support.finder;


import java.util.Objects;
import java.util.UUID;

public class CameraDevice {
	public UUID uuid;
	public String serviceURL;
	private int id;
	private String name;
	private String ipAddr;
	private boolean isOnline = false;
	private String rtspUri = "";
	
	public int width;
	public int height;
	public int rate;

	public String username;
	public String password;

	public CameraDevice(UUID uuid, String serviceURL) {
		this.uuid = uuid;
		this.serviceURL = serviceURL;
	}

	public void setSecurity(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public void setProperties(int width, int height, int rate) {
		this.width = width;
		this.height = height;
		this.rate = rate;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getServiceURL() {
		return serviceURL;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public String getRtspUri() {
		return rtspUri;
	}

	public void setRtspUri(String rtspUri) {
		this.rtspUri = rtspUri;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CameraDevice that = (CameraDevice) o;
		return id == that.id &&
				isOnline == that.isOnline &&
				width == that.width &&
				height == that.height &&
				rate == that.rate &&
				Objects.equals(uuid, that.uuid) &&
				Objects.equals(serviceURL, that.serviceURL) &&
				Objects.equals(name, that.name) &&
				Objects.equals(ipAddr, that.ipAddr) &&
				Objects.equals(rtspUri, that.rtspUri) &&
				Objects.equals(username, that.username) &&
				Objects.equals(password, that.password);
	}

	@Override
	public int hashCode() {

		return Objects.hash(uuid, serviceURL, id, name, ipAddr, isOnline, rtspUri, width, height, rate, username, password);
	}

	@Override
	public String toString() {
		return "CameraDevice{" +
				"uuid=" + uuid +
				", serviceURL='" + serviceURL + '\'' +
				'}';
	}
}
