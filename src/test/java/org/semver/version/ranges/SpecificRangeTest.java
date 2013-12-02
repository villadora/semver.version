package org.semver.version.ranges;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.semver.version.Version;

@RunWith(JUnit4ClassRunner.class)
public class SpecificRangeTest {

	@Test
	public void testRange() {
		BaseRange range = Ranges.generate("1.0.0");

		assertThat(range.satisfies("1.0.0-prerelease"), is(false));
		assertThat(range.satisfies("1.0.0+build"), is(true));

		assertThat(range.maxSatisfying("1.0.0-prerelease", "1.2.0", "0.0.9"), nullValue());
		assertThat(range.maxSatisfying("1.0.0+build", "1.2.0", "0.0.9"), equalTo(new Version("1.0.0+build")));

		assertThat(range.greater("1.0.0-prerelease"), is(true));
		assertThat(range.greater("1.0.1"), is(false));
		assertThat(range.greater("1.0.0"), is(false));

		assertThat(range.less("1.0.0-prerelease"), is(false));
		assertThat(range.less("1.0.1"), is(true));
		assertThat(range.less("1.0.0"), is(false));

		assertThat(range.outside("1.0.0-prerelease"), is(true));
		assertThat(range.outside("1.0.0"), is(false));
	}
}
