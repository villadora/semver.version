package com.github.villadora.semver;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.github.villadora.semver.regexp.VersionRegExps;

class Build {
	private String[] build;

	public static boolean valid(String build) {
		if (build == null)
			return false;

		return VersionRegExps.BUILD_REG.matcher(build).matches();
	}

	public Build() {
		this.setBuild(null);
	}

	public Build(String... build) {
		this.setBuild(build);
	}

	public String[] getBuild() {
		return build;
	}

	public void setBuild(String[] build) {
		if (build == null)
			this.build = new String[] {};
		else {
			List<String> bds = new LinkedList<String>();
			for (String bd : build) {
				if (bd == null || bd.matches("\\s*"))
					continue;

				if (!bd.matches(VersionRegExps.BUILD_REG.pattern()))
					throw new IllegalArgumentException("build format is invalid: " + bd);

				bds.addAll(Arrays.asList(bd.split("\\.")));
			}
			this.build = bds.toArray(new String[0]);
		}
	}

	@Override
	public String toString() {
		if (this.build.length > 0) {
			StringBuilder bb = new StringBuilder(this.build[0]);
			for (int i = 1; i < build.length; ++i) {
				bb.append(".");
				bb.append(this.build[i]);
			}

			return bb.toString();
		}
		return "";
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(this.build);
	}

	@Override
	public boolean equals(Object that) {
		if (this == that)
			return true;

		if (that == null)
			return false;

		if (that instanceof Build) {
			return Arrays.equals(this.build, ((Build) that).build);
		}

		return false;
	}

}
