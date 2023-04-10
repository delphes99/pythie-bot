import FeatureDescriptionType from "@/features/configurations/FeatureDescriptionType"
import FeatureType from "@/features/configurations/FeatureType"
import OutgoingEvent from "@/features/outgoingevents/OutgoingEvent"
import OutgoingEventType from "@/features/outgoingevents/OutgoingEventType"
import TwitchOutgoingSendMessage from "@/features/outgoingevents/twitch/twitch-outgoing-send-message/TwitchOutgoingSendMessage"

export default class Feature {
  public id: string
  public type: FeatureType

  constructor(
    id: string,
    type: FeatureType,
  ) {
    this.id = id
    this.type = type
  }
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export function fromJson(item: any): Feature {
  const identifier = item.id
  return new Feature(identifier, item.type)
}
