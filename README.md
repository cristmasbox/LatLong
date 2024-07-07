# LatLong
A simple Java datatype to store the Latitude and the Longitude. Distance calculations are included.


## Calculation of the distance between two points
### Formular
The formular for the calculation of the distance is from https://www.calculator.net/distance-calculator.html.

> [!NOTE]
> For this project I used the _Lambert's formula_ for distance calculations on an earth elipsoid.

This is the formula:<br>
<img src="https://d26tpo4cm8sb6k.cloudfront.net/img/distance/lambert-formula.png"/><br><br>
In code:
```
double d = a * (o - (f / 2) * (X + Y));
```
* `double d` is the distance to be calculated, __unit: meter__ <br>
* `a` is the _equatorial radius_ of the earth,   __unit: meter__ <br>
* `o` means __σ__ and stands for the central angle, _(we will calculate it later)_ __unit: radiand__ <br>
* `f` is the _flattening_ of the earth, _(we will calculate it later)_ <br>
* `X` and `Y`is calculated later.<br><br>

So to calculate the distance we have to get `a`, `o`, `f`, `X` and `Y`.
<br><br>

First we have to get `a`, the equatorial radius. You can get it by calling my class:
```
double a = LatLong.radiusEquatorial;
```

> [!TIP]
> Alternatively you can go to https://www.heret.de/mathe/erde.shtml and copy the "Äquatorradius", which is `6378137`m.

<br><br>

Next we have to calculate `o`. There is a formula for this on <a href="https://en.wikipedia.org/wiki/Great-circle_distance">wikipedia</a>:<br>
<img src="https://wikimedia.org/api/rest_v1/media/math/render/svg/a6fabdf1ec290545f531a6b9259fb46c9d3b0bdc"/><br>
* <img src="https://wikimedia.org/api/rest_v1/media/math/render/svg/01ba6a99cd97870e320dcdc1420f54703f6dbac1"/> is our `o`. __unit: radiand__ <br>
* <img src="https://wikimedia.org/api/rest_v1/media/math/render/svg/2691650573917bbe9b3d1c28ecfb49275110d16c"/> is the latitude of a given point. In our case is 
<img src="https://wikimedia.org/api/rest_v1/media/math/render/svg/2691650573917bbe9b3d1c28ecfb49275110d16c"/>1 the latitude of _Point A_ and
<img src="https://wikimedia.org/api/rest_v1/media/math/render/svg/2691650573917bbe9b3d1c28ecfb49275110d16c"/>2 the latitude of _Point B_. __unit: radiand__ <br>
* <img src="https://wikimedia.org/api/rest_v1/media/math/render/svg/e0d0df754ef5325a4811c91827c025a2304ad2c9"/> are the differences of the latitudes and longitudes of the two points. __unit: radiand__ <br>

In code it looks like that:
```
double deltaLongitude = Math.abs(longitudeA - longitudeB);
double o = Math.acos(Math.sin(latitudeA) * Math.sin(latitudeB) + Math.cos(latitudeA) * Math.cos(latitudeB) * Math.cos(deltaLongitude));
```
> [!CAUTION]
> Don't forget to convert the latitude and longitude values from _degree_ to _radiand_, because the `Math.acos()`, `Math.sin()`and `Math.cos()` need or return angles in radiands.
> ```
> latitudeA = Math.toRadians(latitudeA);
> latitudeB = Math.toRadians(latitudeB);
> longitudeA = Math.toRadians(longitudeA);
> longitudeB = Math.toRadians(longitudeB);
> ```
<br><br>

Now we calculate `f`. `f`is the _flattening_ of the earth and is calculated like that:<br>
<img src="https://wikimedia.org/api/rest_v1/media/math/render/svg/8caff55e7069e9a3421cac91fbdad7f2290a72c8"/><br>
In code:
```
f = (a - b) / a;
```

* `a` is still the equatorial radius of the earth  __unit: meter__ <br>
* `b` is the _polar radius_ of the earth __unit: meter__ <br>

<br><br>

After that we can calculate `X` and `Y`. The calculation of that is a little bit complex and we need some other values.
The formulas are:<br><br>
<img src="https://d26tpo4cm8sb6k.cloudfront.net/img/distance/x-y-value.png"/><br><br>

> [!NOTE]
> For those who don't know, __sin²()__ and __cos²()__ is a short writing for __(sin())²__ and __(cos())²__.<br>

In Code it looks like that:
```
double X = (o - Math.sin(o)) * ((Math.pow(Math.sin(P), 2) * Math.pow(Math.cos(Q), 2)) / (Math.pow(Math.cos(o / 2), 2)));
double Y = (o + Math.sin(o)) * ((Math.pow(Math.cos(P), 2) * Math.pow(Math.sin(Q), 2)) / (Math.pow(Math.sin(o / 2), 2)));
```

* `o` is the variable we calculated earlier  __unit: radiand__ <br>
* `P` and `Q` are Variables which have to be calculated.

So for using the formula we have to calculate `P` and `Q`. Here is the formula for that:<br><br>
P = (β1 + β2)/2<br>and<br>Q = (β2 - β1)/2<br><br>

Now we have to calculate β1 and β2. And as you may already know, there is a formular for this:<br><br>
tan(β) = (1 - f)tan(ϕ)<br><br>
That means that you have to calculate one β for each of the two points.

* `f` is the value which we calculated earlier,<br>
* `ϕ` is the latitude of the given point,<br>

When we have all values, we can calculate `X` and `Y`. Here is the Code for that:
```
betaA = Math.atan((1 - f) * Math.tan(latitudeA));
betaB = Math.atan((1 - f) * Math.tan(latitudeB));

P = (betaA + betaB) / 2;
Q = (betaB - betaA) / 2;

double X = (o - Math.sin(o)) * ((Math.pow(Math.sin(P), 2) * Math.pow(Math.cos(Q), 2)) / (Math.pow(Math.cos(o / 2), 2)));
double Y = (o + Math.sin(o)) * ((Math.pow(Math.cos(P), 2) * Math.pow(Math.sin(Q), 2)) / (Math.pow(Math.sin(o / 2), 2)));
```

> [!TIP]
> You can test the code with the [Distance Calculator](https://www.calculator.net/distance-calculator.html) (It uses the same Code).

### Usage of the distance calculation
There are different ways to use the distance calculation with the LatLong class integrated in your project:

* Use the static distance function of the class:
  ```
  LatLong.distance(latitudeFromPointA, longitudeFromPointA, latitudeFromPointB, longitudeFromPointB);
  ```
  ```
  LatLong PointA = new LatLong(latitudeFromPointA, longitudeFromPointA);
  LatLong PointB = new LatLong(latitudeFromPointB, longitudeFromPointB);
  LatLong.distance(PointA, PointB);
  ```

* Use the distance function from an LatLong object:
  ```
  LatLong PointA = new LatLong(latitudeFromPointA, longitudeFromPointA);
  LatLong PointB = new LatLong(latitudeFromPointB, longitudeFromPointB);
  PointA.distanceTo(PointB);
  ```

## General Usage

### Creating an LatLong object
There are three different ways to make an object of the LatLong class in your project:

1. ```
   LatLong location = new LatLong();
   ```
   _Creates an LatLong object with the cooridinates `0, 0`._<br><br>

2. ```
   LatLong location = new LatLong(latitude, longitude);
   ```
   _Creates an LatLong object with the given cooridinates._<br><br>

3. ```
   Location mylocation = getmylocation();
   LatLong location = new LatLong(mylocation);
   ```
   _In this example we have an (imaginary) function which returns an value of type Location. The constructor converts the Location into an LatLong datatype._<br><br>

### Getting or setting the values of an LatLong object
You can set the latitude and longitude values by calling the `setLatitude()` or the `setLongitude()` functions:

```
LatLong location = new LatLong();
location.setLatitude(latitude);
location.setLongitude(longitude);
```

You can get the latitude and longitude values by calling the `getLatitude()` or the `getLongitude()` functions:

```
LatLong location = new LatLong();
double latitude = location.getLatitude();
double longitude = location.getLongitude();
```

### Constants

#### radiusEquatorial
Returns the euatorial radius in meter:
```
double eqatorialRadius = LatLong.radiusEquatorial;
```

#### radiusPolar
Returns the polar radius in meter:
```
double polarRadius = LatLong.radiusPolar;
```

#### Other Constants
The other Constants only return Locations in LatLong Datatype. They are not very useful and you can delete them if you want. They are not used in the program.
