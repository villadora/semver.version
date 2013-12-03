package org.semver.version.parser;

import org.semver.version.ranges.BaseRange;

/**
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
		return null;
	}

	private void skipWhitespace() {
		if (pos < input.length() && Character.isWhitespace(ch()))
			++pos;
	}

	private char ch() {
		return input.charAt(pos);
	}

}

/**
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
 */
