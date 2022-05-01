import OutgoingEvent from "@/features/outgoingevents/OutgoingEvent"
import OutgoingEventType from "@/features/outgoingevents/OutgoingEventType"
import TwitchOutgoingSendMessageView from "@/features/outgoingevents/twitch/twitch-outgoing-send-message/TwitchOutgoingSendMessageView.vue"

export function selectViewComponentFor(event: OutgoingEvent): any {
  switch (event.type) {
    case OutgoingEventType.TwitchOutgoingSendMessage:
      return TwitchOutgoingSendMessageView
    default:
      safeGuard(event.type)
  }
}

// eslint-disable-next-line @typescript-eslint/no-unused-vars
function safeGuard(_: never) {
  throw new Error("Failed to pattern match outgoing event")
}