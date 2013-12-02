package org.semver.version.regexp;

import java.util.regex.Pattern;

public class RangeRegExps {

	// ==========================
	// Range Regular Expression
	// ==========================

	protected static final String OR_SPLIT = "\\s*\\|\\|\\s*";

	protected static final String GTLT = "((?:<|>)?=?)";

	protected static final String XRANGE_ID = VersionRegExps.NUMERIC_ID + "|x|X|\\*";

	protected static final String XRANGE = "[v=\\s]*(" + XRANGE_ID + ")" + "(?:\\.(" + XRANGE_ID + ")" + "(?:\\.("
			+ XRANGE_ID + ")" + "(?:(" + VersionRegExps.PRERELEASE + ")" + ")?)?)?";

	protected static final String LONE_TILDE = "(?:~>?)";
	protected static final String LONE_CARET = "(?:\\^)";

	protected static final String TILDE_TRIM = "(?:\\s*~>?\\s*)";
	protected static final String TILDE_RANGE = "^" + LONE_TILDE + "(" + VersionRegExps.MAIN_VERSION_NOCAP
			+ VersionRegExps.PRERELEASE_NOCAP + "?)";

	protected static final String CARET_TRIM = "";

	protected static final String COMPARATOR = "^" + GTLT + "\\s*(" + VersionRegExps.FULL_NOCAP + ")$|^$";

	protected static final String HYPHEN_RANGE = "^\\s*(" + VersionRegExps.FULL_NOCAP + ")\\s+-\\s+("
			+ VersionRegExps.FULL_NOCAP + ")\\s*$";

	protected static final String STAR = "(<|>)?=?\\s*\\*";

	protected static final String RANGE = "(?:" + XRANGE + ")|(?:" + VersionRegExps.FULL + ")|(?:" + COMPARATOR
			+ ")|(?:" + TILDE_RANGE + ")|(?:" + HYPHEN_RANGE + ")";

	protected static final String AND_RANGE = RANGE + "(?: " + RANGE + ")*";

	protected static final String OR_RANGE = AND_RANGE + "(?:\\s*\\|\\|\\s*" + AND_RANGE + ")*";

	/**
	 * RegExp
	 */
	public static final Pattern AND_REG = Pattern.compile(" ");

	public static final Pattern OR_REG = Pattern.compile(OR_SPLIT);

	public static final Pattern SPECIFIC_REG = VersionRegExps.FULL_REG;

	public static final Pattern COMPARATOR_REG = Pattern.compile(COMPARATOR);

	public static final Pattern HYPHEN_RANGE_REG = Pattern.compile(HYPHEN_RANGE);

	public static final Pattern TILDE_RANGE_REG = Pattern.compile("^" + LONE_TILDE + VersionRegExps.MAIN_VERSION
			+ VersionRegExps.PRERELEASE + "?");

	public static final Pattern RANEG_REG = Pattern.compile("^" + RANGE + "$");

	public static final Pattern VALID_RANGE_REG = Pattern.compile("^" + OR_RANGE + "$");
}
