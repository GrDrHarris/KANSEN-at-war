# formula

In order to make things more customizable, instead of giving several preset classes and letting scripters setting values, we use formulas to describe how objects intereacts with others. Beacuse such formulas will probably be calculated very frequently, the formulas written in json files will actually dynamically compiled into java byte code. So what you need to write is the X in the following code:

```java
import java.lang.Math.* //import Math to make code shorter
//some irrelevant stuff
public double calc(double arg...)
{
    return X;
}
```

Here we warn the onwers of the servers: **no security checks are done, so make sure the scources of your scripts are trusted.**

To make these formulas easier to read, the args will be renamed. Never use ```arg[]``` directily, we don't guaranteen the order of how args are renamed won't change.

The rescritions formula will be discibled as follow:

- ```time``` [0,24) current time of day in hours
- ```wave``` [0,-) wave height in meters
- return [0,1] the success rate of ejecting a plane

Here the brackets means the same as that in math,'[' and ']' standsfor including while '(' and ')' stand for excluding. '-' stands for infinity.

The first two lines give outs the avaliable arguments, along with their bounds and meanings.The last line gives out what you should returned and its bound. If out of the bound, the behaviour of the framework is undifined.

A good example can be:

```java
    wave > 5 ? 0.01 : (sin(time / 12 * PI) + 1)/(2*max(wave - 1, 1))
```
