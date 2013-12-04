package com.github.villadora.semver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

@RunWith(JUnit4ClassRunner.class)
public class VersionTest {

	@Test
	public void testConstructor() {
		Version version = new Version();
		assertThat(version, notNullValue());
		assertThat(version.getMajor(), is(0));

		assertThat(new Version(0, 1, 12), equalTo(new Version("0.1.12")));

		version = new Version("v1.0.0-alpha.beta+build.2013");

		assertThat(version, equalTo(new Version(1, 0, 0, new String[] { "alpha", "beta" },
				new String[] { "build.2013" })));

		assertThat(version.getMajor(), is(1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWrongArgument() {
		new Version("1.a0.3-alpha");
		Assert.fail();
	}

	@Test
	public void testCompare() {
		assertThat(new Version("v0.0.1"), equalTo(new Version("0.0.1")));
		// Notice: that two versions could have 0 in compareTo result but not
		// equal.
		assertThat(new Version("0.1.1+build"), not(equalTo(new Version("0.1.1+otherbuild"))));
		assertThat(new Version("0.1.1+build"), not(lessThan(new Version("0.1.1+otherbuild"))));
		assertThat(new Version("0.1.1+build"), not(greaterThan(new Version("0.1.1+otherbuild"))));

		assertThat(new Version("= 0.0.3"), lessThan(new Version("0.3.0")));
		assertThat(new Version("0.3.0-alpha"), lessThan(new Version("0.3.0")));
		assertThat(new Version("v 10.2.0"), lessThan(new Version("11.0.3")));

		assertThat(new Version(2, 3, 0), greaterThan(new Version("2.2.19")));
		assertThat(new Version(3, 3, 3, null, new String[] { "build.20131111" }), greaterThan(new Version(
				"3.3.3-alpha+build.20131111")));
	}

	@Test
	public void testToString() {
		assertThat(new Version(1, 2, 12).toString(), is("1.2.12"));
		assertThat(new Version(1, 2, 12, new String[] { "alpha", "b1.12" }).toString(), is("1.2.12-alpha.b1.12"));
		assertThat(
				new Version(1, 2, 12, new String[] { "alpha", "b1.12" }, new String[] { "build.2013", "11" })
						.toString(),
				is("1.2.12-alpha.b1.12+build.2013.11"));
	}
}
