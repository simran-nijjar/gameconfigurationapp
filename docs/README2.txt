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

It is README file for iteration 2; for iteration 1 refer to README1.

All previous game configurations from iteration 1 should be deleted otherwise iteration 2 of the application will not run with the old game configurations and old game plays

Game Plays:
When user clicks game from list of game history, user will be taken to a screen where they can edit the game
Here user can change game theme, difficulty, and scores for each player
User cannot change the number of players when editing
Default difficulty is Normal and difficulty does not persist
Default theme is Fruits and theme does persist
When user saves game, they will go back to game history

Achievement Improvement:
When user changes theme, background changes depending on theme selected in add new game screen, edit game screen, and view achievements screen
Rest of the application has the same purple background
Each time user saves the game, they will have an alert dialog for achievement theme
Each theme has their own image and own sound, the animation is the same for each theme
Max user input for players in view achievements has been set to 10000 which sends an alert dialog when user input is too high.


...