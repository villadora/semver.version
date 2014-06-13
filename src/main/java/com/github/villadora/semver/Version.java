package com.github.villadora.semver;

import java.io.Serializable;
import java.util.Arrays;
import java.util.regex.Matcher;

import com.github.villadora.semver.regexp.VersionRegExps;

public class Version implements Comparable<Version>, Serializable {

	private static final long serialVersionUID = 8782774273177698465L;

	private MainVersion mainVersion;

	private PreRelease prerelease;

	private Build build;

	private String version;

	public static boolean valid(String version) {
		if (version == null)
			return false;

		return VersionRegExps.FULL_REG.matcher(version).matches();
	}

        public static Version valueOf(String version) {
                if(version == null)
                        return null;
                return new Version(version);
        }

	public Version() {
		this.mainVersion = new MainVersion();
		this.prerelease = new PreRelease();
		this.build = new Build();
	}

	public Version(String version) {
		if (!valid(version))
			throw new IllegalArgumentException("version is invalid: " + version);

		Matcher mc = VersionRegExps.FULL_REG.matcher(version);
		if (mc.matches()) {
			this.version = version;
			this.mainVersion = new MainVersion(Integer.parseInt(mc.group(1)), Integer.parseInt(mc.group(2)),
					Integer.parseInt(mc.group(3)));
			this.prerelease = new PreRelease(mc.group(4));
			this.build = new Build(mc.group(5));
		} else
			throw new IllegalArgumentException("version is invalid: " + version);
	}

	public Version(Version version) {
		this(version.getMajor(), version.getMinor(), version.getPatch(), version.getPrerelease(), version.getBuild());
	}

	public Version(int major, int minor, int patch, String prerelease, String build) {
		this(major, minor, patch, new String[] { prerelease }, new String[] { build });
	}

	public Version(int major, int minor, int patch, String[] prerelease, String[] build) {
		this.mainVersion = new MainVersion(major, minor, patch);
		this.prerelease = new PreRelease(prerelease);
		this.build = new Build(build);
	}

	public Version(int major, int minor, int patch, String[] prerelease) {
		this(major, minor, patch, prerelease, null);
	}

	public Version(int major, int minor, int patch) {
		this(major, minor, patch, new String[] {}, new String[] {});
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

	public String getPrerelease() {
		return prerelease.toString();
	}

	public void setPrerelease(String... prerelease) {
		this.prerelease.setPrerelease(prerelease);
	}

	public String getBuild() {
		return build.toString();
	}

	public void setBuild(String... build) {
		this.build.setBuild(build);
	}

	public Version incrMajor() {
		this.mainVersion.incrMajor();
                this.mainVersion.setPatch(0);
                this.prerelease.setPrerelease(null);
		return this;
	}

	public Version incrMinor() {
		this.mainVersion.incrMinor();
                this.mainVersion.setPatch(0);
                this.prerelease.setPrerelease(null);
		return this;
	}

	public Version incrPatch() {
		this.mainVersion.incrPatch();
                this.prerelease.setPrerelease(null);
		return this;
	}

	@Override
	public String toString() {
		if (this.version != null)
			return this.version;

		StringBuilder vb = new StringBuilder(mainVersion.toString());
		if (prerelease.getPrerelease().length > 0) {
			vb.append("-");
			vb.append(prerelease.toString());
		}
		if (build.getBuild().length > 0) {
			vb.append("+");
			vb.append(build.toString());
		}

		return vb.toString();
	}

	@Override
	public boolean equals(Object that) {
		if (this == that)
			return true;

		if (that instanceof Version) {
			Version other = (Version) that;
			return this.mainVersion.equals(other.mainVersion) && this.prerelease.equals(other.prerelease)
					&& this.build.equals(other.build);
		}

		return false;
	}

	@Override
	public int hashCode() {
		int result = 17;
		for (int code : Arrays.asList(this.mainVersion.hashCode(), this.prerelease.hashCode(), this.build.hashCode())) {
			result = 31 * result + code;
		}
		return result;
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
