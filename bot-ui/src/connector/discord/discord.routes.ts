import {RouteRecordRaw} from "vue-router";

export const discordRoutes: Array<RouteRecordRaw> = [
    {
        path: "/discord",
        name: "discord",
        component: () => import("@/connector/discord/discord-configuration.vue"),
    }
]