package org.semver.version;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Map;

import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

import com.google.common.collect.ImmutableMap;

@RunWith(JUnit4ClassRunner.class)
public class SemVerTest {
	@Test
	public void testValid() {
		Map<String, Boolean> values = ImmutableMap.<String, Boolean> builder().put("1.0.0", true).put("1.01.1", false)
				.put(".0.0", false).put("1.1", false).put("1.x1.0", false).put("1.13.0", true).put("1..3", false)
				.put("v1.3.0", true).put("= 1.3.0", true).put("=1.0.0", true).put(" = 1.0.0", true).build();

		for (String version : values.keySet()) {
			assertThat(SemVer.valid(version), is(values.get(version)));
		}
	}

	@Test
	public void testCmp() {
		Map<String, String> values = ImmutableMap.<String, String> builder().put("0.0.0", "0.0.0-foo")
				.put("0.0.1", "0.0.0").put("1.0.0", "0.9.9").put("0.10.0", "0.9.0").put("0.99.0", "0.10.0")
				.put("=2.0.0", "1.2.3").put("v0.0.0", "0.0.0-foo").put("v0.0.1", "0.0.0").put("v1.0.0", "0.9.9")
				.put("v0.10.0", "0.9.0").put("v0.99.0", "0.10.0").put("v2.0.0", "1.2.3").put("= 0.0.0", "v0.0.0-foo")
				.put(" =0.0.1", "v0.0.0").put(" v 1.0.0", "v0.9.9").put(" =0.10.0", "v0.9.0")
				.put("v 0.99.0", "v0.10.0")
				.put("2.0.0", "v1.2.3").put("=1.2.3", "1.2.3-asdf").put("1.2.3", "1.2.3-4")
				.put("v1.2.3", "1.2.3-4-foo").put("1.2.3-5-foo", "1.2.3-5").put("1.2.3-5", "1.2.3-4")
				.put("=1.2.3-5-foo", "1.2.3-5-Foo").put("3.0.0", "2.7.2+asdf").put("1.2.3-a.10", "1.2.3-a.5")
				.put("v1.2.3-a.b", "1.2.3-a.5").put("1.2.3-a.b", "1.2.3-a")
				.put("1.2.3-a.b.c.10.d.5", "1.2.3-a.b.c.5.d.100").build();

		for (String verA : values.keySet()) {
			String verB = values.get(verA);

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

		for (String[] versions : values.keySet()) {
			String verA = versions[0], verB = versions[1];
			boolean result = values.get(versions);

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
