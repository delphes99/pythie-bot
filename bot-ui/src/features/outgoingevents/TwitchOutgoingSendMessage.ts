import OutgoingEvent from "@/features/outgoingevents/OutgoingEvent"
import OutgoingEventType from "@/features/outgoingevents/OutgoingEventType"

import { v4 as uuid } from "uuid"

export default class TwitchOutgoingSendMessage implements OutgoingEvent {
  type = OutgoingEventType.TwitchOutgoingSendMessage
  id = uuid()
  text: string
  channel: string

  constructor(text: string, channel: string) {
    this.text = text
    this.channel = channel
  }
}
