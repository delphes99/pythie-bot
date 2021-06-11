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
- Feature: Group of event handler, possibly with a state, which provides a coherent behavior

### Roadmap
https://github.com/delphes99/pythie-bot/projects/1, feel free to create issues to propose new feature

## Configure your bot

### Configure

#### Static configuration
The project is modularized, you can load only what is needed.

The `LaunchBot.kt` file of the `pythie-bot` module is an example that launches all modules. The only configuration needed is the configuration of [Ngrok](https://ngrok.com/), which is mandatory for the majority of connectors because of the webhooks.

#### Build
run `mvn clean package`

#### Launch your bot
run `java -jar pythie-bot-1.0-SNAPSHOT-jar-with-dependencies.jar`

You can now connect to bot-ui to configure your bot : http://localhost:8080/admin

#### Kotlin Feature
For more advanced customization, check all the `pythie-bot` module, where all the custom features are configured (especially the `delphes99.kt` file). Any modification requires bot reboot.