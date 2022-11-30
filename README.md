If you like what I do and want to support me, you can

<a href="https://www.buymeacoffee.com/cosminradu" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-yellow.png" alt="Buy Me A Coffee" style="height: 60px !important;width: 217px !important;" ></a>

# Knob

![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Fcom%2Fcodevblocks%2Fandroid%2Fknob%2Fmaven-metadata.xml)
![Kotlin Language](https://img.shields.io/badge/language-kotlin-blueviolet.svg)
![Kotlin Language](https://img.shields.io/badge/platform-android-green.svg)

A customizable Android knob View.

The shape of the knob is a circle and consists of fill, track, progress, thumb, steps and substeps. The diameter of the circle is defined using [`knobSize`](https://github.com/CoDevBlocks/Knob#knobSize), while the angle of rotation is specified using [`progressStartAngle`](https://github.com/CoDevBlocks/Knob#progressStartAngle) (in degrees, with the angle 0 corresponding to 3 o'clock) and [`progressSweepAngle`](https://github.com/CoDevBlocks/Knob#progressSweepAngle) (in degrees, representing the rotation arc starting from the start angle).

The arc on the circle defined by the size, start angle and sweep angle is the reference, conceptual, imaginary track (see [`debug_drawTrack`](https://github.com/CoDevBlocks/Knob#debug_drawTrack)), in reference to which all other visual elements are positioned. This means that the visual track, progress, thumb, steps and substeps are positioned (centered) on this reference track according to their anchor points (see [`thumbAnchorX`](https://github.com/CoDevBlocks/Knob#thumbAnchorX), [`thumbAnchorY`](https://github.com/CoDevBlocks/Knob#thumbAnchorY), [`stepAnchorX`](https://github.com/CoDevBlocks/Knob#stepAnchorX), [`stepAnchorY`](https://github.com/CoDevBlocks/Knob#stepAnchorY), [`substepAnchorX`](https://github.com/CoDevBlocks/Knob#substepAnchorX), [`substepAnchorY`](https://github.com/CoDevBlocks/Knob#substepAnchorY)), but can be offset (see [`trackStrokeOffset`](https://github.com/CoDevBlocks/Knob#trackStrokeOffset), [`progressStrokeOffset`](https://github.com/CoDevBlocks/Knob#progressStrokeOffset), [`thumbOffset`](https://github.com/CoDevBlocks/Knob#thumbOffset), [`stepOffset`](https://github.com/CoDevBlocks/Knob#stepOffset), [`substepOffset`](https://github.com/CoDevBlocks/Knob#substepOffset), [`touchOffset`](https://github.com/CoDevBlocks/Knob#touchOffset)) to the outside of the circle (using positive values), or to the inside of the circle (using negative values).

By default, the whole view reacts to touches, but that can be restricted using [`touchThreshold`](https://github.com/CoDevBlocks/Knob#touchThreshold) and [`touchOffset`](https://github.com/CoDevBlocks/Knob#touchOffset) (see [`debug_drawTouchArea`](https://github.com/CoDevBlocks/Knob#debug_drawTouchArea))

![Knob Sample 1](/media/knob_01.gif)
![Knob Sample 2](/media/knob_02.gif)
![Knob Sample 3](/media/knob_03.gif)
![Knob Sample 4](/media/knob_04.gif)

## Table of contents

[Sample in-app usage](https://github.com/CoDevBlocks/Knob#sample-in-app-usage)

[Installation](https://github.com/CoDevBlocks/Knob#installation)

[Usage](https://github.com/CoDevBlocks/Knob#usage)

[XML Attributes](https://github.com/CoDevBlocks/Knob#xml-attributes)

[Debug](https://github.com/CoDevBlocks/Knob#debug)

[Samples](https://github.com/CoDevBlocks/Knob#samples)

## Sample in-app usage

![Screen Recording](/media/knob_screenrec.gif)

## Installation

Simply add the dependency to your module `build.gradle` file

```groovy
dependencies {
    // ...
    implementation 'com.codevblocks.android:knob:<version>'
    // ...
}
```
Current version available on [Maven](https://search.maven.org/artifact/com.codevblocks.android/knob)

![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Fcom%2Fcodevblocks%2Fandroid%2Fknob%2Fmaven-metadata.xml)

## Usage

Include the `com.codevblocks.android.knob.Knob` view in your XML layout file. Here is a complete example:

```xml
<com.codevblocks.android.knob.Knob
    android:id="@+id/knob"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:layout_constraintTop_toTopOf="parent"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintEnd_toEndOf="parent"

    app:knobSize="250dp"

    app:fillColor="@android:color/transparent"
    app:fillStartAngle="0"
    app:fillSweepAngle="360"

    app:trackStrokeColor="#18FFFFFF"
    app:trackStrokeWidth="28dp"
    app:trackStrokeCap="butt"
    app:trackStrokeOffset="-14dp"

    app:progressMode="continuous"
    app:maxProgress="100"
    app:progress="0"

    app:progressStrokeColor="#FFEBBA64"
    app:progressStrokeWidth="20dp"
    app:progressStrokeCap="butt"
    app:progressStrokeOffset="-14dp"
    app:progressStartAngle="135"
    app:progressSweepAngle="270"

    app:thumbDrawable="@drawable/drawable_knob_thumb"
    app:thumbAnchorX="50%"
    app:thumbAnchorY="0%"
    app:thumbOffset="-4dp"
    app:thumbRotation="true"

    app:stepCount="5"
    app:stepDrawable="@drawable/drawable_knob_step"
    app:stepAnchorX="50%"
    app:stepAnchorY="0%"
    app:stepOffset="-4dp"

    app:substepCount="145"
    app:substepDrawable="@drawable/drawable_knob_substep"
    app:substepAnchorX="50%"
    app:substepAnchorY="0%"
    app:substepOffset="-4dp"

    app:touchThreshold="48dp"
    app:touchOffset="-16dp"

    app:debug="false"
    app:debug_drawBounds="false"
    app:debug_drawTrack="false"
    app:debug_drawTouchRadius="false"
    app:debug_drawTouchArea="false"

    android:background="@drawable/drawable_knob_background" />
```
`drawable_knob_background.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:gravity="center">
        <shape android:shape="oval">
            <size android:width="250dp" android:height="250dp" />
            <gradient android:startColor="#434756" android:endColor="#252936" android:type="linear" android:angle="270" />
        </shape>
    </item>
</layer-list>
```

`drawable_knob_thumb.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <size android:width="4dp" android:height="36dp" />
    <solid android:color="#F0F0F0" />
</shape>
```

`drawable_knob_step.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <size android:width="1dp" android:height="32dp" />
    <solid android:color="#A0A0A0" />
</shape>
```

`drawable_knob_substep.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <size android:width="1dp" android:height="20dp" />
    <gradient android:startColor="#A0A0A0" android:endColor="#00FFFFFF" android:angle="270"/>
</shape>
```

## XML Attributes

#### `knobSize`
The diameter of the knob circle
```
app:knobSize="250dp"
```
#### `fillColor`
The fill color of the knob arc or circle
```
app:fillColor="@android:color/transparent"
```
```
app:fillColor="#00000000"
```
#### `fillStartAngle`
Starting angle (in degrees) of the fill arc
```
app:fillStartAngle="0"
```
#### `fillSweepAngle`
Sweep angle (in degrees) of the fill arc, measured clockwise. 360 is equivalent to a complete fill circle
#
```
app:fillSweepAngle="360"
```
#### `trackStrokeColor`
The color of the track which is rendered as a stroked arc
```
app:trackStrokeColor="#18FFFFFF"
```
```
app:trackStrokeColor="@android:color/holo_red_dark"
```
#### `trackStrokeWidth`
The width of the track arc stroke. The stroke will be centered on the imaginary reference track (see [`debug_drawTrack`](https://github.com/CoDevBlocks/Knob#debug_drawTrack))
```
app:trackStrokeWidth="28dp"
```
#### `trackStrokeCap`
Equivalent to [StrokeCap](https://developer.android.com/reference/kotlin/androidx/compose/ui/graphics/StrokeCap) applied to the track stroke
```
app:trackStrokeCap="butt|square|round"
```
#### `trackStrokeOffset`
The offset of the visual track stroke to the reference track (see [`debug_drawTrack`](https://github.com/CoDevBlocks/Knob#debug_drawTrack)). Keep in mind this is affected by the stroke width because the offset takes into account the centerline of the stroke width.
```
app:trackStrokeOffset="-14dp"
```
#### `progressMode`
The behaviour of the progress. `continuous` will allow for a smooth progress along the track. `step` will restrict progress in between steps. `substep` will restrict progress in between substeps
```
app:progressMode="continuous|step|substep"
```
#### `maxProgress`
The maximum progress value
```
app:maxProgress="100"
```
#### `progress`
The current value of the knob progress
```
app:progress="0"
```
#### `progressStrokeColor`
The color of the progress which is rendered as a stroked arc
```
app:progressStrokeColor="#FFEBBA64"
```
#### `progressStrokeWidth`
The width of the progress arc stroke. The stroke will be centered on the imaginary reference track (see [`debug_drawTrack`](https://github.com/CoDevBlocks/Knob#debug_drawTrack))
```
app:progressStrokeWidth="20dp"
```
#### `progressStrokeCap`
Equivalent to [StrokeCap](https://developer.android.com/reference/kotlin/androidx/compose/ui/graphics/StrokeCap) applied to the progress stroke
```
app:progressStrokeCap="butt|square|round"
```
#### `progressStrokeOffset`
The offset of the progress stroke to the reference track (see [`debug_drawTrack`](https://github.com/CoDevBlocks/Knob#debug_drawTrack)). Keep in mind this is affected by the stroke width because the offset takes into account the centerline of the stroke width.
```
app:progressStrokeOffset="-14dp"
```
#### `progressStartAngle`
The start angle (in degrees) of the knob rotation (progress equals to 0)
```
app:progressStartAngle="135"
```
#### `progressSweepAngle`
The knob rotation angle (in degrees) starting from [`progressStartAngle`](https://github.com/CoDevBlocks/Knob#progressStartAngle).
```
app:progressSweepAngle="270"
```
#### `thumbDrawable`
Optional drawable for the thumb indicating the current progress. The position of the drawable relative to the reference track (see [`debug_drawTrack`](https://github.com/CoDevBlocks/Knob#debug_drawTrack)) depends on [`thumbAnchorX`](https://github.com/CoDevBlocks/Knob#thumbAnchorX), [`thumbAnchorY`](https://github.com/CoDevBlocks/Knob#thumbAnchorY) and [`thumbOffset`](https://github.com/CoDevBlocks/Knob#thumbOffset).
```
app:thumbDrawable="@drawable/drawable_knob_thumb"
```
```
app:thumbDrawable="@null"
```
#### `thumbOffset`
The offset of the thumb drawable anchor point relative to the reference track (see [`debug_drawTrack`](https://github.com/CoDevBlocks/Knob#debug_drawTrack)).
```
app:thumbOffset="-4dp"
```
#### `thumbAnchorX`
The horizontal anchor point (X coordinate, left to right) within the thumb drawable relative to the reference track
```
app:thumbAnchorX="50%"
```
#### `thumbAnchorY`
The vertical anchor point (Y coordinate, top to bottom) within the thumb drawable relative to the reference track
```
app:thumbAnchorY="0%"
```
#### `thumbRotation`
Flag which determines if the thumb drawable will be rotated to be perpendicular to the reference track. Useful to obtain more realistic visual efects like shadows on circular thumb drawables. In this case, the drawable should not be rotated since the shadow should not vary with the progress.
```
app:thumbRotation="true|false"
```
#### `stepCount`
The number of main progress steps. Ex: on a clock knob, there would be 12 steps. (see [`progressMode`](https://github.com/CoDevBlocks/Knob#progressMode))
```
app:stepCount="5"
```
#### `stepDrawable`
Optional drawable for a step along the progress arc. The position of the drawable relative to the reference track (see [`debug_drawTrack`](https://github.com/CoDevBlocks/Knob#debug_drawTrack)) depends on [`stepAnchorX`](https://github.com/CoDevBlocks/Knob#stepAnchorX), [`stepAnchorY`](https://github.com/CoDevBlocks/Knob#stepAnchorY) and [`stepOffset`](https://github.com/CoDevBlocks/Knob#stepOffset).
```
app:stepDrawable="@drawable/drawable_knob_step"
```
```
app:stepDrawable="@null"
```
#### `stepOffset`
The offset of the step drawable anchor point relative to the reference track (see [`debug_drawTrack`](https://github.com/CoDevBlocks/Knob#debug_drawTrack)).
```
app:stepOffset="-4dp"
```
#### `stepAnchorX`
The horizontal anchor point (X coordinate, left to right) within the step drawable relative to the reference track
```
app:stepAnchorX="50%"
```
#### `stepAnchorY`
The vertical anchor point (Y coordinate, top to bottom) within the step drawable relative to the reference track
```
app:stepAnchorY="0%"
```
#### `substepCount`
The number of secondary progress steps. Ex: on a clock knob, there would be 60 substeps. (see [`progressMode`](https://github.com/CoDevBlocks/Knob#progressMode)))
```
app:substepCount="145"
```
#### `substepDrawable`
Optional drawable for a substep along the progress arc. The position of the drawable relative to the reference track (see [`debug_drawTrack`](https://github.com/CoDevBlocks/Knob#debug_drawTrack)) depends on [`substepAnchorX`](https://github.com/CoDevBlocks/Knob#substepAnchorX), [`substepAnchorY`](https://github.com/CoDevBlocks/Knob#substepAnchorY) and [`substepOffset`](https://github.com/CoDevBlocks/Knob#substepOffset).
```
app:substepDrawable="@drawable/drawable_knob_substep"
```
```
app:substepDrawable="@null"
```
#### `substepOffset`
The offset of the substep drawable anchor point relative to the reference track (see [`debug_drawTrack`](https://github.com/CoDevBlocks/Knob#debug_drawTrack)).
```
app:substepOffset="-4dp"
```
#### `substepAnchorX`
The horizontal anchor point (X coordinate, left to right) within the substep drawable relative to the reference track
```
app:substepAnchorX="50%"
```
#### `substepAnchorY`
The vertical anchor point (Y coordinate, top to bottom) within the step drawable relative to the reference track
```
app:substepAnchorY="0%"
```
#### `touchThreshold`
If specified with a value greater than 0, defines a touch treshold relative to the knob circle where touches are interpreted. Touches outside of this treshold will be ignored (see [`debug_drawTouchArea`](https://github.com/CoDevBlocks/Knob#debug_drawTouchArea)).
```
app:touchThreshold="0dp"
```
```
app:touchThreshold="48dp"
```
#### `touchOffset`
Offset of the touch threshold centerline relative to the reference knob circle (see [`debug_drawTouchArea`](https://github.com/CoDevBlocks/Knob#debug_drawTouchArea)). A positive value moves the threshold towards the outside of the knob circle, a negative value moves it towards the inner of the knob circle.
```
app:touchOffset="-16dp"
```

## Debug

![Debug](/media/debug.png)

#### `debug`
Enables debugging mode. This must be set to `true` for any of the other debug flags to be taken into account.

#### `debug_drawBounds`
Renders the bounds of the drawable area

#### `debug_drawTrack`
Renders the reference track with a red stroke

#### `debug_drawTouchRadius`
Renders a red radius from the center of the knob circle to the touch point

#### `debug_drawTouchArea`
Renders the touch area with a semitransparent green tint if [`touchThreshold`](https://github.com/CoDevBlocks/Knob#touchThreshold) is greater than 0.

## Samples

### Sample 1

![Sample 1](/media/sample_01.png)

```xml
<com.codevblocks.android.knob.Knob
    android:id="@+id/knob"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"

    app:knobSize="250dp"

    app:fillColor="@android:color/transparent"
    app:fillStartAngle="0"
    app:fillSweepAngle="360"

    app:trackStrokeColor="@android:color/transparent"
    app:trackStrokeWidth="1dp"
    app:trackStrokeCap="round"
    app:trackStrokeOffset="0dp"

    app:progressMode="step"
    app:maxProgress="100"
    app:progress="0"

    app:progressStrokeColor="#21C0E0"
    app:progressStrokeWidth="10dp"
    app:progressStrokeCap="round"
    app:progressStrokeOffset="16dp"

    app:progressStartAngle="270"
    app:progressSweepAngle="360"

    app:thumbDrawable="@drawable/drawable_knob_thumb"
    app:thumbOffset="-20dp"
    app:thumbAnchorX="50%"
    app:thumbAnchorY="0%"
    app:thumbRotation="true"

    app:stepCount="18"
    app:stepDrawable="@drawable/drawable_knob_step"
    app:stepOffset="16dp"
    app:stepAnchorX="50%"
    app:stepAnchorY="50%"

    app:touchThreshold="64dp"
    app:touchOffset="-16dp"

    app:debug="false"
    app:debug_drawBounds="false"
    app:debug_drawTrack="false"
    app:debug_drawTouchRadius="false"
    app:debug_drawTouchArea="false"

    android:background="@drawable/drawable_knob_background"
    android:elevation="5dp" />
```
`drawable_knob_background.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:gravity="center">
        <shape android:shape="oval">
            <size android:width="250dp" android:height="250dp" />
            <gradient android:startColor="@android:color/white" android:endColor="#D0D0D0" android:type="linear" android:angle="225" />
        </shape>
    </item>
    <item android:gravity="center">
        <shape android:shape="oval">
            <size android:width="230dp" android:height="230dp" />
            <gradient android:startColor="@android:color/white" android:endColor="#D0D0D0" android:type="linear" android:angle="45" />
        </shape>
    </item>
</layer-list>

```


`drawable_knob_step.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="oval">
    <size android:width="10dp" android:height="10dp" />
    <solid android:color="#21C0E0" />
</shape>

```

### Sample 2

![Sample 2](/media/sample_02.png)

```xml
<com.codevblocks.android.knob.Knob
    android:id="@+id/knob"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"

    app:knobSize="250dp"

    app:trackStrokeColor="#23979F"
    app:trackStrokeWidth="8dp"
    app:trackStrokeCap="round"
    app:trackStrokeOffset="14dp"

    app:progressMode="continuous"
    app:maxProgress="100"

    app:progressStrokeColor="#34FFFF"
    app:progressStrokeWidth="8dp"
    app:progressStrokeCap="round"
    app:progressStrokeOffset="14dp"

    app:progressStartAngle="135"
    app:progressSweepAngle="270"

    app:thumbDrawable="@drawable/drawable_knob_thumb"
    app:thumbOffset="-16dp"
    app:thumbAnchorX="50%"
    app:thumbAnchorY="0%"
    app:thumbRotation="false"

    app:stepCount="5"
    app:stepDrawable="@drawable/drawable_knob_step"
    app:stepOffset="22dp"
    app:stepAnchorX="50%"
    app:stepAnchorY="100%"

    app:touchThreshold="64dp"
    app:touchOffset="-16dp"

    android:background="@drawable/drawable_knob_background"
    android:elevation="5dp" />
```
`drawable_knob_background.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:gravity="center">
        <shape android:shape="oval">
            <size android:width="250dp" android:height="250dp" />
            <gradient android:startColor="#434756" android:endColor="#252936" android:type="linear" android:angle="270" />
        </shape>
    </item>
</layer-list>
```

`drawable_knob_thumb.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="oval">
    <size android:width="44dp" android:height="44dp" />
    <gradient android:startColor="#434756" android:endColor="#292e3c" android:type="linear" android:angle="90" />
</shape>
```

`drawable_knob_step.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <size android:width="2dp" android:height="24dp" />
    <solid android:color="#34FFFF" />
</shape>
```

### Sample 3

![Sample 3](/media/sample_03.png)

```xml
<com.codevblocks.android.knob.Knob
    android:id="@+id/knob"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"

    app:knobSize="250dp"

    app:trackStrokeColor="#9a234f"
    app:trackStrokeWidth="12dp"
    app:trackStrokeCap="round"
    app:trackStrokeOffset="24dp"

    app:progressMode="continuous"
    app:maxProgress="100"

    app:progressStrokeColor="#f31558"
    app:progressStrokeWidth="8dp"
    app:progressStrokeCap="round"
    app:progressStrokeOffset="24dp"

    app:progressStartAngle="180"
    app:progressSweepAngle="180"

    app:thumbDrawable="@drawable/drawable_knob_thumb"
    app:thumbOffset="-25dp"
    app:thumbRotation="false"

    app:touchThreshold="64dp"
    app:touchOffset="-16dp"

    android:background="@drawable/drawable_knob_background"
    android:elevation="5dp" />
```
`drawable_knob_background.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:gravity="center">
        <shape android:shape="oval">
            <size android:width="250dp" android:height="250dp" />
            <gradient android:startColor="@android:color/white" android:endColor="#D0D0D0" android:type="linear" android:angle="270" />
        </shape>
    </item>
    <item android:gravity="center">
        <shape android:shape="oval">
            <size android:width="150dp" android:height="150dp" />
            <gradient android:startColor="#EBEBEB" android:endColor="#E0E0E0" android:type="linear" android:angle="90" />
        </shape>
    </item>
    <item android:gravity="center">
        <shape android:shape="oval">
            <size android:width="148dp" android:height="148dp" />
            <gradient android:startColor="@android:color/white" android:endColor="#D0D0D0" android:type="radial" android:gradientRadius="146dp" />
        </shape>
    </item>
</layer-list>

```

`drawable_knob_thumb.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:gravity="center">
        <shape android:shape="oval">
            <size android:width="18dp" android:height="18dp" />
            <gradient android:startColor="#920d35" android:endColor="#f31558" android:type="linear" android:angle="270" />
        </shape>
    </item>
    <item android:gravity="center">
        <shape android:shape="oval">
            <size android:width="14dp" android:height="14dp" />
            <gradient android:startColor="#db134f" android:endColor="#f31558" android:type="radial" android:gradientRadius="7dp" />
        </shape>
    </item>
</layer-list>
```

### Sample 4

![Sample 4](/media/sample_04.png)

```xml
<com.codevblocks.android.knob.Knob
    android:id="@+id/knob"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"

    app:knobSize="250dp"

    app:trackStrokeColor="#FDE7DF"
    app:trackStrokeWidth="4dp"
    app:trackStrokeCap="round"

    app:progressMode="continuous"
    app:maxProgress="100"

    app:progressStrokeColor="#F3865F"
    app:progressStrokeWidth="4dp"
    app:progressStrokeCap="round"

    app:progressStartAngle="105"
    app:progressSweepAngle="330"

    app:thumbDrawable="@drawable/drawable_knob_thumb"

    app:touchThreshold="64dp" />
```

`drawable_knob_thumb.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="oval">
    <size android:width="32dp" android:height="32dp" />
    <solid android:color="@android:color/white" />
    <stroke android:color="#F3865F" android:width="2dp" />
</shape>
```