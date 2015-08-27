# AcquaWatchFace

Android wear watchface

# Screen

![alt tag](http://i62.tinypic.com/qx9sfk.png)

# Usage

You can define values on you XML file or you can make it programmatically. We have 5 values to customize
our player view.


# java
mpv = (MaterialMusicPlayerView) findViewById(R.id.mpv);
```

```buttonColor```  play/pause button background.
```progressEmptyColor``` progress bar color(Left Seconds)
```progressLoadedColor``` progress bar color(Passed Seconds)
```textColor``` music minutes(Left and passed time) color
```textSize``` music minutes(Left and passed time) size

## XML Usage

```xml
<androgeek.material.library.MaterialMusicPlayerView
        android:id="@+id/mpv"
        android:layout_width="250dp"
        android:layout_height="250dp"
        app:textSize = "14sp"
        app:textColor = "#80FFFFFF"
        app:buttonColor = "#FF0028"
        app:progressLoadedColor = "#00815E"
        app:progressEmptyColor = "#20FFFFFF"
        app:cover = "@drawable/mycover"/>

# Libraries Used

[Butterknife Dependency Injection](https://github.com/JakeWharton/butterknife)

# Design Owner

Design is created by [Amin Ghabi] (https://www.facebook.com/amin.duferme)

License
--------


    Copyright 2015 Amin Ghabi.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



