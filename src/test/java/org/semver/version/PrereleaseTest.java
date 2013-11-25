package org.semver.version;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

@RunWith(JUnit4ClassRunner.class)
public class PrereleaseTest {

	@Test
	public void testConstructor() {
		PreRelease pre = new PreRelease(new String[] {});

		Assert.assertNotNull(pre.getPrerelease());

		pre = new PreRelease("alpha", "beta", "12");
	}

	@Test
	public void testCompare() {
		assertThat(new PreRelease("alpha").compareTo(new PreRelease("beta")), lessThan(0));
		assertThat(new PreRelease().compareTo(new PreRelease("beta")), greaterThan(0));
		assertThat(new PreRelease("alpha", "beta").compareTo(new PreRelease("alpha")), greaterThan(0));
	}
}
