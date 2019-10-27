[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Carbon-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1491)&nbsp;&nbsp;
[![Maven Central](https://img.shields.io/badge/Maven%20Central-0.16.0.1-brightgreen.svg)](https://oss.sonatype.org/content/groups/public/tk/zielony/carbon/0.16.0.1/)&nbsp;&nbsp;
[![Dropbox](https://img.shields.io/badge/Dropbox-Sample%20app-brightgreen.svg)](https://www.dropbox.com/s/wllgpan9cl01mh3/samples.apk?raw=1)

[![Twitter](https://img.shields.io/badge/Twitter-GreenMakesApps-blue.svg)](https://twitter.com/GreenMakesApps)

Carbon
================
Material Design implementation for Android 4.0 and newer. This is not the exact copy of the Lollipop's API and features. It's a custom implementation of the most useful things as shown in the design specification. Carbon tries to:

 - make things easier (specify cornerRadius='dp' instead of creating an xml and/or a ViewOutlineProvider)
 - make it all work and look the same on all APIs (like CheckBox's left padding)
 - really backport features (don't use gradients for shadows!)
 - fix Android's everlasting bugs (FrameLayout ignores child's padding when no gravity is set)
 
### What's new

 - custom item layouts for BottomNavigationView, TabLayout and NavigationView
 - DayNight themes
 - updated control colors
 - fixed color animations, background tint, menu inflation, shadow transformations

### Features

##### Android 4.x

 - generated, animated shadows with elevation system
 - the touch ripple
 - rounded corners with content clipping
 - circular reveal
 - theme xml attribute
 - widget and drawable tinting
 - font resources
 - edge effects

##### All APIs

 - simple to use xml attributes for stroke, cut corners, rounded corners and ripples
 - colored shadows
 - brightness/saturation fade
 - SVG support (with transformations, text, gradients, etc.)
 
###### Useful extensions and bugfixes

 - html text
 - percent layouts, anchors, colored insets
 - view stroke
 - visibility animations
 - color state animations
 - ttf/otf fonts
 - text auto size
 - TextMarker

##### Material constants, widgets and components

 - colors, dimensions, typography
 - ready-to-use rows and adapters for RecyclerView
 - DropDown, FloatingActionMenu, RangeSeekBar, ExpandableRecyclerView, FlowLayout, TableView, BackdropLayout, Banner, BottomNavigationView, TabLayout

### Samples
<img src="https://github.com/ZieIony/Carbon/blob/master/images/shadow.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/searchtoolbar.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/flowlayoutchips.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/musicplayer.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/fab.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/code.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/bottomnavigation.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/profile.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/backdrop.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/checkboxes.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/components.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/dropdown.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/fontresource.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/guidelinesbuttons.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/guidelinesmenus.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/listdialog.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/buttons.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/textfields.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/tablelayout.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/multiselect.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/powermenu.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/studiescrane.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/tabs.png" width="135px" height="240px"/> <img src="https://github.com/ZieIony/Carbon/blob/master/images/themes.png" width="135px" height="240px"/> 

### [Installation](https://github.com/ZieIony/Carbon/wiki/Installation)

### [FAQ](https://github.com/ZieIony/Carbon/wiki/FAQ)

### [Changelog](https://github.com/ZieIony/Carbon/wiki/Changelog)

### [JavaDoc](http://zieiony.github.io/Carbon/javadoc/)

### Articles

 - [Clipping and shadows on Android](https://medium.com/@Zielony/clipping-and-shadows-on-android-e702a0d96bd4)
 - [Aligning text using markers](https://medium.com/@Zielony/aligning-text-on-android-7119eb3dba74)

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
