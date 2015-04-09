# aerogear-otp-java - Java One Time Password API

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

## how to create a new project

### basic use case

1. add the maven dependency

        <dependency>
            <groupId>org.jboss.aerogear</groupId>
            <artifactId>aerogear-otp-java</artifactId>
            <version>1.0.0</version>
            <scope>compile</scope>
        </dependency>
       
## Getting started

### Use 

1. Time based OTP's

        String secret = Base32.random();
        Totp totp = new Totp(secret);
        totp.now(); //427773
        
        totp.verify("427773"); //true
        Thread.sleep(40);
        totp.verify("427773"); //false
         

## Working example

1. Follow directions to install [Shoot 'n Share backend](https://github.com/aerogear/aerogear-backend-cookbook/blob/master/Shoot/README.md)
1. Open the [console shoot-realm credentials](http://localhost:8080/auth/admin/master/console/#/realms/shoot-realm/required-credentials) and add TOTP in `Required User Credentials`
1. Open Shoot 'n Share backend http://localhost:8080/shoot/photos
1. Login with username: *user* and password: *password*.
1. Now open [android otp client application](https://github.com/aerogear/aerogear-otp-android-demo) on your phone
1. Then scan *Scan barcode*
1. Enter the current OTP on your mobile

For more details, please refer to our [documentation](http://aerogear.org/docs/specs/aerogear-security-otp/)

## Are you feeling brave? Try from the snapshot releases!

     <dependency>
         <groupId>org.jboss.aerogear</groupId>
         <artifactId>aerogear-security</artifactId>
         <version>1.0.1-SNAPSHOT</version>
         <scope>compile</scope>
     </dependency>

## Documentation

For more details about the current release, please consult [our documentation](https://aerogear.org/docs/).

## Development

If you would like to help develop AeroGear you can join our [developer's mailing list](https://lists.jboss.org/mailman/listinfo/aerogear-dev), join #aerogear on Freenode, or shout at us on Twitter @aerogears.

Also takes some time and skim the [contributor guide](http://aerogear.org/docs/guides/Contributing/)

## Questions?

Join our [user mailing list](https://lists.jboss.org/mailman/listinfo/aerogear-users) for any questions or help! We really hope you enjoy app development with AeroGear!

## Found a bug?

If you found a bug please create a ticket for us on [Jira](https://issues.jboss.org/browse/AGSEC) with some steps to reproduce it.
