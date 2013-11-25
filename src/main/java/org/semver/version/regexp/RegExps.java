package org.semver.version.regexp;

import java.util.regex.Pattern;

public class RegExps {

	// =========================
	// RegExp Strings
	// =========================

	private static final String NUMERIC_ID = "0|[1-9]\\d*";

	private static final String NUMBERIC = "\\d+";

	private static final String TILDE = "(?:\\s*~>?\\s*)";

	private static final String NON_NUMERIC_ID = "\\d*[a-zA-Z-][a-zA-Z0-9-]*";

	private static final String PRERELEASE_ID = "(?:" + NUMERIC_ID + "|" + NON_NUMERIC_ID + ")";

	private static final String BUILD_ID = "[0-9A-Za-z-]+";

	private static final String MAIN_VERSION = "(?<major>" + NUMERIC_ID + ")\\.(?<minor>" + NUMERIC_ID
			+ ")\\.(?<patch>" + NUMERIC_ID + ")";

	private static final String PRERELEASE = ("(?:-(?<prerelease>" + PRERELEASE_ID + "(?:\\." + PRERELEASE_ID + ")*))");

	private static final String BUILD = "(?:\\+(?<build>" + BUILD_ID + "(?:\\." + BUILD_ID + ")*))";

	/**
	 * Regexps for version analyzing
	 */

	public static final Pattern MAIN_VERSION_REG = Pattern.compile(MAIN_VERSION);

	public static final Pattern PRERELEASE_REG = Pattern.compile(PRERELEASE);

	public static final Pattern BUILD_REG = Pattern.compile(BUILD);

	public static final Pattern FULL_REG = Pattern.compile("^" + MAIN_VERSION + PRERELEASE + "?" + BUILD + "?$");

	public static final Pattern TILDE_RANGE_REG = Pattern.compile("^" + TILDE + MAIN_VERSION + PRERELEASE + "?");

}
