# Meine Mediathek
TODO

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
### Version 0.0.2 (Code: *2*, Released on: ***2012-11-XX***) - [Changes][100]
* Made all texts translateable
* An ongoing download can now be canceled
* Added a license agreement for the first start of the application
* The download buffer size will vary according to the estimated file size of the movie which should be downloaded
* Removed the use of the ACRA library (since the Play Store is used now)
* Made the application available in the Google Play Store

### Version 0.0.1 (Code: *1*, Released on: ***2012-11-20***)
* First version which is able to download a stream from [ZDF Mediathek][4]

 [1]: https://play.google.com/store/apps/details?id=com.halcyonwaves.apps.meinemediathek
 [2]: http://www.eclipse.org/
 [3]: http://developer.android.com/tools/sdk/ndk/index.html
 [4]: http://www.zdf.de/ZDFmediathek/hauptnavigation/startseite?flash=off
 [100]: https://github.com/thuetz/MeineMediathek/compare/v0.0.1...v0.0.2 