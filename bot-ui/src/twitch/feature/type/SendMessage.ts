import OutgoingEvent from "@/twitch/feature/type/OutgoingEvent.js";

export interface SendMessage extends OutgoingEvent {
  channel: string;
  text: string;
}
