[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Carbon-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1491)&nbsp;&nbsp;
[![Maven Central](https://img.shields.io/badge/Maven%20Central-0.15.0-brightgreen.svg)](https://oss.sonatype.org/content/groups/public/tk/zielony/carbon/0.15.0/)&nbsp;&nbsp;
[![Dropbox](https://img.shields.io/badge/Dropbox-Sample%20app-brightgreen.svg)](https://www.dropbox.com/s/wllgpan9cl01mh3/samples.apk?raw=1)

[![Google+](https://img.shields.io/badge/Google+-Zielony-red.svg)](https://plus.google.com/u/2/109054799904873578131)&nbsp;&nbsp;
[![Google+](https://img.shields.io/badge/Google+-Carbon-red.svg)](https://plus.google.com/u/1/communities/111973718340428039040)&nbsp;&nbsp;
[![Twitter](https://img.shields.io/badge/Twitter-GreenMakesApps-blue.svg)](https://twitter.com/GreenMakesApps)

Carbon
================
Material Design implementation for Android 4.0 and newer. This is not the exact copy of the Lollipop's API and features. It's a custom implementation of the most useful things as shown in the design specification. Carbon tries to:

 - make things easier (specify cornerRadius='dp' instead of creating an xml and/or a ViewOutlineProvider)
 - make it all work and look the same on all APIs (like CheckBox's left padding)
 - really backport features (don't use gradients for shadows!)
 - fix Android's everlasting bugs (FrameLayout ignores child's padding when no gravity is set)

### Features

##### Android 4.x

 - generated, animated shadows with elevation system
 - the touch ripple
 - rounded corners with content clipping
 - circular reveal
 - theme xml attribute
 - widget and drawable tinting

##### All APIs

 - simple to use xml attributes for corners and ripples
 - colored shadows
 - brightness/saturation fade
 - SVG support (with transformations, text, gradients, etc.)
 
###### Useful extensions and bugfixes

 - html text
 - percent layouts, anchors, colored insets
 - view stroke
 - visibility animations
 - ttf/otf fonts
 - text auto size
 - TextMarker

##### Material constants, widgets and components

 - colors, dimensions, typography
 - ready-to-use rows and adapters for RecyclerView
 - DropDown, FloatingActionMenu, RangeSeekBar, ExpandableRecyclerView, FlowLayout, TableView

### Samples
![Sample app](https://github.com/ZieIony/Carbon/blob/master/images/sampleapp.png)
![Buttons / Usage sample](https://github.com/ZieIony/Carbon/blob/master/images/buttonsusage.png)
![CheckBoxes](https://github.com/ZieIony/Carbon/blob/master/images/checkboxes.png)
![Colored shadows](https://github.com/ZieIony/Carbon/blob/master/images/coloredshadows.png)
![Component](https://github.com/ZieIony/Carbon/blob/master/images/component.png)
![HTML](https://github.com/ZieIony/Carbon/blob/master/images/html.png)
![PagerTabStrip](https://github.com/ZieIony/Carbon/blob/master/images/pagertabstrip.png)
![Registration form](https://github.com/ZieIony/Carbon/blob/master/images/registrationform.png)
![Rounded corners](https://github.com/ZieIony/Carbon/blob/master/images/roundedcorners.png)
![SeekBar and RangeSeekBar](https://github.com/ZieIony/Carbon/blob/master/images/seekbar.png)
![Spinner](https://github.com/ZieIony/Carbon/blob/master/images/spinner.png)
![Table layout](https://github.com/ZieIony/Carbon/blob/master/images/tablelayout.png)
![Theme](https://github.com/ZieIony/Carbon/blob/master/images/theme.png)

### [Installation](https://github.com/ZieIony/Carbon/wiki/Installation)

### [FAQ](https://github.com/ZieIony/Carbon/wiki/FAQ)

### [Changelog](https://github.com/ZieIony/Carbon/wiki/Changelog)

### [JavaDoc](http://zieiony.github.io/Carbon/javadoc/)

### License
```
Copyright 2015 Marcin Korniluk 'Zielony'

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
