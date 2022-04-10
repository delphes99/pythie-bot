import FeatureDescriptionType from "@/features/configurations/FeatureDescriptionType"
import FeatureType from "@/features/configurations/FeatureType"
import OutgoingEvent from "@/features/outgoingevents/OutgoingEvent"
import OutgoingEventType from "@/features/outgoingevents/OutgoingEventType"
import TwitchOutgoingSendMessage from "@/features/outgoingevents/TwitchOutgoingSendMessage"

export default class Feature {
  identifier: string
  type: FeatureType
  descriptionItems: DescriptionItem[]

  constructor(
    identifier: string,
    type: FeatureType,
    descriptionItems: DescriptionItem[],
  ) {
    this.identifier = identifier
    this.type = type
    this.descriptionItems = descriptionItems
  }
}

export type DescriptionItemType = string | OutgoingEvent[] | bigint

export class DescriptionItem {
  name: string
  currentValue: DescriptionItemType
  type: FeatureDescriptionType

  constructor(
    name: string,
    type: FeatureDescriptionType,
    value: string | OutgoingEvent[],
  ) {
    this.name = name
    this.currentValue = value
    this.type = type
  }

  static ofString(key: string, value: string): DescriptionItem {
    return new this(key, FeatureDescriptionType.STRING, value)
  }

  static ofOutgoingEvents(key: string, value: OutgoingEvent[]): DescriptionItem {
    return new this(key, FeatureDescriptionType.OUTGOING_EVENTS, value)
  }
}

const durationSecondRegex = /PT(\d+)S/

// eslint-disable-next-line @typescript-eslint/no-explicit-any
function mapDescriptionItem(obj: any): DescriptionItem {
  let value: string | OutgoingEvent[]

  switch (obj.type) {
    case FeatureDescriptionType.STRING:
      value = obj.currentValue as string
      break
    case FeatureDescriptionType.OUTGOING_EVENTS:
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-ignore
      value = obj.currentValue.map((outgoingEvent) => mapOutgoingEvent(outgoingEvent))
      break
    case FeatureDescriptionType.DURATION:
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-ignore
      value = BigInt(obj.currentValue.match(durationSecondRegex)[1])
      break
    default:
      throw new Error(`unknow description type : ${obj.type}`)
  }

  return new DescriptionItem(obj.name, obj.type, value)
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
  return new Feature(obj.identifier, obj.type, obj.items.map(mapDescriptionItem))
}
