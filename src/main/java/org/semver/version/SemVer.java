package org.semver.version;

public class SemVer {
	public static boolean valid(String version) {
		return Version.valid(version);
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
	 * Whether two versions are equal
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
	 * @param versionA
	 * @param versionB
	 * @return
	 */
	public static boolean neq(String versionA, String versionB) {
		return !eq(versionA, versionB);
	}

	public static boolean gt(String versionA, String versionB) {
		checkVersionArg(versionA, versionB);
		return new Version(versionA).compareTo(new Version(versionB)) > 0;
	}

	public static boolean gte(String versionA, String versionB) {
		checkVersionArg(versionA, versionB);
		return new Version(versionA).compareTo(new Version(versionB)) >= 0;
	}

	public static boolean lt(String versionA, String versionB) {
		checkVersionArg(versionA, versionB);
		return new Version(versionA).compareTo(new Version(versionB)) < 0;
	}

	public static boolean lte(String versionA, String versionB) {
		checkVersionArg(versionA, versionB);
		return new Version(versionA).compareTo(new Version(versionB)) <= 0;
	}

	private static void checkVersionArg(String... versions) {
		for (String version : versions)
			if (!Version.valid(version))
				throw new IllegalArgumentException("Invalid version: " + version);
	}
}
