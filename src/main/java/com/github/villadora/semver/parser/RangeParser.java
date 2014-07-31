package com.github.villadora.semver.parser;

import java.util.regex.Matcher;

import com.github.villadora.semver.Version;
import com.github.villadora.semver.ranges.Range;
import com.github.villadora.semver.regexp.RangeRegExps;
import com.github.villadora.semver.regexp.VersionRegExps;

/**
 * 
 * <pre>
 * range ::=  simple_range
 *  		  | simple_range " " range
 *   		  | simple_range "||" range
 *            | "(" range ")" 
 * 
 * simple_range ::= hyphen | xrange | comparator
 * 					| simple | tilde
 * 
 * hyphen ::= <im_vers> "\\s*-\\s*" <im_vers>
 * xrange ::= <xver> "\\." <xver> "\\." <xver>
 * 	          | <xver> "\\." <xver>
 * 			  | <xver>
 * comparator ::= <gltr> <im_vers>
 * simple ::= <im_vers>
 * tilde ::= <tld> <im_vers>
 * caret :: <cart> <im_vers>
 * im_vers ::= <exact_version> | <short_version>
 * exact_version ::= <full_version>
 * short_version ::= <num>
 *                   | <num> "\\."<num>
 * 
 * tld ::= "\\s*~\\s*"
 * caret ::= "\\s*\\s*"
 * gltr ::= "\\s*(>|<)=?\\s*"
 * xver ::= x|X|*|<num>
 * alpha ::= [a-zA-Z0-9]*[a-zA-Z]+[a-zA-Z0-9]*                  
 * num ::= 0|[0-9]\\d*
 * </pre>
 * 
 * 
 * @author villa
 * 
 */
/**
 * @author villa
 * 
 */
public class RangeParser {

    private String input;
    private int pos;

    public static Range parse(String range) {
        return new RangeParser(range).parse();
    }

    public RangeParser(String input) {
        this.input = input.trim();
        this.pos = 0;
    }

    public Range parse() {
        if (input.length() == 0)
            return Range.any();
        return range();
    }

    private Range range() {
        return range(false);
    }

    private Range range(boolean paren) {
        skipWhitespace();
        Range range = null;

        char ch = input.charAt(pos);
        // LL top-down parser
        switch (ch) {
        case '(':
            pos += 1;
            range = range(true);
            if (input.charAt(pos) != ')')
                throw new IllegalArgumentException("Cannot find matching close paren: " + input);
            pos += 1;
            break;
        case '~':
            // tilde
            range = tilde();
            break;
        case '>':
        case '<':
            // compare
            range = compare();
            break;
        case '^':
            // caret, which lock down the first no-zero number
            range = caret();
            break;
        default:
            // The following
            Matcher mc = RangeRegExps.HYPHEN_RANGE_REG.matcher(input.substring(pos));
            if (mc.find()) {
                // hyphen
                String hyphen = mc.group(), start = mc.group(1), end = mc.group(2);
                pos += hyphen.length(); // update pos
                // TODO: how to expand the short version, the specification is
                // still not decided.
                range = Range.between(precise(start), precise(end), true, true);
                break;
            }

            mc = RangeRegExps.SIMPLE_RANGE_REG.matcher(input.substring(pos));
            if (mc.find()) {
                // simple range
                String srange = mc.group();
                pos += srange.length(); // comsume, update pos
                range = Range.exact(precise(srange));
                break;
            }

            mc = RangeRegExps.X_RANGE_REG.matcher(input.substring(pos));
            if (mc.find()) {
                String xrange = mc.group();
                pos += xrange.length();
                String[] v = xrange.split("\\.");
                if (v.length > 3) {
                    throw new IllegalArgumentException("BaseRange is invalid for: " + input);
                } else if (v.length == 3 || (v.length == 2 && !v[1].matches("x|X|\\*"))) {
                    if (v[1].matches("x|X|\\*")) {
                        int major = Integer.parseInt(v[0]);
                        range = Range.between(precise(Integer.toString(major), true),
                                precise(Integer.toString(major + 1), true), true, false);
                    } else {
                        int minor = Integer.parseInt(v[1]);
                        range = Range.between(precise(v[0] + "." + Integer.toString(minor), true),
                                precise(v[0] + "." + Integer.toString(minor + 1), true), true, false);
                    }
                } else if (v.length == 2 || (v.length == 1 && !v[0].matches("x|X|\\*"))) {
                    int major = Integer.parseInt(v[0]);
                    range = Range.between(precise(Integer.toString(major), true),
                            precise(Integer.toString(major + 1), true), true, false);
                } else if (v.length == 1) {
                    range = Range.any();
                }
                break;
            }
            throw new IllegalArgumentException("Invalid range expression: " + input);
        }

        // recursive parse range to handle and/or situation
        while (pos < input.length()) {
            if (pos + 1 < input.length()) {
                char first = input.charAt(pos), second = input.charAt(pos + 1);
                if (first == ' ' && !Character.isWhitespace(second) && second != '|' && second != ')') {
                    // TODO: currently, there is no expression just start with
                    // '|' and I don't want to handle empty string as range
                    // here: '|| 1.2.3' => '* || 1.2.3'
                    // try and expression
                    pos += 1;
                    range = Range.compositeAnd(range, range());
                    continue;
                }
            }

            skipWhitespace();

            ch = input.charAt(pos);
            if (pos + 1 < input.length()) {
                if (ch == '|' && input.charAt(pos + 1) == '|') {
                    pos += 2;
                    range = Range.compositeOr(range, range());
                    continue;
                }
            }
            if (paren && input.charAt(pos) == ')')
                // pop out stack
                return range;

            if (!Character.isWhitespace(input.charAt(pos))) {
                // end with unrecognized char
                throw new IllegalArgumentException("Invalid range expression: " + input);
            }
        }

        return range;
    }

    private Range compare() {
        Matcher mc = RangeRegExps.COMPARE_RANGE_REG.matcher(input.substring(pos));
        if (mc.find()) {
            String comp = mc.group(1), version = mc.group(2);
            pos += mc.group().length();
            // TODO: how to expand '>= 1', there is no spec yet.
            // 1) >= 1.0.0
            // 2) >= 1.0.0-0
            // Now implement the first one

            if (RangeRegExps.SHORT_VERSION_REG.matcher(version).matches()) {
                if (comp.equals("<=") || comp.equals(">=") || comp.equals("<")) {
                    version = precise(version, true);
                } else {
                    String[] v = version.split("\\.");
                    if (v.length == 2) {
                        version = precise(v[0] + "." + Integer.toString(Integer.parseInt(v[1]) + 1), true);
                    } else if (v.length == 1) {
                        version = precise(Integer.toString(Integer.parseInt(v[0]) + 1), true);
                    }
                    comp = ">=";
                }
            }

            if (comp.equals(">")) {
                return Range.greaterThan(version);
            } else if (comp.equals("<")) {
                if (!version.contains("-")) {
                    // has no prerelease
                    version = version + "-0";
                }

                return Range.lessThan(version);
            } else if (comp.equals(">="))
                return Range.greaterAndEqualThan(version);
            else if (comp.equals("<="))
                return Range.lessAndEqualThan(version);
        }

        throw new IllegalArgumentException("Invalid compare range: " + input.substring(pos));
    }

    private Range tilde() {
        Matcher mc = RangeRegExps.TILDE_RANGE_REG.matcher(input.substring(pos));
        if (mc.find()) {
            String impv = mc.group(1);
            String[] v = impv.split("\\.");

            pos += mc.group().length(); // comsume, update pos

            Version version = new Version(precise(impv, true));
            if (v.length == 1) {
                // ~1: Any version starting with 1
                Version upper = new Version(version);
                upper.setMajor(upper.getMajor() + 1);
                upper.setPatch(0);
                return Range.between(version, upper, true, false);
            } else if (v.length <= 3) {
                // ~1.2: Any version starting with 1.2
                // ~1.0.0: Reasonably close to 1.0.0
                Version upper = new Version(version);
                upper.setMinor(upper.getMinor() + 1);
                upper.setPatch(0);
                return Range.between(version, upper, true, false);
            }
        }

        throw new IllegalArgumentException("Invalid tilde range: " + input.substring(pos));
    }

    private Range caret() {
        Matcher mc = RangeRegExps.CARET_RANGE_REG.matcher(input.substring(pos));
        if (mc.find()) {
            String impv = mc.group(1);
            pos += mc.group().length(); // comsume
            Version version = new Version(precise(impv, true));

            // lock down major
            if (version.getMajor() != 0) {
                Version upper = new Version(version.getMajor() + 1, 0, 0, new String[] { "0" });
                return Range.between(version, upper, true, false);
            }

            // if leading major is 0, than just lock the version
            return Range.exact(new Version(precise(impv)));
        }

        throw new IllegalArgumentException("Invalid caret range: " + input.substring(pos));
    }

    private void skipWhitespace() {
        if (pos < input.length() && Character.isWhitespace(input.charAt(pos)))
            ++pos;
    }

    private String precise(String version) {
        return precise(version, false);
    }

    private String precise(String version, boolean prerelease) {
        if (RangeRegExps.EXACT_VERSION_REG.matcher(version).matches()) {
            if (prerelease && VersionRegExps.MAIN_VERSION_REG.matcher(version).matches())
                return version + "-0";
            else
                return version;
        }

        String[] v = version.split("\\.");
        if (v.length == 3) {
            return prerelease ? (version + "-0") : version;
        } else if (v.length == 2) {
            return prerelease ? (version + ".0-0") : (version + ".0");
        } else if (v.length == 1) {
            return prerelease ? (version + ".0.0-0") : (version + ".0.0");
        }

        throw new IllegalArgumentException("Invalid version for expand: " + version);
    }
}
