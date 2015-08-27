# AcquaWatchFace

Android wear watchface

# Screen

![alt tag](http://i62.tinypic.com/qx9sfk.png)

# Usage

You define the AcquaView on you XML file and you get it programmatically.


```java
@Bind(R.id.acqua_view) AcquaView view;
```

You should not forget to inject view in your Activity by adding this line ```ButterKnife.bind(this);``` .

## XML Usage

```xml
<tn.amineghabi.acquawatchface.AcquaView
        android:id="@+id/acqua_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

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



