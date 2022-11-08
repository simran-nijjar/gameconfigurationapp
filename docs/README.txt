Configurations Application
This is an application to store game configurations and game plays for saved games.
User can create games with expected min and max scores and add new games with combined scores for multiple players.
Users can view achievements for a given number of players, view history, edit and delete game configuration.
Game configurations and game plays are saved between launches of the application.
This app was developed by the Sodium Team.

The following are assumptions made after making clarifications about feature requirements with
Dr. Brian Fraser and design choices made by Sodium.

General:
Application mode is set to portrait mode only

Game Configurations:
In help page, buttons when clicked will display project and course information, app description,
hyperlink sources, and list of achievements
Scores can be whole numbers and negative numbers. Decimals are not allowed
Expected poor score is always less than the expected great score.
Game name allows special characters and names can be a single integer.
Displays alert dialog when user enters name longer than 100000000, expected min score,
or expected max score greater than 100000000 or less than -100000000 and erases user input
To view achievement levels for the number of players, users who cannot enter 0 will be displayed
with a message when done so and user input will be erased.
Scores are displayed when expected max - min < 8 because not all levels will be calculated for
or range may end up exceeding expected max in achievement levels.

Example: expected min = 1 and expected max = 3 for 1 player
Achievement Levels:
Beautiful Bananas Score : < 1
Wonderful Watermelons: [1]
Outstanding Oranges: [2]
Perfect Peaches: >=  3

Less than 8 levels are sometimes shown when the range (max - min/8) may exceed expected max
score when adjusting to 8 levels
A decision was made to do this to prevent achievement level ranges from containing decimals
which user cannot enter and displayed numbers greater than max before reaching Perfect Peaches level
Worst level is always Beautiful Bananas with less than expected min score and best level is always
Perfect Peaches >= expected max score


Game Plays:
When a user clicks a game from the game configuration list, they are taken to view configuration
where they have the buttons when clicked to add a new game, view achievement levels for number of
players inputted, edit, and delete game configuration. Users can see the history of a game when
there is at least one game played.
Combined scores can be whole numbers and negative numbers. Decimals are not allowed.
Displays alert dialog when user enters number of players greater than 100000000 and resets value
to 1 and erases combined score when it is greater than 100000000 or less than -100000000
A drop-down menu is added in add new game for convenience. This allows the user to change the game
configuration to add a new game without going back to the list of configurations. Users can still
access every game through the configuration list.
When a user enters a new game for a game selected from the drop-down menu and clicks save, the view
configuration page will change to display information about the game selected from the drop-down menu
instead of the game selected from list of game configurations.
However, if the user clicks the game from the drop-down menu and clicks the back button instead, the
view configuration page will display details for the game selected from list of game configurations
and NOT game selected from drop-down.






