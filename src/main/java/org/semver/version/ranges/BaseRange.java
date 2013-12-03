package org.semver.version.ranges;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.semver.version.Version;

public class BaseRange {

	protected boolean minClose;
	protected boolean maxClose;

	protected Version min;
	protected Version max;

	public static Version MIN = new Version(-1, -1, -1, new String[] { "0" });

	public static Version MAX = new Version(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

	protected BaseRange() {
		this(MAX, MIN);
	}

	protected BaseRange(String start, String end) {
		this(new Version(start), new Version(end));
	}

	protected BaseRange(Version start, Version end) {
		this(start, end, false, false);
	}

	protected BaseRange(String start, String end, boolean startClose, boolean endClose) {
		this(new Version(start), new Version(end), startClose, endClose);
	}

	protected BaseRange(Version start, Version end, boolean startClose, boolean endClose) {
		this.min = start;
		this.max = end;
		this.minClose = startClose;
		this.maxClose = endClose;
	}

	public boolean satisfies(Version version) {
		if (version == null)
			return false;

		if (min.compareTo(max) > 0) // empty
			return false;

		return (minClose ? min.compareTo(version) <= 0 : min.compareTo(version) < 0)
				&& (maxClose ? max.compareTo(version) >= 0 : max.compareTo(version) > 0);
	}

	public boolean outside(Version version) {
		if (version == null)
			return true;

		if (min.compareTo(max) > 0) // empty
			return true;

		return greater(version) || less(version);
	}

	public boolean greater(Version version) {
		if (version == null)
			return true;

		if (min.compareTo(max) > 0) // empty
			return false;

		return minClose ? min.compareTo(version) > 0 : min.compareTo(version) >= 0;
	}

	public boolean less(Version version) {
		if (version == null)
			return false;

		if (min.compareTo(max) > 0) // empty
			return false;

		return maxClose ? max.compareTo(version) < 0 : max.compareTo(version) <= 0;
	}

	public boolean satisfies(String version) {
		return satisfies(new Version(version));
	}

	public Version maxSatisfying(Version... versions) {
		return maxSatisfying(Arrays.asList(versions));
	}

	public Version maxSatisfying(String... versions) {
		List<Version> list = new LinkedList<Version>();
		for (String version : versions) {
			list.add(new Version(version));
		}
		return maxSatisfying(list);
	}

	public Version maxSatisfying(List<Version> versions) {
		List<Version> list = new LinkedList<Version>(versions);
		Collections.sort(list);
		for (int i = list.size() - 1; i >= 0; --i) {
			Version ver = list.get(i);
			if (satisfies(ver))
				return ver;
		}
		return null;
	}

	public boolean outside(String version) {
		return outside(new Version(version));
	}

	public boolean greater(String version) {
		return greater(new Version(version));
	}

	public boolean less(String version) {
		return less(new Version(version));
	}

	@Override
	public String toString() {
		if (min == MIN && max == MAX)
			return ">0.0.0 <0.0.0";

		if (min == MIN) {
			return maxClose ? "<=" : "<" + max.toString();
		} else if (max == MAX) {
			return minClose ? ">=" : ">" + min.toString();
		}

		return (maxClose ? "<=" : "<") + max.toString() + " " + (minClose ? ">=" : ">") + min.toString();
	}
}
