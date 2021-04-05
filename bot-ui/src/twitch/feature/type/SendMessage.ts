import OutgoingEvent from "@/twitch/feature/type/OutgoingEvent.ts";

export interface SendMessage extends OutgoingEvent {
  channel: String;
  text: String;
}
