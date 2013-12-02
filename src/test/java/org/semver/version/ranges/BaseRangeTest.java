package org.semver.version.ranges;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

@RunWith(JUnit4ClassRunner.class)
public class BaseRangeTest {

	@Test
	public void testRange() {
		BaseRange range = Ranges.generate("> 1.0.0");
		assertThat(range.satisfies("1.0.0"), is(false));
		assertThat(range.satisfies("1.0.1"), is(true));
		assertThat(range.satisfies("1.0.1-pre"), not(is(false)));
	}
}
