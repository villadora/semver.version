package org.semver.version;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import junit.framework.Assert;

import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

@RunWith(JUnit4ClassRunner.class)
public class BuildTest {

	@Test
	public void testConstructor() {
		Build build = new Build();
		assertThat(build.getBuild(), notNullValue());

		build = new Build("build20010103.32.3");

		assertThat(build.getBuild().length, is(3));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWrongArgument() {
		Build build = new Build("build323+34-234.3");
		Assert.fail();
	}

	@Test
	public void testToString() {
		assertThat(new Build().toString(), is(""));
		assertThat(new Build("build", "3.4").toString(), is("build.3.4"));
	}
}
