package org.semver.version;

public class PreRelease implements Comparable<PreRelease> {
	private String[] prerelease;

	public PreRelease() {
	}

	public PreRelease(String... prerelease) {
		this.prerelease = prerelease;
	}

	public String[] getPrerelease() {
		return prerelease;
	}

	public void setPrerelease(String[] prerelease) {
		this.prerelease = prerelease;
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
