package org.semver.version.ranges;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

@RunWith(JUnit4ClassRunner.class)
public class BaseRangeTest {

	@Test
	public void testGreaterRange() {
		BaseRange range = Ranges.generate("> 1.0.0");
		assertThat(range.satisfies("1.0.0"), is(false));
		assertThat(range.satisfies("1.0.1"), is(true));
		assertThat(range.satisfies("1.0.1-pre"), not(is(false)));
	}

	@Test
	public void testHyphenRange() {
		BaseRange range = Ranges.generate("1.2.3 - 2.3.4");
		assertThat(range.satisfies("1.2.4"), is(true));
		assertThat(range.satisfies("1.2.3"), is(true));
		assertThat(range.satisfies("2.2.3"), is(true));
		assertThat(range.satisfies("2.4.3"), is(false));
	}
}
