# Euklid
[![wakatime](https://wakatime.com/badge/user/3c933a7d-21c4-47f5-a3d7-60982c2b2e25/project/65d56cc2-97f2-43a6-830f-017d2464c381.svg?style=for-the-badge)](https://wakatime.com/badge/user/3c933a7d-21c4-47f5-a3d7-60982c2b2e25/project/65d56cc2-97f2-43a6-830f-017d2464c381)
![GitHub](https://img.shields.io/github/license/Lobby-Hoes/Euklid?style=for-the-badge)
![GitHub issues](https://img.shields.io/github/issues/Lobby-Hoes/Euklid?style=for-the-badge)
![GitHub pull requests](https://img.shields.io/github/issues-pr/Lobby-Hoes/Euklid?style=for-the-badge)
![GitHub commit activity](https://img.shields.io/github/commit-activity/m/Lobby-Hoes/Euklid?style=for-the-badge)
![Subreddit subscribers](https://img.shields.io/reddit/subreddit-subscribers/Hobbylos?label=Lobbyhoes%20in%20r%2Fhobbylos&style=for-the-badge)
----
🤖 A Discord-Bot for the german podcast "Hobbylos".

## Inviting the Bot
You may invite the Discord Bot by going to the following link: https://redcodes.dev/euklid/. 

## Using the Bot
The bot currently has 3 Slash-Commands:
- /info
  - Shows you Information about the bot.
- /mathefact <fact>
  - Gives you information about a math-fact that was mentioned in the podcast.
- /stadtgeschichte <story>
  - Gives you information about a town-story that was mentioned in the podcast.
  


## Running the Bot by your own
> We strongly recomend to use the public bot if possible, but you may build the bot on your own 

Euklid uses Maven. You can build the project by running:
```
mvn package
```
  
To start the bot you'll need to parse your bot-token as the first starter argument:
```
java -jar Euklid.jar <YOUR_TOKEN>
```
  
## Where can I find the data that the bot uses?
The Discord Bot uses data from the [Lobby-Hoes/hobbylos-data](https://github.com/Lobby-Hoes/hobbylos-data) repository.
