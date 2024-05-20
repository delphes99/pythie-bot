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
                component: () => import("@/connector/twitch/TwitchChannelConfiguration.vue"),
                props: true,
                children: [
                    {
                        path: "features",
                        name: "twitch_channel_features",
                        component: () => import("@/connector/twitch/feature/TwitchFeature.vue"),
                        props: true,
                    },
                    {
                        path: "rewards",
                        name: "twitch_channel_rewards",
                        component: () => import("@/connector/twitch/reward/TwitchReward.vue"),
                        props: true,
                    },
                    {
                        path: "",
                        name: "twitch_channel_rewards",
                        component: () => import("@/connector/twitch/reward/TwitchReward.vue"),
                        props: true,
                    },
                ],
            },
        ],
    }
]