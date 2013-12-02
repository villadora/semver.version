package org.semver.version.ranges;

import org.semver.version.Version;

public class SpecificRange extends BaseRange {

	public SpecificRange(String version) {
		this(new Version(version));
	}

	public SpecificRange(Version version) {
		super(version, version, true, true);
	}
}
