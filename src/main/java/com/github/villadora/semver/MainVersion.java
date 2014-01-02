package com.github.villadora.semver;

import java.io.Serializable;
import java.util.Arrays;
import java.util.regex.Matcher;

import com.github.villadora.semver.regexp.VersionRegExps;

class MainVersion implements Comparable<MainVersion>, Serializable {

	private static final long serialVersionUID = 161760463033091502L;

	private int major;
	private int minor;
	private int patch;

	public static boolean valid(String mainVersion) {
		if (mainVersion == null)
			return false;

		return VersionRegExps.MAIN_VERSION_REG.matcher(mainVersion).matches();
	}

	public MainVersion() {
		this.major = this.minor = this.patch = 0;
	}

	public MainVersion(String mainVersion) {
		if (!valid(mainVersion))
			throw new IllegalArgumentException("mainVersion is invalid: " + mainVersion);

		Matcher matcher = VersionRegExps.MAIN_VERSION_REG.matcher(mainVersion);
		if (matcher.matches()) {
			this.major = Integer.parseInt(matcher.group(1));
			this.minor = Integer.parseInt(matcher.group(2));
			this.patch = Integer.parseInt(matcher.group(3));
		} else {
			throw new IllegalArgumentException("mainVersion is invalid: " + mainVersion);
		}
	}

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

	public void incrMajor() {
		major += 1;
	}

	public void incrMinor() {
		minor += 1;
	}

	public void incrPatch() {
		patch += 1;
	}

	@Override
	public String toString() {
		return Integer.toString(major) + "." + Integer.toString(minor) + "." + Integer.toString(patch);
	}

	@Override
	public boolean equals(final Object that) {
		if (this == that) {
			return true;
		}
		return that instanceof MainVersion ? this.compareTo((MainVersion) that) == 0 : false;
	}

	@Override
	public int hashCode() {
		int result = 17;
		for (int num : Arrays.asList(this.major, this.minor, this.patch)) {
			result = 31 * result + num;
		}

		return result;
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