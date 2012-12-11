# Meine Mediathek
Diese Anwendung wurde dazu entwickelt, Deutschen Android-Nutzern es zu ermöglichen Folgen aus der *ZDF Mediathek* herunterzulassen um diese auch ohne Internet zu gucken.

## Download a pre-build version
You can download a pre-build version of **Meine Mediathek** from the [Google Play][1] store.

## Build the application with [Eclipse][2]
For building the application by yourself follow the following steps:

  1. Clone the application repository

          git clone https://github.com/thuetz/MeineMediathek.git
       
  2. Change into the cloned directory

          cd MeineMediathek

  3. Change into the app directory and run the native build process (you need the [Android NDK][3]):

          cd app
          $NDK_ROOT_DIR/ndk-build

  4. Create an Android project for the actual application (using the `Android Project from Existing Code` function of Eclipse)
  5. Build the application :)

## Changelog
### Version 0.2 (Code: *20*, Veröffentlicht am: ***2013-XX-XX***) - [Changes][120]
* Unterstützung für das RTMP Protokoll hinzugefügt (für eine spätere Unterstützung der ARD und WDR Mediathek)
* Benutzeroberfläche überarbeitet

### Version 0.1 (Code: *10*, Released on: ***2012-12-08***) - [Changes][110]
* Downloads are now cancelable
* Prepared the code to handle custom user settings
* The device will now stay awake until all downloads finished
* Removed the English translation, the application is now German by default
* Fixed several bug which caused the application to crash

### Version 0.0.7 (Code: *7*, Released on: ***2012-11-30***) - [Changes][105]
* Fixed a bug which caused an infinite loop while parsing the search results
* The application will now show a message if no results were found
* The downloaded movies will now be stored in the public movies directory of the current user (the preview pictures will remain in the private directory of the application)

### Version 0.0.6 (Code: *6*, Released on: ***2012-11-28***) - [Changes][104]
* Fixed a bug which caused the application to crash immediately after starting it (this time always :()

### Version 0.0.5 (Code: *5*, Released on: ***2012-11-27***) - [Changes][103]
* Fixed an annoying bug which happened sometime while just starting the app

### Version 0.0.4 (Code: *4*, Released on: ***2012-11-25***) - [Changes][102]
* The download thread has now a limited runtime of 120 Minutes
* Added more debugging information to the bug report of some specific exceptions
* Fixed a smaller bug when fetching the ASX descriptor from the ZDF Mediathek

### Version 0.0.3 (Code: *3*, Released on: ***2012-11-24***) - [Changes][101]
* Added the missing German translation of the application
* Changed the way the titles of the movies in the ZDF Mediathek are extracted
* The notification indicator will now stop moving as soon as the download was finished
* Fixed a bug which caused a crash while starting the app

### Version 0.0.2 (Code: *2*, Released on: ***2012-11-23***) - [Changes][100]
* Made all texts translatable
* Added a license agreement for the first start of the application
* The download buffer size will vary according to the estimated file size of the movie which should be downloaded
* Fixed a lot of bugs causing the application to crash
* Made the application available in the Google Play Store

### Version 0.0.1 (Code: *1*, Released on: ***2012-11-20***)
* First version which is able to download a stream from [ZDF Mediathek][4]

 [1]: https://play.google.com/store/apps/details?id=com.halcyonwaves.apps.meinemediathek
 [2]: http://www.eclipse.org/
 [3]: http://developer.android.com/tools/sdk/ndk/index.html
 [4]: http://www.zdf.de/ZDFmediathek/hauptnavigation/startseite?flash=off
 [100]: https://github.com/thuetz/MeineMediathek/compare/v0.0.1...v0.0.2 
 [101]: https://github.com/thuetz/MeineMediathek/compare/v0.0.2...v0.0.3 
 [102]: https://github.com/thuetz/MeineMediathek/compare/v0.0.3...v0.0.4 
 [103]: https://github.com/thuetz/MeineMediathek/compare/v0.0.4...v0.0.5 
 [104]: https://github.com/thuetz/MeineMediathek/compare/v0.0.5...v0.0.6
 [105]: https://github.com/thuetz/MeineMediathek/compare/v0.0.6...v0.0.7
 [110]: https://github.com/thuetz/MeineMediathek/compare/v0.0.7...v0.1
 [120]: https://github.com/thuetz/MeineMediathek/compare/v0.1...v0.2
