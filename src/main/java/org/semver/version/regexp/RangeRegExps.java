package org.semver.version.regexp;

import static org.semver.version.regexp.VersionRegExps.*;

import java.util.regex.Pattern;

public class RangeRegExps {

	// ==========================
	// Range Regular Expression
	// ==========================

	protected static final String GTLT = "((?:<|>)=?)";

	protected static final String XRANGE_ID = "(?:" + NUMERIC_ID + "|x|X|\\*)";

	protected static final String TILDE = "(?:~>?)";

	protected static final String SHORT_VERSION = "(?:" + NUMERIC_ID + "(?:\\." + NUMERIC_ID + ")*)";

	protected static final String EXACT_VERSION = "(?:" + MAIN_VERSION_NOCAP + "(?:" + PRERELEASE_NOCAP + ")?)";

	protected static final String IMPRECISE_VERSION = "((?:" + EXACT_VERSION + ")|(?:" + SHORT_VERSION + "))";

	protected static final String SIMPLE_RANGE = IMPRECISE_VERSION;

	protected static final String TILDE_RANGE = "(?:" + TILDE + "\\s*[v=]?\\s*" + IMPRECISE_VERSION + ")";

	protected static final String COMPARE_RANGE = "(?:" + GTLT + "\\s*[v]?\\s*" + IMPRECISE_VERSION + ")";

	protected static final String HYPHEN_RANGE = "(?:" + IMPRECISE_VERSION + "\\s*-\\s*" + IMPRECISE_VERSION + ")";

	protected static final String X_RANGE = "(?:(?:[v=]\\s*)?(?:(?:" + XRANGE_ID + ")|(?:" + XRANGE_ID + "\\."
			+ XRANGE_ID + ")|(?:" + XRANGE_ID + "\\." + XRANGE_ID + "\\." + XRANGE_ID + ")))";

	/**
	 * RegExp
	 */
	public static final Pattern SIMPLE_RANGE_REG = Pattern.compile("\\s*" + SIMPLE_RANGE + "\\s*");

	public static final Pattern COMPARE_RANGE_REG = Pattern.compile("\\s*" + COMPARE_RANGE + "\\s*");

	public static final Pattern HYPHEN_RANGE_REG = Pattern.compile("\\s*" + HYPHEN_RANGE + "\\s*");

	public static final Pattern TILDE_RANGE_REG = Pattern.compile("\\s*" + TILDE_RANGE + "\\s*");

	public static final Pattern X_RANGE_REG = Pattern.compile("\\s*" + X_RANGE + "\\s*");

}
