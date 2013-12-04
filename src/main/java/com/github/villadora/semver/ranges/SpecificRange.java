package com.github.villadora.semver.ranges;

import com.github.villadora.semver.Version;

public class SpecificRange extends Range {

	public SpecificRange(String version) {
		this(new Version(version));
	}

	public SpecificRange(Version version) {
		super(version, version, true, true);
	}
}
