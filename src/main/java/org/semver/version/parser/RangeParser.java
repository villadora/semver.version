package org.semver.version.parser;

import java.util.regex.Matcher;

import org.semver.version.ranges.BaseRange;
import org.semver.version.ranges.Ranges;
import org.semver.version.regexp.RangeRegExps;

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
 * 
 * im_vers ::= <exact_version> | <short_version>
 * exact_version ::= <full_version>
 * short_version ::= <num>
 *                   | <num> "\\."<num>
 * 
 * tld ::= "\\s*~\\s*"
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
public class RangeParser {

	private String input;
	private int pos;

	public RangeParser(String input) {
		this.input = input.trim();
		this.pos = 0;
	}

	public BaseRange parse() {
		skipWhitespace();

		BaseRange range = range();

		while (pos < input.length()) {
			skipWhitespace();

		}

		return range;
	}

	private BaseRange range() {
		skipWhitespace();
		BaseRange range = null;

		char ch = input.charAt(pos);
		switch (ch) {
		case '~':
			// tilde
			break;
		case '>':
		case '<':
			// compare
			break;
		default:
			Matcher mc = RangeRegExps.HYPHEN_RANGE_REG.matcher(input.substring(pos));
			if (mc.find()) {
				// hyphen
				String hyphen = mc.group(0), start = mc.group(1), end = mc.group(2);
				pos += hyphen.length(); // update pos
				// TODO: how to expand the short version, the specification is
				// still not decided.
				range = Ranges.between(precious(start), precious(end), true, true);
				break;
			}

			mc = RangeRegExps.SIMPLE_RANGE_REG.matcher(input.substring(pos));
			if (mc.find()) {
				// simple range
				String srange = mc.group(0);
				pos += srange.length();
				range = Ranges.exact(precious(srange));
				break;
			}

			mc = RangeRegExps.X_RANGE_REG.matcher(input.substring(pos));
			if (mc.find()) {

			}

		}

		return null;
	}

	private void skipWhitespace() {
		if (pos < input.length() && Character.isWhitespace(input.charAt(pos)))
			++pos;
	}

	private String precious(String version) {
		return precious(version, false);
	}

	private String precious(String version, boolean prerelease) {
		if (RangeRegExps.EXACT_VERSION_REG.matcher(version).matches())
			return version;

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
