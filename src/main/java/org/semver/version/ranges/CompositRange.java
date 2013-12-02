package org.semver.version.ranges;

import java.util.Arrays;
import java.util.List;

import org.semver.version.Version;

class CompositRange extends BaseRange {

	private boolean and = false;
	private List<BaseRange> ranges;

	public CompositRange(BaseRange... ranges) {
		this(Arrays.asList(ranges), false);
	}

	public CompositRange(boolean and, BaseRange... ranges) {
		this(Arrays.asList(ranges), and);
	}

	public CompositRange(List<BaseRange> ranges) {
		this(ranges, false);
	}

	public CompositRange(List<BaseRange> ranges, boolean and) {
		boolean maxClose = false, minClose = false;
		Version max = null, min = null;

		for (BaseRange range : ranges) {
			if (max == null) {
				max = range.max;
				maxClose = range.maxClose;
			} else {
				if ((!and && (maxClose ? max.compareTo(range.max) < 0 : max.compareTo(range.max) <= 0))
						|| (and && (maxClose ? max.compareTo(range.max) > 0 : max.compareTo(range.max) >= 0))) {
					max = range.max;
					maxClose = range.maxClose;
				}
			}

			if (min == null) {
				min = range.min;
				minClose = range.minClose;
			} else {
				if ((!and && (minClose ? min.compareTo(range.min) > 0 : min.compareTo(range.min) >= 0))
						|| (and && (minClose ? min.compareTo(range.min) < 0 : min.compareTo(range.min) <= 0))) {
					min = range.min;
					minClose = range.minClose;
				}
			}
		}

		this.ranges = ranges;
		this.and = and;
		this.min = min;
		this.max = max;
		this.minClose = minClose;
		this.maxClose = maxClose;
	}

	@Override
	public boolean satisfies(Version version) {
		for (BaseRange range : ranges) {
			if (and) {
				if (!range.satisfies(version))
					return false;
			} else {
				if (range.satisfies(version))
					return true;
			}

		}
		return and ? true : false;
	}
}