# aerogear-otp-java

[![circle-ci](https://img.shields.io/circleci/project/github/aerogear/aerogear-otp-java/master.svg)](https://circleci.com/gh/aerogear/aerogear-otp-java)
[![License](https://img.shields.io/badge/-Apache%202.0-blue.svg)](https://opensource.org/s/Apache-2.0)
[![Maven Central](https://img.shields.io/maven-central/v/org.jboss.aerogear/aerogear-otp-java.svg)](http://search.maven.org/#search%7Cga%7C1%7Caerogear-otp-java)
[![Javadocs](http://www.javadoc.io/badge/org.jboss.aerogear/aerogear-otp-java.svg?color=blue)](http://www.javadoc.io/doc/org.jboss.aerogear/aerogear-otp-java)

## Java One Time Password API

A Java library for generating one time passwords according to [RFC 4226.](http://tools.ietf.org/html/rfc4226).

This API is compatible with Google Authenticator apps available for [Android](https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2&hl=en) and [iPhone](https://itunes.apple.com/us/app/google-authenticator/id388497605?mt=8). You can follow the instructions [here](http://support.google.com/accounts/bin/answer.py?hl=en&answer=1066447) to install Google Authenticator. 

|                 | Project Info  |
| --------------- | ------------- |
| License:        | Apache License, Version 2.0  |
| Build:          | Maven  |
| Documentation:  | https://aerogear.org/docs/  |
| Issue tracker:  | https://issues.jboss.org/browse/AGSEC  |
| Mailing lists:  | [aerogear-users](http://aerogear-users.1116366.n5.nabble.com/) ([subscribe](https://lists.jboss.org/mailman/listinfo/aerogear-users))  |
|                 | [aerogear-dev](http://aerogear-dev.1069024.n5.nabble.com/) ([subscribe](https://lists.jboss.org/mailman/listinfo/aerogear-dev))  |

## Usage

### Android Studio

Add to your application's `build.gradle` file

```groovy
dependencies {
  compile 'org.jboss.aerogear:aerogear-otp-java:1.0.0'
}
```

### Maven

Include the following dependencies in your project's `pom.xml`

```xml
<dependency>
  <groupId>org.jboss.aerogear</groupId>
  <artifactId>aerogear-otp-java</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Demo apps

Take a look in our demo apps

* [Android](https://github.com/aerogear/aerogear-android-cookbook/tree/master/Two-Factor)
* [iOS](https://github.com/aerogear/aerogear-ios-cookbook/tree/master/Two-Factor)

If you wanna test thoses app without need to run you own server you can use the [browser-authenticator](https://daplie.github.io/browser-authenticator/) page

## Documentation

For more details about the current release, please consult [our documentation](https://aerogear.org/docs/).

## Development

If you would like to help develop AeroGear you can join our [developer's mailing list](https://lists.jboss.org/mailman/listinfo/aerogear-dev), join #aerogear on Freenode, or shout at us on Twitter @aerogears.

Also takes some time and skim the [contributor guide](http://aerogear.org/docs/guides/Contributing/)

## Questions?

Join our [user mailing list](https://lists.jboss.org/mailman/listinfo/aerogear-users) for any questions or help! We really hope you enjoy app development with AeroGear!

## Found a bug?

If you found a bug please create a ticket for us on [Jira](https://issues.jboss.org/browse/AGSEC) with some steps to reproduce it.
