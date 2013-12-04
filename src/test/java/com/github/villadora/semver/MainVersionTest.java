package com.github.villadora.semver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import junit.framework.Assert;

import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

@RunWith(JUnit4ClassRunner.class)
public class MainVersionTest {

	@Test
	public void testConstructor() {
		MainVersion mVer = new MainVersion();
		assertThat(mVer, notNullValue());
		assertThat(mVer, equalTo(new MainVersion("0.0.0")));

		mVer = new MainVersion(1, 3, 4);
		assertThat(mVer.getMajor(), is(1));

		mVer = new MainVersion("1.0.3");
		assertThat(mVer, equalTo(new MainVersion(1, 0, 3)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWrongArgument() {
		String version = "1.0";
		assertThat(MainVersion.valid(version), is(false));

		new MainVersion(version);
		Assert.fail();
	}

	@Test
	public void testCompare() {
		assertThat(new MainVersion("0.0.1"), greaterThan(new MainVersion()));
		assertThat(new MainVersion("1.10.1"), lessThan(new MainVersion("1.11.1")));
		assertThat(new MainVersion(1, 0, 1), equalTo(new MainVersion("1.0.1")));
	}
}
