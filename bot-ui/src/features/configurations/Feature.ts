import FeatureType from "@/features/configurations/FeatureType"
import TwitchIncomingCommand from "@/features/configurations/TwitchIncomingCommand"
import OutgoingEvent from "@/features/outgoingevents/OutgoingEvent"
import OutgoingEventType from "@/features/outgoingevents/OutgoingEventType"
import TwitchOutgoingSendMessage from "@/features/outgoingevents/TwitchOutgoingSendMessage"

export default interface Feature {
  type: FeatureType
  identifier: string
  response: OutgoingEvent[]

  description(): DescriptionItem[]
}

export class DescriptionItem {
  key: string
  value: string

  constructor(key: string, value: string) {
    this.key = key
    this.value = value
  }
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
function mapOutgoingEvent(obj: any): OutgoingEvent {
  switch (obj.type as OutgoingEventType) {
    case OutgoingEventType.TwitchOutgoingSendMessage:
      return new TwitchOutgoingSendMessage(obj.text, obj.channel)
  }
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export function fromJson(obj: any): Feature {
  switch (obj.type as FeatureType) {
    case FeatureType.TwitchIncomingCommand:
      return new TwitchIncomingCommand(
        obj.identifier,
        obj.channel,
        obj.trigger,
        obj.response.map(mapOutgoingEvent),
      )
  }
}
