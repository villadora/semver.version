package org.semver.version.ranges;

import java.util.List;

import org.semver.version.Version;

class EmptyRange extends BaseRange {

	protected static final EmptyRange RANGE = new EmptyRange();

	private EmptyRange() {
		super(BaseRange.MAX, BaseRange.MIN);
	}

	public boolean satisfies(String version) {
		return false;
	}

	public boolean satisfies(Version version) {
		return false;
	}

	public Version maxSatisfying(Version... versions) {
		return null;
	}

	public Version maxSatisfying(String... versions) {
		return null;
	}

	public Version maxSatisfying(List<Version> versions) {
		return null;
	}

	public boolean outside(String version) {
		return true;
	}

	public boolean outside(Version version) {
		return true;
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
		return "> 0.0.0 < 0.0.0";
	}
}
