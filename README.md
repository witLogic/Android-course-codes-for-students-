# Android-Course

+ Module 1
	+ adb - Android debug bridge.
		adb devices - list of android devices attached to the PC
		adb pull - pull data from device
		adb push - push data from device
		adb shell - Android command line shell
		adb logcat - https://developer.android.com/studio/command-line/logcat.html
			eg: adb logcat ActivityManager:I MyApp:D *:S
				Follows the format tag:priority; *:S silent other messages as to show only the messages that match the specified filter.
	
	+ Views - A single rectangle in the screen that has specific bounds
	+ ViewGroup/Layout - Parent for all Views, used to arrange and impose rules on child Views for 			desired View placement.
	+ LinearLayout - Vertical or Horizontal
	+ RelativeLayout - Flexible
	+ Constraint layout
		+ Horizontal/Vertical constraints
		+ Bias Vertical/Horizontal
		+ Baseline
		+ Chains
			+ Spread
			+ Spread inside
			+ Weighted
			+ Packed
			More info : https://developer.android.com/training/constraint-layout/index.html

+ Data Persistance Notes
  	+ Look at shared_prefs file
    	$ adb shell
    	$ run-as <app-package-name>
    	$ cat /data/data/<app-package-name>/shared_prefs/<your-prefs-file-name>.xml
  	+ More on SQLITE http://www.sqlitetutorial.net/
  	+ Pull db using ADB https://blog.shvetsov.com/2013/02/access-android-app-data-without-root.html
+ Loaders
 	+ Loaders are used to load the data asynchronously
 	+ Loaders should monitor the source of their data and should deliver new results when available. (Eg. Updating data with FCM notifications from server)
 	+ Loader Manager manages all the created loaders. 
 	+ CursorLoaders
 		+ Extends AsyncTaskLoader, which loads data in a background thread. 
 		+ Loads the data from SQLite database.
 		+ Automatically reflects the new data when data in the db is changed. 
+ ContentProviders
	+ Uses SQLite to store data.
	+ Can transport data to different applications(process) with some restrictions
	+ Internally users IPC mechanisms to transfer data from one app to another. 


