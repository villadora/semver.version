package org.semver.version.ranges;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Assert;
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

		range = Ranges.generate("<1");
		assertThat(range.satisfies("1.0.0-0"), is(false));
		assertThat(range.satisfies("0.10.0-pre"), is(true));
	}

	@Test
	public void testGreaterEqualRange() {
		BaseRange range = Ranges.generate(">= 1.0.0");
		assertThat(range.satisfies("1.0.0"), is(true));
		assertThat(range.satisfies("1.0.1"), is(true));
		assertThat(range.satisfies("1.0.0-pre"), is(false));

		range = Ranges.generate(">= 1.2");
		assertThat(range.satisfies("1.2.0-0"), is(true));
		assertThat(range.satisfies("1.1.10"), is(false));
		assertThat(range.satisfies("1.4.0"), is(true));
	}

	@Test
	public void testInvalid() {
		String[] invalids = new String[] { "> = 1.0.0", ">< 1.0.0", ">= 1.x", "<1.2.*" };

		for (String invalid : invalids) {
			try {
				Ranges.generate(invalid);
				Assert.fail(invalid + " is valid.");
			} catch (IllegalArgumentException e) {
				Assert.assertTrue(e instanceof IllegalArgumentException);
			}
		}
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
