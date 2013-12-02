package org.semver.version;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.semver.version.ranges.BaseRange;
import org.semver.version.ranges.Ranges;

/**
 * Version range, currently support: ~1.0.2,
 * 
 * @author villa
 * 
 */
public class Range {

	private BaseRange innerRange;

	public static boolean valid(String range) {
		return Ranges.valid(range);
	}

	public Range(String range) {
		this.innerRange = Ranges.generate(range);
	}

	/**
	 * @see #satisfies(Version)
	 * @param version
	 * @return
	 */
	public boolean satisfies(String version) {
		return satisfies(new Version(version));
	}

	/**
	 * Return true if the version satisfies the range.
	 * 
	 * @param version
	 * @return
	 */

	public boolean satisfies(Version version) {
		return innerRange.satisfies(version);
	}

	public boolean outside(String version) {
		return outside(new Version(version));
	}

	public boolean outside(Version version) {
		return innerRange.outside(version);
	}

	public String maxSatisfying(String... versions) {
		List<Version> vers = new LinkedList<Version>();
		for (String ver : versions) {
			vers.add(new Version(ver));
		}

		return maxSatisfying(vers).toString();
	}

	public Version maxSatisfying(Version... versions) {
		return maxSatisfying(Arrays.asList(versions));
	}

	public Version maxSatisfying(List<Version> versions) {
		return innerRange.maxSatisfying(versions);
	}

	public boolean greater(Version version) {
		return innerRange.greater(version);
	}

	public boolean greater(String version) {
		return greater(new Version(version));
	}

	public boolean less(Version version) {
		return innerRange.less(version);
	}

	public boolean less(String version) {
		return less(new Version(version));
	}
}
