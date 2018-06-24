# UR Dashboard

This is an Android application predicted to Udacity code reviewers.
Its main purpose is to display and notify about the assigned submissions, project prices and submission request data.


![Showcase](http://gogreenyellow.com/github/urdashboard/urdashboard_main.png)

## Getting started

Download and install the [APK file](https://github.com/paulinaglab/URDashboard/raw/master/URDashboard.apk).

The application needs a valid JSON web token from Udacity to work. 
You can obtain it from your browser's cookies. 

You'll find the right cookie by opening the developer tools (`Ctrl` + `Shift` + `I` for Chrome) in the
[new review dashboard](https://mentor-dashboard.udacity.com/reviews/overview). 
Then open the **Application** tab and copy the value of `_jwt` in 
`Cookies` -> `https://mentor-dashboard.udacity.com/reviews/overview`. 
 The token needs to be entered into the dialog displayed at app's startup.

![Instructions](http://gogreenyellow.com/github/urdashboard/application_api_key_instruction.png)

For convenience, you may paste it into a QR code generator of your choice or 
[this one](http://www.gogreenyellow.com/tools/qrcode/qrcode-generator.html) and scan it with the app.


## Notifications

There are three types of notifications in the app.
### New assignment 

Displayed when a new review is assigned to you.

### Price change

Displayed when the project review price for one of your projects is different
than it was when you last opened the application.

### Submission requests inconsistency (experimental)

The notification shows up when at least of the submission requests contains
different projects than the others in the array. It happens from time to time and
it may (based on personal experience) cause problems with getting the reviews assigned.
 
I fix the issue by updating my request: I remove one of the projects from submission requests 
(via the dashboard), submit the changes and turn the desired project back on.

## Disclaimer

Your data/token is never sent to any private server - it's stored on your device exclusively.

It is all based on an undocumented API, so you use it at your own risk.
