package com.github.villadora.semver.ranges;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.github.villadora.semver.Version;
import com.github.villadora.semver.parser.RangeParser;

public class Range {

    protected boolean minClose;
    protected boolean maxClose;

    protected Version min;
    protected Version max;

    public final static Version MIN = new Version(-1, -1, -1, new String[] { "0" });

    public final static Version MAX = new Version(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

    protected Range() {
        this(MAX, MIN);
    }

    protected Range(String start, String end) {
        this(new Version(start), new Version(end));
    }

    protected Range(Version start, Version end) {
        this(start, end, false, false);
    }

    protected Range(String start, String end, boolean startClose, boolean endClose) {
        this(new Version(start), new Version(end), startClose, endClose);
    }

    protected Range(Version start, Version end, boolean startClose, boolean endClose) {
        this.min = start;
        this.max = end;
        this.minClose = startClose;
        this.maxClose = endClose;
    }

    /**
     * Return true if the version satisfies the range.
     * 
     * @param version
     * @return
     */
    public boolean satisfies(Version version) {
        if (version == null)
            return false;

        if (min.compareTo(max) > 0) // empty
            return false;

        return (minClose ? min.compareTo(version) <= 0 : min.compareTo(version) < 0)
                && (maxClose ? max.compareTo(version) >= 0 : max.compareTo(version) > 0);

    }

    /**
     * Find the highest version in the list that satisfies the range, if none is found, return null
     * 
     * @param versions
     * @return
     */
    public Version maxSatisfying(List<Version> versions) {
        if (versions == null || versions.size() == 0)
            return null;

        List<Version> list = new LinkedList<Version>(versions);
        Collections.sort(list);
        for (int i = list.size() - 1; i >= 0; --i) {
            Version ver = list.get(i);
            if (satisfies(ver))
                return ver;
        }
        return null;
    }

    /**
     * Return true if the version is outside the bounds of the range in either the high or low
     * direction.
     * 
     * @param version
     * @return
     */
    public boolean outside(Version version) {
        if (version == null)
            return true;

        if (min.compareTo(max) > 0) // empty
            return true;

        return greater(version) || less(version);
    }

    /**
     * Return true if any of versions that in the range is greater than version
     * 
     * @param version
     * @return
     */
    public boolean greater(Version version) {
        if (version == null)
            return true;

        if (min.compareTo(max) > 0) // empty
            return false;

        return minClose ? min.compareTo(version) > 0 : min.compareTo(version) >= 0;
    }

    /**
     * Return true if any of versions that in the range is less than version
     * 
     * @param version
     * @return
     */
    public boolean less(Version version) {
        if (version == null)
            return false;

        if (min.compareTo(max) > 0) // empty
            return false;

        return maxClose ? max.compareTo(version) < 0 : max.compareTo(version) <= 0;
    }

    /**
     * @see #satisfies(Version)
     * @param version
     * @return
     */
    public boolean satisfies(String version) {
        return satisfies(new Version(version));
    }

    /**
     * @see #maxSatisfying(List)
     * @param versions
     * @return
     */
    public Version maxSatisfying(Version... versions) {
        return maxSatisfying(Arrays.asList(versions));
    }

    /**
     * @see #maxSatisfying(List)
     * @param versions
     * @return
     */
    public Version maxSatisfying(String... versions) {
        List<Version> list = new LinkedList<Version>();
        for (String version : versions) {
            list.add(new Version(version));
        }
        return maxSatisfying(list);
    }

    /**
     * @see #outside(Version)
     * @param version
     * @return
     */
    public boolean outside(String version) {
        return outside(new Version(version));
    }

    /**
     * @see #greater(Version)
     * @param version
     * @return
     */
    public boolean greater(String version) {
        return greater(new Version(version));
    }

    /**
     * @see #less(Version);
     * @param version
     * @return
     */
    public boolean less(String version) {
        return less(new Version(version));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (o instanceof Range) {
            Range that = (Range) o;
            return min.equals(that.min) && max.equals(that.max) && minClose == that.minClose
                    && maxClose == that.maxClose;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        for (int code : Arrays.asList(min.hashCode(), max.hashCode(), Boolean.valueOf(minClose).hashCode(), Boolean
                .valueOf(maxClose).hashCode())) {
            result = 31 * result + code;
        }
        return result;
    }

    @Override
    public String toString() {
        if (min == MIN && max == MAX)
            return ">0.0.0 <0.0.0";

        if (min == MIN) {
            return (maxClose ? "<=" : "<") + max.toString();
        } else if (max == MAX) {
            return (minClose ? ">=" : ">") + min.toString();
        }

        return (maxClose ? "<=" : "<") + max.toString() + " " + (minClose ? ">=" : ">") + min.toString();
    }

    public static boolean valid(String range) {
        try {
            new RangeParser(range).parse();
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    /**
     * Range that match none version
     * 
     * @return
     */
    public static Range empty() {
        return EmptyRange.RANGE;
    }

    /**
     * Range that matches any valid version
     * 
     * @return
     */
    public static Range any() {
        return AnyRange.RANGE;
    }

    public static Range valueOf(String range) {
        return new RangeParser(range.trim()).parse();
    }

    public static Range compositeOr(String... rgs) {
        return composite(false, rgs);
    }

    public static Range compositeAnd(String... rgs) {
        return composite(true, rgs);
    }

    public static Range compositeOr(Range... rgs) {
        return composite(false, Arrays.asList(rgs));
    }

    public static Range compositeAnd(Range... rgs) {
        return composite(true, Arrays.asList(rgs));
    }

    private static Range composite(boolean and, List<Range> rgs) {
        return new CompositRange(rgs, and);
    }

    private static Range composite(boolean and, String... rgs) {
        List<Range> ranges = new LinkedList<Range>();
        for (String rg : rgs) {
            ranges.add(new RangeParser(rg).parse());
        }
        return composite(and, ranges);
    }

    public static Range exact(Version version) {
        return new Range(version, version, true, true);
    }

    public static Range exact(String version) {
        return exact(new Version(version));
    }

    public static Range greaterThan(String version) {
        return greaterThan(new Version(version));
    }

    public static Range greaterThan(Version version) {
        return new Range(version, Range.MAX);
    }

    public static Range lessThan(String version) {
        return lessThan(new Version(version));
    }

    public static Range lessThan(Version version) {
        return new Range(Range.MIN, version);
    }

    public static Range greaterAndEqualThan(String version) {
        return greaterAndEqualThan(new Version(version));
    }

    public static Range greaterAndEqualThan(Version version) {
        return new Range(version, Range.MAX, true, false);
    }

    public static Range lessAndEqualThan(String version) {
        return lessAndEqualThan(new Version(version));
    }

    public static Range lessAndEqualThan(Version version) {
        return new Range(Range.MIN, version, false, true);
    }

    public static Range between(String start, String end, boolean sClosed, boolean eClosed) {
        return between(new Version(start), new Version(end), sClosed, eClosed);
    }

    public static Range between(Version start, Version end, boolean sClosed, boolean eClosed) {
        return new Range(start, end, sClosed, eClosed);
    }

}
