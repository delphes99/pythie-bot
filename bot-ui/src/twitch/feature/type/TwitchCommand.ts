import Feature from "@/twitch/feature/type/Feature";

export default interface TwitchCommand extends Feature {
  channel: string;
  trigger: string;
}
