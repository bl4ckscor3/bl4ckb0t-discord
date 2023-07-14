v3.6.1:	- Update JDA to 5.0.0-beta.12
		- The info command now shows the JDA version and the most up to date GitHub link of the library

v3.6:	- Update JDA to 5.0.0-beta.8
		- Add support for guild specific slash commands

v3.5.2:	- Update JDA to 5.0.0-beta.1
		- Bot and Java versions for the info command are now retrieved via the jar manifest
		- Update jsoup to 1.15.3

v3.5.1:	- Update JDA to 5.0.0-alpha.17
		- Add Main#isDev to check whether the bot is running with the dev flag or not

v3.5:	- Update JDA to 5.0.0-alpha.1
			-> The bot now works in threads!

v3.4.5: - Update log4j to 2.17.1

v3.4.4: - Update log4j to 2.17.0

v3.4.3: - Update log4j to 2.16.0

v3.4.2: - Update log4j to 2.15.0

v3.4.1:	- Update commons-io
		- Update JDA
		- Update jsoup

v3.4:	- Update commons-io
		- Update to and compile using Java 16
		- Add class format version to info command (will be shown as a Java version in the embed)

v3.3.1:	- Fix modules not working if they would be triggered by a reply
		- Update JDA

v3.3:	- Add "-module list" to list all active modules

v3.2.1:	- Fix bot not working due to Discord API changes
		- Update JDA

v3.2:	- Add postConnect method to AbstractModule, which gets called after the bot has connected to Discord

v3.1.1:	- Update log4j-core

v3.1:	- Re-add additional info hash map to IRequestDM

v3.0:	- Major rewrite to switch from Discord4J to JDA

v2.1:   - Add Info module
		- Add "stop" as an alias to "exit"
		- Fix disabled module not getting deleted when loading a new version of the same module
		- Possibly fix some modules not loading new version when already enabled

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