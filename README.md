# android

# What is it?
Android restful client for employees users of [https://github.com/dsd-meetme/backend](https://github.com/dsd-meetme/backend)

# Useful links
* [https://guides.codepath.com/android/Consuming-APIs-with-Retrofit#references](https://guides.codepath.com/android/Consuming-APIs-with-Retrofit#references)
* [http://www.jsonschema2pojo.org/](http://www.jsonschema2pojo.org/)
* [https://stormpath.com/blog/jjwt-how-it-works-why/](https://stormpath.com/blog/jjwt-how-it-works-why/)
* [http://randomdotnext.com/retrofit-rxjava/](http://randomdotnext.com/retrofit-rxjava/)
* [http://blog.udinic.com/2013/04/24/write-your-own-android-authenticator/](http://blog.udinic.com/2013/04/24/write-your-own-android-authenticator/) used for authentication
* [http://developer.android.com/training/sync-adapters/creating-authenticator.html](http://developer.android.com/training/sync-adapters/creating-authenticator.html)

# Credits
 * [backend](https://github.com/dsd-meetme/backend)
 * [reactivex](http://reactivex.io/)
 * [retrofit](http://square.github.io/retrofit/)
 * [Authenticator examples](http://blog.udinic.com/2013/04/24/write-your-own-android-authenticator/)

# Notes
* To avoid errors get the response body (or errorBody) just one time
* for security reason after a fixed amount of time the re-login is needed but if the password is the same, the process is automatic
* **UTC time????**
