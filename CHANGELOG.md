v2.0.3: - Add "stop" as an alias to "exit"
		- Fix disabled module not getting deleted when loading a new version of the same module
		- Possibly fix some modules not loading

v2.0.2: - Add "restart" as an alias to reloading a module
		- Fix ConcurrentModificationException when loading modules

v2.0.1: - Fix only one module firing when multiple modules could have fired
		- Fix modules not being reloadable

v2.0:	- The bot now works based on modules. Each feature is a seperate .jar file which can get loaded at runtime
 		- Removed Upgrading
 		- Update Discord4J
 		- Remove an unused dependency
 		- Internal changes
 
v1.8.4:	- Added support for all characters to hangman, but still, only characters from the English alphabet can be guessed
 		- Added way of guessing the complete word (ignores all characters that are not a letter): -hangman guessingThisWord
 
v1.8.3:	- Fixed black jack not working
 		- Fixed players with a blackjack not getting tied with the dealer if he has a blackjack
 
v1.8.2:	- Added support for multiple words in hangman
 		- Increased rate limit retry delay
 		- Fixed black jack round not ending properly when all players have a blackjack
 		- Small black jack display changes
 
v1.8.1:	- Added restriction to hangman words so only letters are allowed
 		- Hangman messages now show already guessed letters
 		- Already guessed letters can no longer be guessed
 
v1.8:	- Added -hangman
 		- Added interface to allow waiting for DMs
 		- Messages get resent after being rate limited
 		- Potential fix for Black Jack soft locking
 
v1.7:	- Added -blackjack (-bj) command
 		- Removed CSGO update notifier as it depended on Maunz, who no longer has that feature
 		- Internal changes
 
v1.6:	- Added a ~~5%~~1% chance to add an automatic reaction to Raqbit's messages
 		- Added -prick to turn that on and off
v1.5:	- Added -osuacc (-oa) to calculate the accuracy with a given amount of 300s/100s/50s and misses
 
v1.4:	- Added notification when the SecurityCraft server is down
 
v1.3.2: - Added fallback to WolframAlpha query
 
v1.3.1:	- Potentially fixed disappearing playing text
 
v1.3:	- Added logging
 		- Backend rewrite
 		- Fixed -upgrades being usable in all channels
 		- Fixed rare case where upgrades would not count towards the total
 
v1.2.1:	- Updated to Discord4J 2.9
 
v1.2:	- Mavenized to fix the bot not mentioning and notifying after a CS:GO update
 		- Removed playing text timer
 		- Added program argument for development (-dev)
 		- Added error logging
 		- Added command to exit the bot
 
v1.1.1:	- Added 'funny' playing text
 
v1.1: 	- Added upgrade counting in #extruders
 
v1.0: 	- Initial release with CSGO update notifications and -calc for WolframAlpha calculations
 /