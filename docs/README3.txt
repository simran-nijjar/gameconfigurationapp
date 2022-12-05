Configurations Application
This is an application to store game configurations and game plays for saved games.
User can create games with expected min and max scores and add new games for multiple players with individual scores scores and set difficulty to that games.
New game may have one of the 3 themes of achievements.
Game Editing is now available. It allows to change individual scores, theme and difficulty of the game.
Users can view achievements for a given number of players with different game difficulty and theme, view history, edit and delete game configuration.
Game configurations and game plays are saved between launches of the application.
This app was developed by the Sodium Team.

The following are assumptions made after making clarifications about feature requirements with
Dr. Brian Fraser and design choices made by Sodium.

It is README file for iteration 3; for iteration 1 or 2 refer to README1 or README2.

All previous game configurations from iteration 1 & 2 should be deleted otherwise iteration 3 of the application will not run with the old game configurations and old game plays

Photos for Configurations and GamePlays:
Yo can open ViewImage activity from clicking on standard image in ViewConfiguration page.
It can also start from the AddNewGame activity after saving a game in Add new gamePlay and Edit gamePlay activities. In addition Edit gamePlay has a button to open ViewImage activity as well for a better user convenience.

Known issues (were covered with a TA and they said that current state is acceptable) on some emulators:
1. After accepting an image from the camera activity, activity does not stop but opens up again. To leave it, you need to make another photo and click on X button (do not accept photo). It will go back to the ViewImage activity with the last accepted photo.
2. If image is saved rotated, you can use rotation buttons to move an image 90 degrees right or left.
3. Sometimes when image is rotated, it is scaled down and it can be saved in that state. The image will be saved in that scaling and it can continue until image is extremely small.

Score Calculator Enhancement:
Covered all the requirements.

Configuration Statistics: