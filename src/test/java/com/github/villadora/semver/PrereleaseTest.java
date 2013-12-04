package com.github.villadora.semver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import junit.framework.Assert;

import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

@RunWith(JUnit4ClassRunner.class)
public class PrereleaseTest {

	@Test
	public void testConstructor() {
		PreRelease pre = new PreRelease(new String[] {});

		assertThat(pre.getPrerelease(), notNullValue());

		pre = new PreRelease("alpha", "beta", "12", "2");

		assertThat(pre.getPrerelease().length, is(4));

		assertThat(pre.equals(new PreRelease("alpha.beta", "12.2")), is(true));
		assertThat(pre.hashCode(), equalTo(new PreRelease("alpha.beta.12.2").hashCode()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgument() {
		new PreRelease("alpha+beta");
		Assert.fail();
	}

	@Test
	public void testCompare() {
		assertThat(new PreRelease("alpha"), lessThan(new PreRelease("alpha.1")));
		assertThat(new PreRelease("alpha.5"), lessThan(new PreRelease("alpha.10")));
		assertThat(new PreRelease("10"), lessThan(new PreRelease("alpha")));

		assertThat(new PreRelease("alpha"), lessThan(new PreRelease("beta")));
		assertThat(new PreRelease(), greaterThan(new PreRelease("beta")));
		assertThat(new PreRelease("alpha", "beta"), greaterThan(new PreRelease("alpha")));
	}

	@Test
	public void testToString() {
		assertThat(new PreRelease().toString(), is(""));
		assertThat(new PreRelease("alpha", "1.beta", "10").toString(), is("alpha.1.beta.10"));
	}
}
