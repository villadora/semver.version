package com.github.villadora.semver.ranges;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

@RunWith(JUnit4ClassRunner.class)
public class RangeTest {

    @Test
    public void testCaret() {
        Range range = Range.valueOf("^1");

        assertThat(range, notNullValue());
        assertThat(range.satisfies("1.0.0-0"), is(true));
        assertThat(range.satisfies("1.2.0"), is(true));
        assertThat(range.satisfies("2.0.0"), is(false));
        assertThat(range.satisfies("2.2.0"), is(false));

        range = Range.valueOf("^0.1.2");

        assertThat(range, notNullValue());
        assertThat(range.satisfies("0.1.2-a"), is(true));
        assertThat(range.satisfies("0.1.3"), is(true));
        assertThat(range.satisfies("0.3.3"), is(false));
    }

    @Test
    public void testTilde() {
        Range range = Range.valueOf("~ 1.0.2");

        assertThat(range, notNullValue());
        assertThat(range.satisfies("1.0.2-alpha"), is(true));
        assertThat(range.satisfies("1.0.3+build"), is(true));
        assertThat(range.satisfies("1.0.3"), is(true));
        assertThat(range.satisfies("1.1.1"), is(false));
        assertThat(range.satisfies("1.1.3"), is(false));
        assertThat(range.satisfies("1.2.1"), is(false));
        assertThat(range.satisfies("1.1.0-0"), is(false));
        assertThat(range.satisfies("2.0.1"), is(false));
    }

    @Test
    public void testGreaterRange() {
        Range range = Range.valueOf("> 1.0.0");
        assertThat(range.satisfies("1.0.0"), is(false));
        assertThat(range.satisfies("1.0.1"), is(true));
        assertThat(range.satisfies("1.0.1-pre"), not(is(false)));

        range = Range.valueOf("<1");
        assertThat(range.satisfies("1.0.0-0"), is(false));
        assertThat(range.satisfies("0.10.0-pre"), is(true));
    }

    @Test
    public void testGreaterEqualRange() {
        Range range = Range.valueOf(">= 1.0.0");
        assertThat(range.satisfies("1.0.0"), is(true));
        assertThat(range.satisfies("1.0.1"), is(true));
        assertThat(range.satisfies("1.0.0-pre"), is(false));

        range = Range.valueOf(">= 1.2");
        assertThat(range.satisfies("1.2.0-0"), is(true));
        assertThat(range.satisfies("1.1.10"), is(false));
        assertThat(range.satisfies("1.4.0"), is(true));
    }

    @Test
    public void testInvalid() {
        String[] invalids = new String[] { "> = 1.0.0", ">< 1.0.0", ">= 1.x", "<1.2.*" };

        for (String invalid : invalids) {
            try {
                Range.valueOf(invalid);
                Assert.fail(invalid + " is valid.");
            } catch (IllegalArgumentException e) {
                Assert.assertTrue(e instanceof IllegalArgumentException);
            }
        }
    }

    @Test
    public void testHyphenRange() {
        Range range = Range.valueOf("1.2.3 - 2.3.4");
        assertThat(range.satisfies("1.2.4"), is(true));
        assertThat(range.satisfies("1.2.3"), is(true));
        assertThat(range.satisfies("2.2.3"), is(true));
        assertThat(range.satisfies("2.4.3"), is(false));
    }
}
