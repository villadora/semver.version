package com.github.villadora.semver;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.github.villadora.semver.regexp.VersionRegExps;

public class PreRelease implements Comparable<PreRelease> {
	private String[] prerelease;

	public static boolean valid(String prerelease) {
		if (prerelease == null)
			return false;

		return VersionRegExps.PRERELEASE_REG.matcher(prerelease).matches();
	}

	public PreRelease() {
		this.setPrerelease(null);
	}

	public PreRelease(String... prerelease) {
		this.setPrerelease(prerelease);
	}

	public String[] getPrerelease() {
		return prerelease;
	}

	public void setPrerelease(String[] prerelease) {
		if (prerelease == null) {
			this.prerelease = new String[] {};
		} else {

			List<String> pres = new LinkedList<String>();
			for (String pre : prerelease) {
				if (pre == null || pre.matches("\\s*"))
					continue;

				if (!pre.matches(VersionRegExps.PRERELEASE_REG.pattern()))
					throw new IllegalArgumentException("prerelease format is invalid: " + pre);

				pres.addAll(Arrays.asList(pre.split("\\.")));
			}

			this.prerelease = pres.toArray(new String[0]);
		}
	}

	@Override
	public String toString() {
		if (this.prerelease.length > 0) {
			StringBuilder pb = new StringBuilder(this.prerelease[0]);
			for (int i = 1; i < prerelease.length; ++i) {
				pb.append(".");
				pb.append(this.prerelease[i]);
			}
			return pb.toString();
		}

		return "";
	}

	@Override
	public boolean equals(final Object that) {
		if (this == that) {
			return true;
		}
		return that instanceof PreRelease ? this.compareTo((PreRelease) that) == 0 : false;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(prerelease);
	}

	public int compareTo(PreRelease o) {
		if (this.prerelease == null) {
			if (o.prerelease == null)
				return 0;
			else
				return 1;
		}

		if (o.prerelease == null)
			return -1;

		if (this.prerelease.length > 0 && o.prerelease.length == 0)
			return -1;
		else if (this.prerelease.length == 0 && o.prerelease.length > 0)
			return 1;
		else if (this.prerelease.length == 0 && o.prerelease.length == 0)
			return 0;

		int i = 0, lenA = this.prerelease.length, lenB = o.prerelease.length;

		for (; i < lenA && i < lenB; i++) {
			String preA = this.prerelease[i], preB = o.prerelease[i];

			if (preA == null && preB == null)
				continue;
			else if (preB == null)
				return 1;
			else if (preA == null)
				return -1;

			if (preA.matches("0|[1-9]\\d*")) {
				// preA is numeric
				if (!preB.matches("0|[1-9]\\d*"))
					// preB is not numeric
					return -1;
				else {
					// preB is numeric, too
					return Integer.parseInt(preA) - Integer.parseInt(preB);
				}
			}

			if (preB.matches("0|[1-9]\\d*"))
				return 1;

			// compare string
			int cmp = preA.compareTo(preB);
			if (cmp != 0)
				return cmp;
		}

		if (lenA == lenB)
			return 0;
		else if (lenA < lenB)
			return -1;
		else if (lenA > lenB)
			return 1;

		return 0;
	}
}
