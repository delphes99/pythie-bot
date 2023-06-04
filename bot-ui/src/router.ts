import {createRouter, createWebHashHistory, RouteRecordRaw} from "vue-router"

const routes: Array<RouteRecordRaw> = [
    {
        path: "/",
        name: "Home",
        component: () => import("@/home/home-page.vue"),
    },
    {
        path: "/monitoring",
        name: "Monitoring",
        component: () => import("@/monitoring/monitoring-page.vue"),
    },
    {
        path: "/features",
        name: "features",
        component: () => import("@/features/features-list.vue"),
    },
    {
        path: "/feature",
        name: "feature",
        children: [
            {
                path: ":featureId",
                name: "featureDetail",
                props: true,
                component: () => import("@/features/details/feature-detail.vue"),
            }
        ]
    },
    {
        path: "/overlay",
        children: [
            {
                path: "",
                name: "overlays",
                component: () => import("@/overlay/overlays-list.vue"),
            },
            {
                path: ":overlayId",
                name: "overlay_detail",
                component: () => import("@/overlay/editor/overlay-editor.vue"),
                props: true,
            },
        ],
    },
    {
        path: "/settings",
        name: "settings",
        component: () => import("@/settings/settings-page.vue"),
    },
    {
        path: "/medias",
        name: "medias",
        component: () => import("@/media/medias-page.vue"),
    },
    {
        path: "/about",
        name: "about",
        component: () => import("@/about/about-page.vue"),
    },
    {
        path: "/discord",
        name: "discord",
        component: () => import("@/connector/discord/discord-configuration.vue"),
    },
    {
        path: "/obs",
        name: "obs",
        component: () => import("@/connector/obs/obs-configuration.vue"),
    },
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
    },
]

const router = createRouter({
    history: createWebHashHistory(),
    routes,
})

export default router
