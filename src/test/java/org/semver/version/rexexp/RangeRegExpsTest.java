package org.semver.version.rexexp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.semver.version.regexp.RangeRegExps;

@RunWith(JUnit4ClassRunner.class)
public class RangeRegExpsTest {

	@Test
	public void testRegExps() {
		assertThat(RangeRegExps.COMPARE_RANGE_REG.matcher(" >1.0.0 ").matches(), is(true));
		assertThat(RangeRegExps.HYPHEN_RANGE_REG.matcher(" 1.0.0 -  \t1.3.0 ").matches(), is(true));
		assertThat(RangeRegExps.SIMPLE_RANGE_REG.matcher("1.3.0-pre").matches(), is(true));
		assertThat(RangeRegExps.X_RANGE_REG.matcher("1.x.*").matches(), is(true));
		assertThat(RangeRegExps.TILDE_RANGE_REG.matcher("~1.2").matches(), is(true));
	}
}
