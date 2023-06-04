import {discordRoutes} from "@/connector/discord/discord.routes";
import {obsRoutes} from "@/connector/obs/obs.routes";
import {twitchRoutes} from "@/connector/twitch/twitch.routes";
import {RouteRecordRaw} from "vue-router";

export const connectorRoutes: Array<RouteRecordRaw> = [
    ...discordRoutes,
    ...obsRoutes,
    ...twitchRoutes,
]