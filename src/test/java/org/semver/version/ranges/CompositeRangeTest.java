package org.semver.version.ranges;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

@RunWith(JUnit4ClassRunner.class)
public class CompositeRangeTest {

	@Test
	public void testAndRange() {
		BaseRange range = Ranges.generate(">1.0.0 <= 2.0");
		assertThat(range, nullValue());
		assertThat(range, notNullValue());
	}
}
