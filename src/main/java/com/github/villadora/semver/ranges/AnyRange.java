package com.github.villadora.semver.ranges;

import com.github.villadora.semver.Version;

public class AnyRange extends Range {

	protected static final AnyRange RANGE = new AnyRange();

	private AnyRange() {
		super(Range.MIN, Range.MAX);
	}

	public boolean satisfies(String version) {
		return true;
	}

	public boolean satisfies(Version version) {
		return true;
	}

	public boolean outside(String version) {
		return false;
	}

	public boolean outside(Version version) {
		return false;
	}

	public boolean greater(String version) {
		return false;
	}

	public boolean greater(Version version) {
		return false;
	}

	public boolean less(String version) {
		return false;
	}

	public boolean less(Version version) {
		return false;
	}

	@Override
	public String toString() {
		return ">=0.0.0-0";
	}
}
