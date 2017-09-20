# Android-Course

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


