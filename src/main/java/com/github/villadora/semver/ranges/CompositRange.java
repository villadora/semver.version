package com.github.villadora.semver.ranges;

import java.util.Arrays;
import java.util.List;

import com.github.villadora.semver.Version;

class CompositRange extends Range {

	private boolean and = false;
	private List<Range> ranges;

	public CompositRange(Range... ranges) {
		this(Arrays.asList(ranges), false);
	}

	public CompositRange(boolean and, Range... ranges) {
		this(Arrays.asList(ranges), and);
	}

	public CompositRange(List<Range> ranges) {
		this(ranges, false);
	}

	public CompositRange(List<Range> ranges, boolean and) {
		boolean maxClose = false, minClose = false;
		Version max = null, min = null;

		for (Range range : ranges) {
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
		for (Range range : ranges) {
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

	@Override
	public int hashCode() {
		return 31 * super.hashCode() + ranges.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;

		if (o instanceof CompositRange) {
			CompositRange that = (CompositRange) o;
			if (!super.equals(that))
				return false;

			if (and != that.and)
				return false;

			if (ranges.size() != that.ranges.size())
				return false;

			for (int i = 0; i < ranges.size(); i++) {
				if (!ranges.get(i).equals(that.ranges.get(i)))
					return false;
			}
			return true;
		}

		return false;
	}

}