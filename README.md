# Pythie-bot
>Streaming tool (bots, overlay...)

## What is Pythie-bot ?
Pythie was originally a twitch bot that was intended to offer viewers interactions with channel points. It evolves towards a tool allowing the streamer to regroup all the activities related to his stream (and more...)

### Philosophy
The project offers two approaches:
- an advanced mode that allows advanced customization and complex interactions, via kotlin code. This is the historical configuration mode.
- a UI mode (early stage), which allows a non-developer to customize the experience of his stream. It allows complex actions to be performed by composing several simple elements.

### Main features
- Chat bot (on twitch / discord)
- Twitch channel point interactions
- Cross connector interactions (i.e. : Twitch event trigger discord message)
- Overlay editor

### Core concepts
- Incoming event: Event received by the bot, the main source of trigger interaction 
- Outgoing event: Event emitted by the bot, in response of an incoming events
- Connector: Abstraction of an external platform, provides connection method and state, set of incoming and outgoing event 
- Event Handler: Transforms an incoming event into a list of outgoing events
- Feature: Group of event handler, possibly with a state, which provides a coherent behavior for the user ([more details](documentation/feature.md))
- Descriptor: Model for generating UI from backend object ([more details](documentation/descriptor.md))

### Roadmap
https://github.com/delphes99/pythie-bot/projects/1, feel free to create issues to propose new feature

## Configure your bot

### Configure

#### Static configuration
The project is modularized, you can load only what is needed.

The [LaunchBot.kt](pythie-bot/src/main/kotlin/fr/delphes/LaunchBot.kt) file that launches all modules. The only configuration needed is the [configuration](pythie-bot/src/main/resources/configuration-pythiebot.properties) of [Ngrok](https://ngrok.com/), which is mandatory for the majority of connectors because of the webhooks.

#### Build
```shell
./gradlew shadowJar
```
The jar is generated in [./pythie-bot/build/libs](pythie-bot/build/libs) with the name [pythie-bot.jar](pythie-bot/build/libs/pythie-bot.jar)
#### Launch your bot

```shell
java -jar ./pythie-bot/build/libs/pythie-bot.jar
```

You can now connect to bot-ui to configure your bot : http://localhost:8080/admin

#### Kotlin Feature
For more advanced customization, check all the [pythie-bot](pythie-bot) module, where all the custom features are configured (especially the [delphes99.kt](pythie-bot/src/main/kotlin/fr/delphes/configuration/channel/delphes99/delphes99.kt) file). Any modification requires bot reboot.