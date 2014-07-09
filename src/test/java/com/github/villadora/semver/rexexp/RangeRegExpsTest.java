package com.github.villadora.semver.rexexp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

import com.github.villadora.semver.regexp.RangeRegExps;

@RunWith(JUnit4ClassRunner.class)
public class RangeRegExpsTest {

    @Test
    public void testMatch() {
        assertThat(RangeRegExps.COMPARE_RANGE_REG.matcher(" >1.0.0").matches(), is(true));
        assertThat(RangeRegExps.COMPARE_RANGE_REG.matcher(">= v1.0.0").matches(), is(true));
        assertThat(RangeRegExps.COMPARE_RANGE_REG.matcher("<1.0.0").matches(), is(true));
        assertThat(RangeRegExps.COMPARE_RANGE_REG.matcher(" <= 1.0.0").matches(), is(true));

        assertThat(RangeRegExps.HYPHEN_RANGE_REG.matcher(" 1.0.0 -  \t1.3.0").matches(), is(true));
        assertThat(RangeRegExps.HYPHEN_RANGE_REG.matcher("1.3 - 1.4.4").matches(), is(true));

        assertThat(RangeRegExps.SIMPLE_RANGE_REG.matcher("1.3.0-pre").matches(), is(true));
        assertThat(RangeRegExps.SIMPLE_RANGE_REG.matcher("1.3.0").matches(), is(true));
        assertThat(RangeRegExps.SIMPLE_RANGE_REG.matcher("2.0.10").matches(), is(true));

        assertThat(RangeRegExps.X_RANGE_REG.matcher("2").matches(), is(true));
        assertThat(RangeRegExps.X_RANGE_REG.matcher("1.x").matches(), is(true));
        assertThat(RangeRegExps.X_RANGE_REG.matcher("1.0.*").matches(), is(true));
        assertThat(RangeRegExps.X_RANGE_REG.matcher("2.X").matches(), is(true));

        assertThat(RangeRegExps.TILDE_RANGE_REG.matcher(" ~1.2.3").matches(), is(true));
        assertThat(RangeRegExps.TILDE_RANGE_REG.matcher("~ 1.2").matches(), is(true));
        assertThat(RangeRegExps.TILDE_RANGE_REG.matcher("~> 1").matches(), is(true));

        assertThat(RangeRegExps.CARET_RANGE_REG.matcher("^1").matches(), is(true));
        assertThat(RangeRegExps.CARET_RANGE_REG.matcher("^ 1.0").matches(), is(true));
        assertThat(RangeRegExps.CARET_RANGE_REG.matcher("^ 1.0.3").matches(), is(true));

    }

    @Test
    public void testFind() {
        assertThat(RangeRegExps.X_RANGE_REG.matcher("1.* || >= 3.0.0").find(), is(true));
    }
}
