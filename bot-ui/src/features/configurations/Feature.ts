import FeatureDescriptionType from "@/features/configurations/FeatureDescriptionType"
import FeatureType from "@/features/configurations/FeatureType"
import OutgoingEvent from "@/features/outgoingevents/OutgoingEvent"
import OutgoingEventType from "@/features/outgoingevents/OutgoingEventType"
import TwitchOutgoingSendMessage from "@/features/outgoingevents/twitch/twitch-outgoing-send-message/TwitchOutgoingSendMessage"

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

export type DescriptionItemType = string | OutgoingEvent[] | bigint | null

export class DescriptionItem {
  name: string
  value: DescriptionItemType
  type: FeatureDescriptionType

  constructor(
    name: string,
    type: FeatureDescriptionType,
    value: DescriptionItemType,
  ) {
    this.name = name
    this.value = value
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
  let value: DescriptionItemType

  switch (obj.type) {
    case FeatureDescriptionType.STRING:
      value = obj.value as string
      break
    case FeatureDescriptionType.OUTGOING_EVENTS:
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-ignore
      value = obj.value.map((outgoingEvent) => mapOutgoingEvent(outgoingEvent))
      break
    case FeatureDescriptionType.DURATION:
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-ignore
      value = obj.value && BigInt(obj.value.match(durationSecondRegex)[1])
      break
    default:
      throw new Error(`unknow description type : ${obj.type}`)
  }

  return new DescriptionItem(obj.name, obj.type, value)
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
function mapOutgoingEvent(obj: any): OutgoingEvent {
  switch (obj.type as OutgoingEventType) {
    //TODO generalize this
    case OutgoingEventType.TwitchOutgoingSendMessage:
      return new TwitchOutgoingSendMessage(obj.text, obj.channel)
  }
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export function fromJson(obj: any): Feature {
  const identifier = obj.items.filter((item: any) => item.name === "id")[0].value
  return new Feature(identifier, obj.type, obj.items.filter((item: any) => item.name !== "id").map(mapDescriptionItem))
}
