package org.semver.version.ranges;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.semver.version.Version;

@RunWith(JUnit4ClassRunner.class)
public class EmptyRangeTest {

	@Test
	public void testRange() {
		BaseRange range = Ranges.empty();

		assertThat(range.satisfies(new Version("0.0.1")), is(false));
		assertThat(range.maxSatisfying("0.0.0", "0.2.4", "1.3.4-beta"), nullValue());
		assertThat(range.greater("1.0.0"), is(false));
		assertThat(range.less("1.0.0"), is(false));
		assertThat(range.outside("1.0.0"), is(true));
	}

}
