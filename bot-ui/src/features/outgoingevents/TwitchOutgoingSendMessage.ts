import OutgoingEvent from "@/features/outgoingevents/OutgoingEvent";
import OutgoingEventType from "@/features/outgoingevents/OutgoingEventType";

export default class TwitchOutgoingSendMessage implements OutgoingEvent {
  type = OutgoingEventType.TwitchOutgoingSendMessage;
  text: string;
  channel: string;

  constructor(text: string, channel: string) {
    this.text = text;
    this.channel = channel;
  }
}
