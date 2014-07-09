package com.github.villadora.semver.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

import com.github.villadora.semver.ranges.Range;

@RunWith(JUnit4ClassRunner.class)
public class RangeParserTest {

    @Test
    public void testCompositeAnd() {
        Range range = RangeParser.parse("1.* <=1.4.0");

        assertThat(range, notNullValue());
        assertThat(range.satisfies("1.0.0-0"), is(true));
        assertThat(range.satisfies("1.4.0"), is(true));
        assertThat(range.satisfies("1.3.1"), is(true));
    }

    @Test
    public void testCompositeOr() {
        Range range = RangeParser.parse("1.* || >=3.0.0");

        assertThat(range, notNullValue());
        assertThat(range.satisfies("1.0.0-0"), is(true));
        assertThat(range.satisfies("2.0.0-0"), is(false));
        assertThat(range.satisfies("3.0.1"), is(true));
    }

    @Test
    public void testParen() {
        Range range = RangeParser.parse("( 1.* ) ");

        assertThat(range, notNullValue());
        assertThat(range, equalTo(RangeParser.parse("1.*")));
        assertThat(range.satisfies("1.0.0-0"), is(true));
        assertThat(range.satisfies("2.0.0-0"), is(false));
    }

    @Test
    public void testXRange() {
        Range range = RangeParser.parse("1.*");
        assertThat(range, notNullValue());
        assertThat(range.satisfies("1.0.0-0"), is(true));
        assertThat(range.satisfies("2.0.0-0"), is(false));

        range = RangeParser.parse("1");
        assertThat(range.equals(Range.any()), is(false));
    }

    @Test
    public void testSimple() {
        Range range = RangeParser.parse("1.0.0");
        assertThat(range, notNullValue());
        assertThat(range.satisfies("1.0.0+build"), is(true));
    }

    @Test
    public void testHyphen() {
        Range range = RangeParser.parse("1.0 -  1.2.0");
        assertThat(range, notNullValue());
        assertThat(range.satisfies("1.1.0-0"), is(true));
    }

    @Test
    public void testParseCompare() {
        Range range = RangeParser.parse(">= 1.0.0");
        assertThat(range, notNullValue());
        assertThat(range.satisfies("1.0.0"), is(true));

        range = RangeParser.parse("<= 1.4.0");
        assertThat(range.satisfies("1.4.0"), is(true));

    }

    @Test
    public void testParseTilde() {
        Range range = new RangeParser("~ 1.0.2 ").parse();

        assertThat(range, notNullValue());
        assertThat(range.satisfies("1.0.2"), is(true));
        assertThat(range.satisfies("1.0.3"), is(true));
        assertThat(range.satisfies("1.1.0-0"), is(false));
        assertThat(range.satisfies("2.0.0"), is(false));
    }

    @Test
    public void testParseCaret() {
        Range range = new RangeParser("^ 1.0.3").parse();

        assertThat(range, notNullValue());
        assertThat(range.satisfies("1.0.0"), is(false));
        assertThat(range.satisfies("1.0.3"), is(true));
        assertThat(range.satisfies("1.1.3"), is(true));
        assertThat(range.satisfies("2.1.3"), is(false));
    }
}
