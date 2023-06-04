import {RouteRecordRaw} from "vue-router";

export const twitchRoutes: Array<RouteRecordRaw> = [
    {
        path: "/twitch",
        name: "twitch",
        children: [
            {
                path: "",
                component: () => import("@/connector/twitch/twitch-configuration.vue"),
            },
            {
                path: ":channelName",
                component: () => import("@/connector/twitch/twitch-channel-configuration.vue"),
                props: true,
                children: [
                    {
                        path: "features",
                        name: "twitch_channel_features",
                        component: () => import("@/connector/twitch/feature/twitch-feature.vue"),
                        props: true,
                    },
                    {
                        path: "rewards",
                        name: "twitch_channel_rewards",
                        component: () => import("@/connector/twitch/reward/twitch-reward.vue"),
                        props: true,
                    },
                    {
                        path: "",
                        name: "twitch_channel_rewards",
                        component: () => import("@/connector/twitch/reward/twitch-reward.vue"),
                        props: true,
                    },
                ],
            },
        ],
    }
]