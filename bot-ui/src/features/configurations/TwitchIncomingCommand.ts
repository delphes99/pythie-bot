import Feature, { DescriptionItem } from "@/features/configurations/Feature"
import FeatureType from "@/features/configurations/FeatureType"
import OutgoingEvent from "@/features/outgoingevents/OutgoingEvent.js"

export default class TwitchIncomingCommand implements Feature {
  type = FeatureType.TwitchIncomingCommand
  identifier: string
  channel: string
  trigger: string
  response: OutgoingEvent[]

  constructor(
    identifier: string,
    channel: string,
    trigger: string,
    response: OutgoingEvent[],
  ) {
    this.identifier = identifier
    this.channel = channel
    this.trigger = trigger
    this.response = response
  }

  description(): DescriptionItem[] {
    return [
      new DescriptionItem("channel", this.channel),
      new DescriptionItem("trigger", this.trigger),
    ]
  }
}
