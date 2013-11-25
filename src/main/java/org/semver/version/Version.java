package org.semver.version;

public class Version implements Comparable<Version> {

	private MainVersion mainVersion;

	private PreRelease prerelease;

	private String[] build;

	public Version(int major, int minor, int patch, String[] prerelease, String[] build) {
		this.mainVersion = new MainVersion(major, minor, patch);
		this.prerelease = new PreRelease(prerelease);
		this.build = build;
	}

	public Version(int major, int minor, int patch) {
		this(major, minor, patch, null, null);
	}

	public int getMajor() {
		return mainVersion.getMajor();
	}

	public void setMajor(int major) {
		this.mainVersion.setMajor(major);
	}

	public int getMinor() {
		return mainVersion.getMinor();
	}

	public void setMinor(int minor) {
		this.mainVersion.setMinor(minor);
	}

	public int getPatch() {
		return mainVersion.getPatch();
	}

	public void setPatch(int patch) {
		this.mainVersion.setPatch(patch);
	}

	public String[] getPrerelease() {
		return prerelease.getPrerelease();
	}

	public void setPrerelease(String[] prerelease) {
		this.prerelease.setPrerelease(prerelease);
	}

	public String[] getBuild() {
		return build;
	}

	public void setBuild(String[] build) {
		this.build = build;
	}

	public int compareTo(Version o) {
		if (o == null)
			return 1;

		int diff = this.mainVersion.compareTo(o.mainVersion);
		if (diff != 0)
			return diff;

		diff = this.prerelease.compareTo(o.prerelease);
		if (diff != 0)
			return diff;

		return 0;
	}

}
