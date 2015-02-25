[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Carbon-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1491)&nbsp;&nbsp;
[![Maven Central](https://img.shields.io/badge/Maven%20Central-0.6.4-brightgreen.svg)](https://oss.sonatype.org/content/groups/public/tk/zielony/carbon/0.6.4/)&nbsp;&nbsp;
[![Dropbox](https://img.shields.io/badge/Dropbox-Sample%20app-brightgreen.svg)](https://www.dropbox.com/s/qp4gu6m5so1o0df/samples.apk?raw=1)

Carbon
================
Material Design implementation for Android 2.1 and newer. This is not the exact copy of the Lollipop's API and features. It's a custom implementation of the most useful things as shown in the design specification. The library also features some additional non-standard extensions, like rounded corners for layouts or a Divider view for easy divider creation.

![Circular progress indicators](https://github.com/ZieIony/Carbon/blob/master/images/progress.png)
![Text appearances](https://github.com/ZieIony/Carbon/blob/master/images/textappearances.png)

### Features
 - realtime, animated shadows
 - the touch ripple
 - an elevation system (changing z order changes view rendering order)
 - rounded corners
 - SVG rendering
 - a floating action button view
 - text appearances, sizes, colors and many more useful definitions
 - roboto and roboto condensed fonts for buttons and text fields
 - predefined animation styles and visibility change animations
 - a saturation/brightness/alpha fade for ImageView
 - Divider, StatusBar and NavigationBar drag&drop view
 - dark and light themes in standard and AppCompat versions
 - a radial transition animation
 - circular progress indicators
 - CardView with real shadows and rounded corners
 - state animators

[![YouTube](http://img.youtube.com/vi/YcTQ8a8sTpU/0.jpg)](http://www.youtube.com/watch?v=YcTQ8a8sTpU)

### Instalation
Add the following line to dependencies:

    compile 'tk.zielony:carbon:0.6.2'
    
And these two lines to android/defaultConfig:

    renderscriptTargetApi 20
    renderscriptSupportModeEnabled true
    
In case of any problems with these check the sample app.

![Sample app](https://github.com/ZieIony/Carbon/blob/master/images/sampleapp.png)
![RecyclerView and CardView](https://github.com/ZieIony/Carbon/blob/master/images/recyclercards.png)

### FAQ
##### Why Android 2.1? Isn't 4.0 enough?
I have an old Galaxy S with Android 2.3, so I did that to support my own phone. It's also fun to push the limits. Android 2.1 should work, the code compiles, but I have never checked that.

##### Is it stable?
Seems like it's pretty stable. I'm testing it heavily on different devices and on real projects. There are minor problems with SVG rendering and shadow generation. Also the themes aren't perfect yet. These are the issues I know about. If you have something else, please let me know.

##### Are you using Lollipop's API on Lollipop devices?
No. Maybe one day.

##### Are you thinking about uploading the library to Maven?
Done!

##### Do you plan to offer your Material elements as separate modules? I would prefer not to include the whole project only to use ripples.
I thought about it, but it's not that easy, because almost everything is tightly integrated with other components. For example the ripple. Due to clipping issues in Android < 5.0 it's not possible to draw borderless backgrounds just by setting the background drawable. That's why I would have to export the ripple with all layouts only to make the borderless mode work.

If you wish, you can copy RippleDrawable.java to your project and integrate it with your code manually.

##### Can you add [put your feature name here]?
If it's possible and reasonable, sure! Just let me know.

##### The shadows aren't working. It says something about the RenderScript
You have to add these lines to your android build config:

    renderscriptTargetApi 20
    renderscriptSupportModeEnabled true
    
Carbon uses RenderScript for generating shadows. Gradle doesn't support renderscript very well, so it has to be done that way.

### Changelog
##### 0.6.4
 - added GridLayout,
 - rearranged the sample app,
 - started working on calculator demo,
 - added progress bar demo,
 - added debug mode,
 - color state list can now be passed as a filter color of SVGView,
 - fixed bugs: uninitialized state animator list, window not attached to windowmanager

##### 0.6.3.1
 - fixed bugs: setting corners for layouts, drawing 0dp views, null hotspot
 
##### 0.6.3
 - added ripple styles, demo and more ripple settings,
 - added state animators,
 - cleaned up a bit,
 - fixed bugs: setting background color, RelativeLayout's drawing

##### 0.6.2
 - added CardView and a sample,
 - added a sample and a style for flat buttons,
 - fixed rounded corners in layouts
 
##### 0.6.1
 - CircularProgress in and out animation,
 - shadows improvements,
 - elevation bugfix,
 
##### 0.6.0
 - namespace changed to just 'carbon' - less to write in xml,
 - uploaded to Maven repository,
 - added a sample for using the new image loading animation with Picasso
 
##### 0.5.2
 - added CircularProgress and a progress sample,
 - tweaked light theme a bit, now it's not only gray,
 - changed radial transition sample to show transition between a fragment and a view,
 - updated Toolbar,
 - SVGView color filter bugfixing

##### 0.5.1
 - added dark and light AppCompat themes,
 - added a text appearance demo,
 - added touch ripples to the main list of demos,
 - changed the demo theme to light,
 - added this readme file
 
##### 0.5.0
 - added dark and light themes,
 - cleaned up a bit,
 - the library is open-sourced as Carbon
