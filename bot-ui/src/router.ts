import {createRouter, createWebHashHistory, RouteRecordRaw} from "vue-router"

const routes: Array<RouteRecordRaw> = [
    {
        path: "/",
        name: "Home",
        component: () => import("@/home/HomePage.vue"),
    },
    {
        path: "/features",
        name: "features",
        component: () => import("@/features/FeaturesList.vue"),
    },
    {
        path: "/feature",
        name: "feature",
        children: [
            {
                path: ":featureId",
                name: "featureDetail",
                props: true,
                component: () => import("@/features/details/FeatureDetail.vue"),
            }
        ]
    },
    {
        path: "/overlay",
        children: [
            {
                path: "",
                name: "overlays",
                component: () => import("@/overlay/OverlaysList.vue"),
            },
            {
                path: ":overlayId",
                name: "overlay_detail",
                component: () => import("@/overlay/editor/OverlayEditor.vue"),
                props: true,
            },
        ],
    },
    {
        path: "/settings",
        name: "settings",
        component: () => import("@/settings/SettingsPage.vue"),
    },
    {
        path: "/medias",
        name: "medias",
        component: () => import("@/media/MediasPage.vue"),
    },
    {
        path: "/about",
        name: "about",
        component: () => import("@/about/AboutPage.vue"),
    },
    {
        path: "/discord",
        name: "discord",
        component: () => import("@/discord/DiscordConfiguration.vue"),
    },
    {
        path: "/obs",
        name: "obs",
        component: () => import("@/obs/ObsConfiguration.vue"),
    },
    {
        path: "/twitch",
        name: "twitch",
        children: [
            {
                path: "",
                component: () => import("@/twitch/TwitchConfiguration.vue"),
            },
            {
                path: ":channelName",
                component: () => import("@/twitch/TwitchChannelConfiguration.vue"),
                props: true,
                children: [
                    {
                        path: "features",
                        name: "twitch_channel_features",
                        component: () => import("@/twitch/feature/TwitchFeature.vue"),
                        props: true,
                    },
                    {
                        path: "rewards",
                        name: "twitch_channel_rewards",
                        component: () => import("@/twitch/reward/TwitchReward.vue"),
                        props: true,
                    },
                    {
                        path: "",
                        name: "twitch_channel_rewards",
                        component: () => import("@/twitch/reward/TwitchReward.vue"),
                        props: true,
                    },
                ],
            },
        ],
    },
]

const router = createRouter({
    history: createWebHashHistory(),
    routes,
})

export default router
