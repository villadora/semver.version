package org.semver.version.ranges;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

import org.semver.version.Version;
import org.semver.version.regexp.RangeRegExps;

public class Ranges {

	public static boolean valid(String range) {
		return RangeRegExps.VALID_RANGE_REG.matcher(range).matches();
	}

	public static BaseRange empty() {
		return EmptyRange.RANGE;
	}

	public static BaseRange compositeOr(String... rgs) {
		List<BaseRange> ranges = new LinkedList<BaseRange>();
		for (String rg : rgs) {
			ranges.add(generate(rg));
		}
		return new CompositRange(ranges, false);
	}

	public static BaseRange compositeAnd(String... rgs) {
		List<BaseRange> ranges = new LinkedList<BaseRange>();
		for (String rg : rgs) {
			ranges.add(generate(rg));
		}
		return new CompositRange(ranges, true);
	}

	public static BaseRange generate(String range) {
		range = range.trim();
		if (!valid(range))
			throw new IllegalArgumentException("Invalid range: " + range);

		String[] rgs = RangeRegExps.OR_REG.split(range);

		if (rgs.length == 0)
			return empty();
		else if (rgs.length > 1) {
			// composite range
			return compositeOr(rgs);
		}

		// single range string
		rgs = RangeRegExps.AND_REG.split(rgs[0]);

		Matcher matcher = RangeRegExps.SPECIFIC_REG.matcher(range);
		if (matcher.matches()) {
			return new SpecificRange(range);
		}

		matcher = RangeRegExps.COMPARATOR_REG.matcher(range);
		if (matcher.matches()) {
			String comp = matcher.group(1), version = matcher.group(2);

			if (comp.equals(">")) {
				return greaterThan(version);
			} else if (comp.equals("<"))
				return lessThan(version);
			else if (comp.equals(">="))
				return greaterAndEqualThan(version);
			else if (comp.equals("<="))
				return lessAndEqualThan(version);

			throw new IllegalArgumentException("Unknown comparator: " + comp);
		}

		matcher = RangeRegExps.HYPHEN_RANGE_REG.matcher(range);
		if (matcher.matches()) {
			String min = matcher.group(1), max = matcher.group(2);
			return new BaseRange(min, max, true, true);
		}

		return null;
	}

	public static BaseRange greaterThan(String version) {
		return greaterThan(new Version(version));
	}

	public static BaseRange greaterThan(Version version) {
		return new BaseRange(version, BaseRange.MAX);
	}

	public static BaseRange lessThan(String version) {
		return lessThan(new Version(version));
	}

	public static BaseRange lessThan(Version version) {
		return new BaseRange(BaseRange.MIN, version);
	}

	public static BaseRange greaterAndEqualThan(String version) {
		return greaterAndEqualThan(new Version(version));
	}

	public static BaseRange greaterAndEqualThan(Version version) {
		return new BaseRange(version, BaseRange.MAX, true, false);
	}

	public static BaseRange lessAndEqualThan(String version) {
		return lessThan(new Version(version));
	}

	public static BaseRange lessAndEqualThan(Version version) {
		return new BaseRange(BaseRange.MIN, version, false, true);
	}

}
