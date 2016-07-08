This app is to use voice command for three operations, which are make a phonecall, send an email, and take a picture.
The UI is extremely easy, which is with one button with mic image and a textview with "Tap on mic to speak".

The voice command function can be separated into two steps, recognize speech and do operation. 
The first step is implemented using RecongizerIntent. 
In onActivityResult() function, transform speech to text in the intent. 
Then depends on different keywords, different functions will be called, which is the second step. 
In this demo, there are only three functions to be called, which are call(), email(), and camera().

Don't forget to add two permissions.
<uses-permission android:name="android.permission.CALL_PHONE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />