package com.github.villadora.semver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

import com.github.villadora.semver.ranges.Range;
import com.google.common.collect.ImmutableMap;

@RunWith(JUnit4ClassRunner.class)
public class SemVerTest {

    @Test
    public void testRangeValid() {
        String[] ranges = { "1.0.0 - 2.0.0", "1.0.0", "", "*", "*", ">=1.0.0", ">1.0.0", "<=2.0.0", "1", "<=2.0.0",
                "<=2.0.0", "<2.0.0", "<2.0.0", ">= 1.0.0", ">=  1.0.0", ">=   1.0.0", "> 1.0.0", ">  1.0.0",
                "<=   2.0.0", "<= 2.0.0", "<=  2.0.0", "<    2.0.0", "<        2.0.0", ">=0.1.97", ">=0.1.97",
                "1.x ^1.3", "0.1.20 || 1.2.4", ">=0.2.3 || <0.0.1", ">=0.2.3 || <0.0.1", ">=0.2.3 || <0.0.1", "2.x.x",
                "1.2.x", "1.2.x || 2.x", "1.2.x || 2.x", "x", "2.*.*", "1.2.*", "1.2.* || 2.*", "*", "2", "2.3",
                "~2.4", "~2.4", "~>3.2.1", "~1", "~>1", "~> 1", "~1.0", "~ 1.0", "<1", "< 1", ">=1", ">= 1", "<1.2",
                "< 1.2", "1", "^ 1", "^1.2", "^1.2.0", "~1.2.3-beta", "^ 1.2 ^ 1" };

        for (String range : ranges) {
            assertThat("Range valid: " + range, SemVer.rangeValid(range), is(true));
        }
    }

    @Test
    public void testMaxSatisfies() {

        Range range = SemVer.range("~2.0.0");
        Version version = range.maxSatisfying("1.1.0", "1.2.0", "1.2.1", "1.3.0", "2.0.0-b1", "2.0.0-b2", "2.0.0-b3",
                "2.0.0", "2.1.0");
        assertThat(version.toString(), equalTo("2.0.0"));

        range = SemVer.range("~1.2.3");
        version = range.maxSatisfying("1.2.3", "1.2.4", "1.3.1", "2.1.0");
        assertThat(version.toString(), equalTo("1.2.4"));

        range = SemVer.range("1.2");
        version = range.maxSatisfying("1.2.3", "1.2.4");
        assertThat(version.toString(), equalTo("1.2.4"));

        range = SemVer.range("~1.2.3");
        version = range.maxSatisfying("1.2.3", "1.2.4", "1.2.5", "1.2.6");
        assertThat(version.toString(), equalTo("1.2.6"));

    }

    @Test
    public void testRangeNotSatisfies() {
        Map<String, String> values = ImmutableMap.<String, String> builder().put("1.0.0 - 2.0.0", "2.2.3")
                .put("1.0.0", "1.0.1").put(">=      1.0.0", "0.0.0").put(">=  1.0.0", "0.0.1")
                .put("  >=1.0.0", "0.1.0").put(">1.0.0", "0.0.1").put(" >1.0.0", "0.1.0").put(" <=2.0.0", "3.0.0")
                .put("    <=2.0.0", "2.9999.9999").put("  <=2.0.0", "2.2.9").put("<2.0.0", "2.9999.9999")
                .put(" <2.0.0", "2.2.9").put(">=0.1.97", "v0.1.93").put("  >=0.1.97", "0.1.93")
                .put("0.1.20 || 1.2.4", "1.2.3").put(">=0.2.3 || <0.0.1", "0.0.3").put(" >=0.2.3 || <0.0.1", "0.2.2")
                .put("2.x.x", "1.1.3")
                .put("  2.x.x", "3.1.3")
                .put("1.2.x", "1.3.3")
                .put("  1.2.x || 2.x", "3.1.3")
                .put("1.2.x || 2.x", "1.1.3")
                .put("2.*.*", "1.1.3")
                .put("2.*.*  ", "3.1.3")
                .put("1.2.*  ", "1.3.3")
                .put("1.2.* || 2.*", "3.1.3")
                .put("  1.2.* || 2.*", "1.1.3")
                .put("2", "1.1.2")
                .put("2.3", "2.4.1")
                .put("~2.4", "2.5.0")
                // >=2.4.0 <2.5.0
                .put(" ~2.4", "2.3.9")
                .put("~>3.2.1", "3.3.2")
                .put("~> 3.2.1", "3.2.0")
                // >=3.2.1 <3.3.0
                .put("~1", "0.2.3")
                // >=1.0.0 <2.0.0
                .put("~>1", "2.2.3")
                .put("~1.0", "1.1.0")
                // >=1.0.0 <1.1.0
                .put("<1", "1.0.0").put(">=1.2", "1.1.1").put("1", "2.0.0-beta").put("~v0.5.4-beta", "0.5.4-alpha")
                .put("<1  ", "1.0.0-beta").put("< 1", "1.0.0-beta").put("=0.7", "0.8.2").put(">=0.7", "0.6.2")
                .put("<=0.7   ", "0.7.2").put("<1.2.3", "1.2.3-beta").put("=1.2.3", "1.2.3-beta").put(">1.2", "1.2.8")
                .put("^1.2.3", "2.0.0-pre").put("^1.2.4", "2.0.0-alpha").put("^1.3.3", "1.3.2").put("^2.2", "2.1.9")
                .build();

        for (Entry<String, String> entry : values.entrySet()) {
            String range = entry.getKey(), version = entry.getValue();

            if (!SemVer.rangeValid(range))
                System.out.println(range);

            if (!SemVer.valid(version))
                System.out.println(version);

            if (SemVer.satisfies(version, range))
                System.out.println(version + " range: " + range);

            assertThat(SemVer.satisfies(values.get(range), range), is(false));
        }
    }

    @Test
    public void testRangeSatisfies() {

        Map<String, String> values = ImmutableMap.<String, String> builder().put("1.0.0 - 2.0.0", "1.2.3")
                .put("1.0.0", "1.0.0").put("", "1.0.0").put("*", "v1.2.3-foo").put("  >=  1.0.0", "1.0.1")
                .put(">=1.0.0", "1.1.0").put(">1.0.0", "1.0.1").put("  > 1.0.0", "1.1.0").put("<=2.0.0", "2.0.0")
                .put(" <=2.0.0", "1.9999.9999").put("<=   2.0.0", "0.2.9").put("<2.0.0", "1.9999.9999")
                .put("  <2.0.0", "0.2.9").put(">= 1.0.0", "1.0.0").put(">=  1.0.0", "1.0.1").put(">=   1.0.0", "1.1.0")
                .put("> 1.0.0", "1.0.1").put(">   1.0.0", "1.1.0").put("<=   2.0.0   ", "2.0.0")
                .put(" <=   2.0.0", "1.9999.9999").put("<=  2.0.0", "0.2.9").put(" <    2.0.0", "1.9999.9999")
                .put("<\t2.0.0", "0.2.9").put("   >=0.1.97", "v0.1.97").put(">=0.1.97", "0.1.97")
                .put("0.1.20 || 1.2.4", "1.2.4").put(">=0.2.3 || <0.0.1", "0.0.0").put("   >=0.2.3 || <0.0.1", "0.2.3")
                .put(">= 0.2.3 || <0.0.1", "0.2.4").put("2.x.x", "2.1.3").put("1.2.x", "1.2.3")
                .put("    1.2.x || 2.x", "2.1.3").put(" 1.2.x || 2.x", "1.2.3").put("x", "1.2.3")
                .put("  2.*.*", "2.1.3").put("1.2.*", "1.2.3").put("1.2.X || 2.*", "2.1.3")
                .put("1.2.* || 2.*", "1.2.3").put("  *  ", "1.2.3").put("2", "2.1.2").put("2.3", "2.3.1")
                .put("~2.4", "2.4.0").put("  ~2.4", "2.4.5").put("~>3.2.1", "3.2.2").put("~1", "1.2.3")
                .put("~>1", "1.2.3").put("~> 1", "1.2.3").put("~1.0", "1.0.2").put("~ 1.0", "1.0.2")
                .put("~ 1.0.3", "1.0.12").put(">=1", "1.0.0").put(">=  1", "1.0.0").put("<1.2", "1.1.1")
                .put("< 1.2", "1.1.1").put("1", "1.0.0-beta").put("~v0.5.4-pre", "0.5.5").put("  ~v0.5.4-pre", "0.5.4")
                .put("=0.7", "0.7.2").put("  >=0.7", "0.7.2").put("=0.7   ", "0.7.0-asdf").put(">=0.7", "0.7.0-asdf")
                .put("<=0.7  ", "0.6.2").put("~   1.2.1 >=1.2.3", "1.2.3").put("~1.2.1 =1.2.3", "1.2.3")
                .put("   ~1.2.1 1.2.3", "1.2.3").put("~1.2.1 >=1.2.3 1.2.3", "1.2.3")
                .put("~1.2.1 1.2.3 >=1.2.3", "1.2.3").put("~1.2.1 1.2.3", "1.2.3").put(">=  1.2.1 1.2.3", "1.2.3")
                .put("1.2.3 >= 1.2.1", "1.2.3").put(">=1.2.3 >=1.2.1", "1.2.3").put(">=1.2.1 >=1.2.3  ", "1.2.3")
                .put("<=1.2.3", "1.2.3-beta").put(">1.2 ", "1.3.0-beta").put(">=1.2  ", "1.2.8")
                .put("^1.2.3", "1.2.3-beta").put("^0.1.2", "0.1.2").put("^0.1", "0.1.0").put("^1.2 ^1", "1.4.2")
                .put("^1.2", "1.2.0-pre").build();

        for (Entry<String, String> entry : values.entrySet()) {
            String range = entry.getKey();
            String version = entry.getValue();

            if (!SemVer.rangeValid(range))
                System.out.println(range);

            if (!SemVer.valid(version))
                System.out.println(version);

            if (!SemVer.satisfies(version, range))
                System.out.println(version + " range: " + range);

            assertThat(SemVer.satisfies(values.get(range), range), is(true));
        }
    }

    @Test
    public void testValid() {
        Map<String, Boolean> values = ImmutableMap.<String, Boolean> builder().put("1.0.0", true).put("1.01.1", false)
                .put(".0.0", false).put("1.1", false).put("1.x1.0", false).put("1.13.0", true).put("1..3", false)
                .put("v1.3.0", true).put("= 1.3.0", true).put("=1.0.0", true).put(" = 1.0.0", true).build();

        for (Entry<String, Boolean> entry : values.entrySet()) {
            assertThat("Version valid: " + entry.getKey(), SemVer.valid(entry.getKey()), is(entry.getValue()));
        }
    }

    @Test
    public void testCmp() {
        Map<String, String> values = ImmutableMap.<String, String> builder().put("0.0.0", "0.0.0-foo")
                .put("0.0.1", "0.0.0").put("1.0.0", "0.9.9").put("0.10.0", "0.9.0").put("0.99.0", "0.10.0")
                .put("=2.0.0", "1.2.3").put("v0.0.0", "0.0.0-foo").put("v0.0.1", "0.0.0").put("v1.0.0", "0.9.9")
                .put("v0.10.0", "0.9.0").put("v0.99.0", "0.10.0").put("v2.0.0", "1.2.3").put("= 0.0.0", "v0.0.0-foo")
                .put(" =0.0.1", "v0.0.0").put(" v 1.0.0", "v0.9.9").put(" =0.10.0", "v0.9.0")
                .put("v 0.99.0", "v0.10.0").put("2.0.0", "v1.2.3").put("=1.2.3", "1.2.3-asdf").put("1.2.3", "1.2.3-4")
                .put("v1.2.3", "1.2.3-4-foo").put("1.2.3-5-foo", "1.2.3-5").put("1.2.3-5", "1.2.3-4")
                .put("=1.2.3-5-foo", "1.2.3-5-Foo").put("3.0.0", "2.7.2+asdf").put("1.2.3-a.10", "1.2.3-a.5")
                .put("v1.2.3-a.b", "1.2.3-a.5").put("1.2.3-a.b", "1.2.3-a")
                .put("1.2.3-a.b.c.10.d.5", "1.2.3-a.b.c.5.d.100").build();

        for (Entry<String, String> entry : values.entrySet()) {
            String verA = entry.getKey();
            String verB = entry.getValue();

            assertThat("gt(" + verA + " , " + verB + ")", SemVer.gt(verA, verB), is(true));
            assertThat("lt(" + verB + " , " + verA + ")", SemVer.lt(verB, verA), is(true));
            assertThat("!gt(" + verB + " , " + verA + ")", SemVer.gt(verB, verA), is(false));
            assertThat("!lt(" + verA + " , " + verB + ")", SemVer.lt(verA, verB), is(false));

            assertThat("eq(" + verA + " , " + verA + ")", SemVer.eq(verA, verA), is(true));
            assertThat("eq(" + verB + " , " + verB + ")", SemVer.eq(verB, verB), is(true));

            assertThat("eq(" + verA + " , " + verB + ")", SemVer.eq(verA, verB), is(false));
            assertThat("neq(" + verA + " , " + verB + ")", SemVer.neq(verA, verB), is(true));
            assertThat("0 < cmp(" + verA + " , " + verB + ")", SemVer.compare(verA, verB), greaterThan(0));
            assertThat("0 > cmp(" + verB + " , " + verA + ")", SemVer.compare(verB, verA), lessThan(0));
        }

    }

    @Test
    public void testEqual() {
        Map<String[], Boolean> values = ImmutableMap.<String[], Boolean> builder()
                .put(new String[] { " = 1.0.0", "v1.0.0" }, true).put(new String[] { "1.2.3", "=1.2.3" }, true)
                .put(new String[] { "1.2.3", "v 1.2.3" }, true).put(new String[] { "1.2.3", "= 1.2.3" }, true)
                .put(new String[] { "1.2.3", " v1.2.3" }, true).put(new String[] { "1.2.3", " =1.2.3" }, true)
                .put(new String[] { "1.2.3", " v 1.2.3" }, true).put(new String[] { "1.2.3", " = 1.2.3" }, true)
                .put(new String[] { "1.2.3-0", "v1.2.3-0" }, true).put(new String[] { "1.2.3-0", "=1.2.3-0" }, true)
                .put(new String[] { "1.2.3-0", "v 1.2.3-0" }, true).put(new String[] { "1.2.3-0", "= 1.2.3-0" }, true)
                .put(new String[] { "1.2.3-0", " v1.2.3-0" }, true).put(new String[] { "1.2.3-0", " =1.2.3-0" }, true)
                .put(new String[] { "1.2.3-0", " v 1.2.3-0" }, true)
                .put(new String[] { "1.2.3-0", " = 1.2.3-0" }, true).put(new String[] { "1.2.3-1", "v1.2.3-1" }, true)
                .put(new String[] { "1.2.3-1", "=1.2.3-1" }, true).put(new String[] { "1.2.3-1", "v 1.2.3-1" }, true)
                .put(new String[] { "1.2.3-1", "= 1.2.3-1" }, true).put(new String[] { "1.2.3-1", " v1.2.3-1" }, true)
                .put(new String[] { "1.2.3-1", " =1.2.3-1" }, true).put(new String[] { "1.2.3-1", " v 1.2.3-1" }, true)
                .put(new String[] { "1.2.3-1", " = 1.2.3-1" }, true)
                .put(new String[] { "1.2.3-beta", "v1.2.3-beta" }, true)
                .put(new String[] { "1.2.3-beta", "=1.2.3-beta" }, true)
                .put(new String[] { "1.2.3-beta", "v 1.2.3-beta" }, true)
                .put(new String[] { "=1.2.3-beta", "v 1.2.3-alpha+build" }, false)
                .put(new String[] { "1.2.3-beta", "= 1.2.3-beta" }, true)
                .put(new String[] { "1.2.3-beta", " v1.2.3-beta" }, true)
                .put(new String[] { "1.2.3-beta", " =1.2.3-beta" }, true)
                .put(new String[] { "1.2.3-beta", " v 1.2.3-beta" }, true)
                .put(new String[] { "1.2.3-beta", " = 1.2.3-beta" }, true)
                .put(new String[] { "1.2.3-beta+build", " = 1.2.3-beta+otherbuild" }, true)
                .put(new String[] { "1.2.3+build", " = 1.2.3+otherbuild" }, true)
                .put(new String[] { "1.4.3+build", " = 1.2.3+otherbuild" }, false)
                .put(new String[] { "1.2.3-beta+build", "1.2.3-beta+otherbuild" }, true)
                .put(new String[] { "1.2.3+build", "1.2.3+otherbuild" }, true)
                .put(new String[] { "  v1.2.3+build", "1.2.3+otherbuild" }, true)
                .put(new String[] { "1.3.0", " = 1.3.0" }, true).build();

        for (Entry<String[], Boolean> entry : values.entrySet()) {
            String[] versions = entry.getKey();
            String verA = versions[0], verB = versions[1];
            boolean result = entry.getValue();

            assertThat("eq(" + verA + " , " + verB + ")", SemVer.eq(verA, verB), is(result));
            assertThat("neq(" + verA + " , " + verB + ")", SemVer.neq(verA, verB), is(!result));
            assertThat("cmp(" + verA + " , " + verB + ")", SemVer.compare(verA, verB), result ? is(0) : not(0));

            if (result) {
                assertThat("gte(" + verA + " , " + verB + ")", SemVer.gte(verA, verB), is(result));
                assertThat("lte(" + verA + " , " + verB + ")", SemVer.lte(verA, verB), is(result));
                assertThat("gt(" + verA + " , " + verB + ")", SemVer.gt(verA, verB), not(result));
                assertThat("lt(" + verA + " , " + verB + ")", SemVer.lt(verA, verB), not(result));
            }
        }
    }
}
