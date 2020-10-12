Twitch bot written in Kotlin

#Configure your bot
Configure your bot with `Bot.build(...)`
 - configuration : all credential informations for the bot user
 - publicUrl : open url where your bot can be reach
 - channels : twitch channels with some features you want

See `Main.kt` for example

#Build and run
##Create your bot jar
run `mvn clean package`
##Launch your bot
run `java -jar pythie-bot-1.0-SNAPSHOT-jar-with-dependencies.jar`