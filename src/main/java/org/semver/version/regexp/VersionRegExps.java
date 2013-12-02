package org.semver.version.regexp;

import java.util.regex.Pattern;

public class VersionRegExps {

	// =========================
	// RegExp Strings
	// =========================

	protected static final String EQ_PRE = "(?:\\s*[v=]\\s*)?";

	protected static final String NUMERIC_ID = "0|[1-9]\\d*";

	protected static final String NUMBERIC = "\\\\d+";

	protected static final String NON_NUMERIC_ID = "\\d*[a-zA-Z-][a-zA-Z0-9-]*";

	protected static final String PRERELEASE_ID = "(?:" + NUMERIC_ID + "|" + NON_NUMERIC_ID + ")";

	protected static final String BUILD_ID = "[0-9A-Za-z-]+";

	protected static final String MAIN_VERSION = "(" + NUMERIC_ID + ")\\.(" + NUMERIC_ID + ")\\.(" + NUMERIC_ID + ")";

	protected static final String PRERELEASE = ("(?:-(" + PRERELEASE_ID + "(?:\\." + PRERELEASE_ID + ")*))");

	protected static final String BUILD = "(?:\\+(" + BUILD_ID + "(?:\\." + BUILD_ID + ")*))";

	protected static final String FULL = EQ_PRE + MAIN_VERSION + PRERELEASE + "?" + BUILD + "?";

	/**
	 * Regexps for version analyzing
	 */

	public static final Pattern MAIN_VERSION_REG = Pattern.compile(MAIN_VERSION);

	public static final Pattern PRERELEASE_REG = Pattern.compile("(" + PRERELEASE_ID + ")(?:\\.(" + PRERELEASE_ID
			+ "))*");

	public static final Pattern BUILD_REG = Pattern.compile("(" + BUILD_ID + "(?:\\." + BUILD_ID + ")*)");

	public static final Pattern FULL_REG = Pattern.compile("^" + EQ_PRE + MAIN_VERSION + PRERELEASE + "?" + BUILD
			+ "?$");
}
