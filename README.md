# aerogear-otp-java - Java One Time Password API

A Java library for generating one time passwords according to [RFC 4226.](http://tools.ietf.org/html/rfc4226).

This is compatible with Google Authenticator apps available for [Android](https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2&hl=en) and [iPhone](https://itunes.apple.com/us/app/google-authenticator/id388497605?mt=8). You can follow the instructions [here](http://support.google.com/accounts/bin/answer.py?hl=en&answer=1066447) to install Google Authenticator. 

## how to create a new project

### basic use case

1. add the maven dependency

        <dependency>
            <groupId>org.jboss.aerogear</groupId>
            <artifactId>aerogear-otp-java</artifactId>
            <version>1.0.0.M1-20121124-SNAPSHOT</version>
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
         
2. Mobile OTP's

        String secret = Base32.random();
		String pin = "1234";
        Motp motp = new Motp(pin, secret);
        motp.now(); //427773
        
        motp.verify("427773"); //true
        Thread.sleep(40);
        motp.verify("427773"); //false

## Working example

1. Go to [http://controller-aerogear.rhcloud.com/aerogear-controller-demo/login](http://controller-aerogear.rhcloud.com/aerogear-controller-demo/login)
2. Login with *username: john* and *password: 123*. Click on *Give it a try*.
3. Now open google authenticator application at your phone
4. Go to *Add account*
5. Then ask to *Scan barcode*
6. After that *john* account might be added 
7. On your web browser go to *Try Google authenticator*
8. Enter *username: john* and *password: 123* and use the current OTP on your mobile
