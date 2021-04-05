import Feature from "@/twitch/feature/type/Feature";
import OutgoingEvent from "@/twitch/feature/type/OutgoingEvent";

export default interface TwitchCommand extends Feature {
  channel: string;
  trigger: string;
  responses: OutgoingEvent[];
}
