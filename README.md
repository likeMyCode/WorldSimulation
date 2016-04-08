# World Simulation

Application simulating simultaneously living objects in virtual world.

## Game Rules
 - Each character living in this world is individual thread. 
 - Each characters have to synchronize properly.
 - World has *n* different cities, and at least one road connecting two different cities.
 - Roads can meet, creating crossroads.
 - On each crossroad there can be only one citizen at the time.
 - We have to prevent deadlocks on crossroads.
 - Each villain attack closest city (capital city last one).
 - Game ends when the capital city is destroyed. 
 - We ask player for its name, and save best scores in *Best Score* table.

In this world, we can distinguish three different characters:
 - Citizens
 - Vilains
 - Superheros

### Citizens
- Citizens spawn at random time, but also can be created by player.
- Each citizen has to go from one city to another, and stay random amount of time before next trip.
- Citizen can randomly stop.
- Player can control citizens action (Stop, Delete, Go to another city).

### Villains
 - Villain spawn at random time, and goes to the closest city to destroy it.
 - Each villain has set of skills: *Health*, *Inteligence*, *Strength*, *Speed*, *Stamina*, *Energy* and *Fight Ability*. 
 - Once villain enters city, no citizen can leave or enter it. 
 - While sieging one city, villain kills each character one after one, and gains abilities from cities.
 - When villain meets citizen on the road, he kills citizen immediately.

### Superheros
- Superheros lives in the capital city.
- Superheros have same skills as villains.
- It can leave capital city on demand by player, only if at least one city is under attack.
- When superhero leaves capital city, he goes to the closest attacked city and try to defend.
- Also the fight between him and villains starts when he meet villain at the road.
- Fight between superhero and villain is round fight.
- First character to attack is one with higher speed (or random if equal).
- We calculate how much damage will each character take in single round to the enemy, and we decrease enemy's health.
- When superhero win the fight, and there is no more city sieged, superhero returns to the capital city.

Except characters there are also cities

### Cities
 - Each city can have some set of a skills (or none).
 - City skills are increasing in time.
 - When villain attack city, he drain's skills from it.
 

## Version 1.0
![alt tag](https://raw.githubusercontent.com/likeMyCode/WorldSimulation/master/screens/screen1.png)
![alt tag](https://raw.githubusercontent.com/likeMyCode/WorldSimulation/master/screens/screen2.png)
![alt tag](https://raw.githubusercontent.com/likeMyCode/WorldSimulation/master/screens/screen3.png)
