SemVer java
==============


[![Build Status](https://travis-ci.org/villadora/semver.version.png)](https://travis-ci.org/villadora/semver.version)

Semver Version Implementation in Java

## Installation

Add dependency to your pom.xml

```xml
<dependency>
  <groupId>com.github.villadora</groupId>
  <artifactId>semver</artifactId>
  <version>0.0.1</version>
</dependency>
```

## API

### SemVer
`SemVer` class used to handle normal situations, especially with strings.


```java
// Tell whether string is a valid version.
SemVer.valid("2.0.1"); // true
SemVer.valid("v2.0.1"); // true
SemVer.valid("= 2.0.1"); // true

// Create Version
SemVer.version("2.0.1"); // return Version object

// Compare two versions
SemVer.compare("1.2.3", "1.2.3-0"); // > 0

// Tell whether two versions are comparable equal
SemVer.eq("1.0.0", "1.0.0"); // true

// Tell whether two versions are not comparable equal
SemVer.neq("1.2.3", "1.0.0"); // true

// If versionA is greater than versionB
SemVer.gt("1.2.1", "1.2.0"); // true

// If versionA is greater than or equal to versionB
SemVer.gte("1.2.1", "1.2.1"); // true

// If versionA is less than versionB
SemVer.lt("1.2.1", "1.3.0"); // true

// If versionA is less than or equal to versionB
SemVer.lte("1.2.1", "1.2.1"); // true

// Tell whether string is a valid range..
SemVer.validRange("~2.1.0 1.2.0 - 1.3.4"); // true

// Create a Range
SemVer.range("~2.1.0 || 3.0.x"); // return a Range object

// Whether the version is satisfies the range
SemVer.satisfies("2.1.0-alpha", "~2.1.0"); // true
SemVer.satisfies("2.1.0-alpha", SemVer.range("~2.1.0")); // true

```


### Version
----------------------
`Version` can be constructed from a valid string or using `Version` constructors;

#### Create version

``` java
Version v = SemVer.version("1.0.0-alpha+build.2013");
// or 
v = new Version("1.0.0-alpha+build.2013");
// or
v = new Version(1, 0, 0, "alpha", "build.2013");
// or
v = new Version(1, 0, 0, new String[]{ "alpha" }, new String[]{ "build", "2013" });
```

### Properties Getter and Setter

``` java
Version v = new Version("1.0.0-alpha.1+build.2013");

// Getter
int major = v.getMajor(); 
int minor = v.getMinor(); 
int patch = v.getPatch();

String preRelease = v.getPreRelease(); // "alpha.1"
String build      = v.getBuild();     // "build.2013"

// Setter
v.setMajor(2);
v.setMinor(1);
v.setPatch(0);

v.setPrerelease("alpha", "1", "rc"); // v.getPrerelease() will be "alpha.1.rc"
// or
v.setPrerelease("alpha.1.rc");

v.setBuild("build, "2014"); // v.getBuild() will be "build.2014"
// or
v.setBuild("build.2014");

v.toString(); // "2.1.0-alpha.1.rc+build.2014"
```


### Compare Versions
The `Version` class implements the `Comparable` interface, you can use `compareTo` to compare two versions.

```java
Version v1 = new Version("1.2.3-alpha");
Version v2 = new Version("1.2.0-beta");

v1.compareTo(v2); // > 0
v1.compareTo(new Version(1, 2, 3, "alpha", "build")); // == 0

v1.equals(v2); // false
v1.equals(new Version(1, 2, 3, new String[] {"alpha"})); // true
v1.equals(new Version("1.2.3-alpha.build")); // false
```

#### Other APIs

```java
Version v = new Version("1.2.3");
v.incrMajor(); // major will be 2
v.incrMinor(); // minor will be 3
v.incrPatch(); // patch will be 4

Version.valid("1.3.4-alpha"); // true
Version.valid("1.03.4-rc"); // false
```

### Ranges
----------------------
`Range` class represents a range space for `Versions`.
Range expressions are targeting to follow the expression describes in [mojombo/semver/#113](https://github.com/mojombo/semver/issues/113). But as the specification is not settled yet, it will try to catch up with the spec. Currently, the implementation of expressions copies the rules in [isaacs/node-semver].

#### Implemented Range Rules
Following rules are implemented as description:

* _Wildcard_ - `1`, `1.x`, `1.X`, `1.*`
* _Tilde_ - `~1.5`
* _Range_ - `1.0 - 2.0`, `1.2.3 - 1.2.4`
* _Greater_ - `> 1.0`, `> 1.0.0`
* _Greater Or Equal_ - `>= 1.0.0`, `>= 1`
* _Less_ - `< 2.0`, `< 2.0.0`
* _Less Or Equal_ - `<= 2.3.0`, `<= 1.4`
* _OR_ - Use `||` to concatenate two range expressions with _OR_ operation, `~1.3.0 || ~2`
* _AND_  - Use one space ` ` to concatenate two range expressions with _AND_ operation, `~1.3 1.4.5 1.0-2.0``
* _Parenthesized_ - The sequence or _OR_/_AND_ rules can be ordered by parentheses, `~1.3 | (1.0.0-1.4.5 ~1.5) | ~2`


#### Range APIs

```java
Range r = SemVer.range("~1.2.0");
// or 
r = Range.valueOf("~1.2.0");

// whether a version satisfies the range
range.satisfies("1.2.1-alpha"); // true
// or
range.satisfies(new Version("1.2.1-alpha")); // true

// Find out the max version satisfies the range, return null if none is found.
Version v =  r.maxSatisfying(new Version("1.2.0"), new Version("1.2.2")); // return new Version("1.2.2");
// or 
v =  r.maxSatisfying("1.2.0", "1.2.2", "1.2.1"); // return new Version("1.2.2");
// or 
v = r.maxSatisfying(List or Versions);

// Return true if the version is outside the bounds of the range in either the high or low direction.
r.outside(new Version("0.0.1")); // true
// or 
r.outside("1.3.0"); // true

// Return true if any of versions that in the range is greater than version
r.greater(new Version("1.1.2"));
// or
r.greater("1.1.2");


// Return true if any of versions that in the range is less than version
r.less(new Version("1.3.2"));
// or
r.less("1.3.2");

```

## License

(The MIT License)
