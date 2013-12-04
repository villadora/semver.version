package com.github.villadora.semver;

import com.github.villadora.semver.ranges.Range;

public class SemVer {

	// =======================
	// Version
	// =======================

	/**
	 * Tell whether a version is valid
	 * 
	 * @param version
	 * @return
	 */
	public static boolean valid(String version) {
		return Version.valid(version);
	}

	public static Version version(String version) {
		return new Version(version);
	}

	/**
	 * Compare two versions
	 * 
	 * @param versionA
	 * @param versionB
	 * @return
	 */
	public static int compare(String versionA, String versionB) {
		checkVersionArg(versionA, versionB);

		return new Version(versionA).compareTo(new Version(versionB));
	}

	/**
	 * Return true if two versions are equal, otherwise false
	 * 
	 * @param versionA
	 * @param versionB
	 * @return
	 */
	public static boolean eq(String versionA, String versionB) {
		checkVersionArg(versionA, versionB);

		return new Version(versionA).compareTo(new Version(versionB)) == 0;
	}

	/**
	 * Return true if two versions are not equal
	 * 
	 * @param versionA
	 * @param versionB
	 * @return
	 */
	public static boolean neq(String versionA, String versionB) {
		return !eq(versionA, versionB);
	}

	/**
	 * Returen true if versionA is greater than versionB
	 * 
	 * @param versionA
	 * @param versionB
	 * @return
	 */
	public static boolean gt(String versionA, String versionB) {
		checkVersionArg(versionA, versionB);
		return new Version(versionA).compareTo(new Version(versionB)) > 0;
	}

	/**
	 * Return true if versionA is greater than or equal to versionB
	 * 
	 * @param versionA
	 * @param versionB
	 * @return
	 */
	public static boolean gte(String versionA, String versionB) {
		checkVersionArg(versionA, versionB);
		return new Version(versionA).compareTo(new Version(versionB)) >= 0;
	}

	/**
	 * Return true if versionA is less than versionB, otherwise return false
	 * 
	 * @param versionA
	 * @param versionB
	 * @return
	 */
	public static boolean lt(String versionA, String versionB) {
		checkVersionArg(versionA, versionB);
		return new Version(versionA).compareTo(new Version(versionB)) < 0;
	}

	/**
	 * Return true if versionA is less than or equal to versionB, otherwise
	 * return false
	 * 
	 * @param versionA
	 * @param versionB
	 * @return
	 */
	public static boolean lte(String versionA, String versionB) {
		checkVersionArg(versionA, versionB);
		return new Version(versionA).compareTo(new Version(versionB)) <= 0;
	}

	// =======================
	// Range
	// =======================

	/**
	 * Return true if the range is valid
	 * 
	 * @param range
	 * @return
	 */
	public static boolean rangeValid(String range) {
		return Range.valid(range);
	}

	public static Range range(String range) {
		return Range.valueOf(range);
	}

	/**
	 * @see #satisfies(Version, Range)
	 * @param version
	 * @param range
	 * @return
	 */
	public static boolean satisfies(String version, String range) {
		return satisfies(new Version(version), Range.valueOf(range));
	}

	/**
	 * Return true if the version satisfies the range.
	 * 
	 * @param version
	 * @param range
	 * @return
	 */
	public static boolean satisfies(Version version, Range range) {
		return range.satisfies(version);
	}

	private static void checkVersionArg(String... versions) {
		for (String version : versions)
			if (!Version.valid(version))
				throw new IllegalArgumentException("Invalid version: " + version);
	}
}
