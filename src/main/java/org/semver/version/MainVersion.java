package org.semver.version;

class MainVersion implements Comparable<MainVersion> {
	private int major;
	private int minor;
	private int patch;

	public MainVersion(int major, int minor, int patch) {
		this.major = major;
		this.minor = minor;
		this.patch = patch;
	}

	public int getMajor() {
		return major;
	}

	public void setMajor(int major) {
		this.major = major;
	}

	public int getMinor() {
		return minor;
	}

	public void setMinor(int minor) {
		this.minor = minor;
	}

	public int getPatch() {
		return patch;
	}

	public void setPatch(int patch) {
		this.patch = patch;
	}

	public int compareTo(MainVersion o) {
		if (o == null) {
			return 1;
		}

		int diff = this.major - o.major;
		if (diff != 0)
			return diff;

		diff = this.minor - o.minor;
		if (diff != 0)
			return diff;

		diff = this.patch - o.patch;
		if (diff != 0)
			return diff;

		return 0;
	}
}