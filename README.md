# ImagePicker

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
Step 2. Add the dependency

  	dependencies {
	        implementation 'com.github.84695Axay:JetPackDemo:Tag'
	}

The ImagePicker configuration is created using the builder pattern.

Kotlin

	VasuImagePicker.ActivityBuilder(this)
                .setFolderMode(true)
                .setFolderTitle("Gallery")
                .setMultipleMode(false)
                .setImageCount(1)
                .setMaxSize(10)
                .setBackgroundColor("#FFFFFF")
                .setToolbarColor("#FFFFFF")
                .setToolbarTextColor("#000000")
                .setToolbarIconColor("#000000")
                .setStatusBarColor("#FFFFFF")
                .setProgressBarColor("#50b1ed")
                .setAlwaysShowDoneButton(true)
                .setRequestCode(1010)
                .setKeepScreenOn(true)
                .start()
		
