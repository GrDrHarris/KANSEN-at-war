# weapon

Weapons for warships are very complex, to make things easier, we devided them into the following five types:

- Common Guns
- Torpedos
- Heavy Anti-air Guns
- Light Anti-air Guns
- Wing Guns (On fighter planes)

To make things easier, we don`t make booms on planes a special type, instead, we consider them as a type of **Common Gun** with very short range. The same thing happens on torpedo boomers. Remember, planes are considered as a ship with altitude, not a kind of weapon. More details are discussed in [PlaneEjecters](planeejecter.md)

Many formulas will be used in this document, if confused, see [Formula](formula.md)

In json files, weapon are discribed as follows:

```json
{
    "type" : "weapon",
    "sub_type" : "common",//one of "common", "torpedo", "heavyAA", "lightAA", "wing"
    "name" : "XXXX",
    //other stuff
}
```

## Common Gun

The followings are arguments that a common gun can have.

```json
{
    "parallel" : 3,
    /* how many guns are there in one turret, 1 by default.Integer
     */
    "reload" : 10.5,
    /* how fast these guns are reloaded, in seconds. how many
     * guns are actually running doesn't affect this value.Double
     */
    "endurance" : 900,
    /* after how many times of shooting this gun begin to lose accuracy and reliability, 
     * setting this value here is not required, you can set this value in discription
     * of ships.Integer
     */
    "reliability" : -1e-5,
    /* the possibility of not destorying a gun when firing. This value is probably very 
     * close to 1, so to make things easier, a negative value x can be given and
     * it functions as 1+x, here the actual rate is 1-1e-4. 1 by default. Double
     */
    "misfire" : 1e-4,
    /* the possibility of misfire, the sum of misfire rate and burst barrel rate should
     * not reach 1, this may happen after shooting too many times, but in such cases you
     * should consider setting less ammo count, lower reliability loss or provide
     * more maintance. 0 by default. Double
     */
    "reliability_loss" : 1e-6,
    "accuracy_loss" : 1e-4,
    /* the loss of reliability and accuracy per every shoot over endurance, don't set 
     * this value too large
     */
    "ammo" : [
        /* unlike in azurlane, we want a gun have multiple avaliable types of ammo, 
         * therefore we use a list.To make scripting easier, when loading scripts, if 
         * an argument isn't set, the value of former type is used. If a value 
         * doesn't have a default value, nor a succeeded value, an exception is thrown. 
         * Therefore, if you want to disable a type of ammo, override its number to zero, 
         * do not copy and delete.
         */
        {
            "type" : "AP",
            /* advised values are AP and HE. Some guns can be both AA guns and 
             * common guns, this can be done by adding a kind of ammo with 
             * type AA(see part Heavy Anti-air Guns).
             */
            "number" : 500,
            /* number of this type of ammo, Integer
             */
            "accuracy" : "0.1/max(wave, 1)*abs(dist-14000)/14000",
            /* a formula, return hit rate, advised value is around 0.03~0.05, 
             * larger guns have higher accuracy
             * wave [0,-), wave height in meters
             * dist [mindist,maxdist] , distance of target in meter
             */
            "nearmiss" : "0.1/max(wave, 1)*abs(dist-14000)/14000",
            /* a formula, near miss rate, sometime you don't need to hit directly 
             * to cause harm, this is espacilly true for boomers. "0" by default. 
             * The arguments are the same with accuracy.
             */
            "mindist" : 1000,
            "maxdist" : 23000,
            /* fire distance bounds, in meter, Integer
             */
            "harm_standard_deviation" : 0.01,
            /* the standard deviation of harm, Double
             */
            "harm" : "rand*500*max(0.2, -2.67e-5 *armor*armor + 1.4e-2 *armor - 0.233)",
            /* a formula, harm when such ammo hit armor thick armor
             * rand = clamp(N(1, harm_standard_deviation), 0, 2)
             * armor: thickness of the armor, in mm
             */
            "depth" : "rand*250*sin(angle)",
            /* a formula, show how deep of armor can the ammo go through, in mm.
             * rand = clamp(N(1, harm_standard_deviation), 0, 2)
             * angle: the angle of the ammo hits the armor, [0, PI/2], in radius
             */
            "missharm" : "rand*200*100/armor",
            /* how much harm a near miss can cause for ship body with thickness armor
             * arguments are the same as harm
             */
            "jump" : "cos(angle)",
            /* the possiblity of a ricochet when the ammo hits at angle
             * angle: the angle of the ammo hits the armor, [0, PI/2], in radius
             */
            "reliability" : 0.99,
            /* rate of the ammo can explode when it reach the target, the trick of 
             * using negative numbers is still avaliable.
             */
            "fire" : 0.05,
            /* rate of causing fire on target once hit
             */
            "speed" : 800
            /* speed of the ammo, we assume the time from fire to splash equals to
             * distiance / speed, in m/s
             */
        },{
            "type" : "HE",
            /* number, accuracy, nearmiss, mindist, maxdist, harm_standard_deviation 
             * are extended from AP
             */
            "harm" : "rand*500*max(0.2, 2.62e-6*armor*armor -3.32e-3*armor +1.61)",
            "depth" : "rand*80*sin(angle)",
            "missharm" : "rand*80*100/armor",
            "jump" : "0.1*cos(angle)",
            "fire" : 0.5
        }
    ],
    //"altitude":-1
    /* for boomers only, gives the minial altitude required for releasing the boom, 
     * in foot. For common guns, leave its default value -1.
     */
}
```

So, what happens when a gun fires?

- According to the reliability and misfire rate, wheather this ammo is shooted.

  - If it bursts barrel, the avaliable guns will minus 1 until next maintance.
  - If it misfires, an extra reload time is needed to remove the dead ammo from the gun, causing avaliable guns minus by 1 temporatily.
  - If too much fire has been done, some reliability and accuracy is lost.

- Then we judge wheather this ammo is a hit, near miss, or miss. Assume $t = distance/speed$ , the maneuvering factor of the target in $t$ seconds is calculated and multiplied to accuracy and nearmiss.

  - For a near miss, we then judge wheather the ammo explodes. If so, the miss harm is calculated and applied to the ship body.
  - For a miss, the progress ends here.

- Here comes the most complex part, hits. First, we judeg wheather it explodes.
  - If not, only minor harm is done to the armor (1%).
  - If so, we calculate the angle and thickness of the armor, determines wheather it is a ricochet.

    - If so, minor harm is done to the armor (10%).
    - If not, all of the harm is done to the armor.
      - The depth is then calculated. If $thickness \leq depth \leq 3\cdot thickness$, a critical hit is done and corresponding dealing functions are called. In such cases, usually the parts will be completely disabled.
      - In the mean time, we calculate the actual ratio of fire, by multiplying argument fire and the factor from the target. If the target does get lighted up, continuous damage will be applied to the armor, then the part, if the armor is destoried, the part will also be disabled. Until proper damage control measures are done, these two effects will continue.
