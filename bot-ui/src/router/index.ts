import {createRouter, createWebHashHistory, RouteRecordRaw} from "vue-router";
import Home from "../views/Home.vue";
import Discord from "../views/Discord.vue";
import Features from "@/views/Features.vue";
import Twitch from "@/views/Twitch.vue";
import TwitchConfiguration from "@/components/twitch/TwitchConfiguration.vue";
import TwitchReward from "@/components/twitch/TwitchReward.vue";
import TwitchChannelConfiguration from "@/components/twitch/TwitchChannelConfiguration.vue";
import TwitchFeature from "@/components/twitch/TwitchFeature.vue";

const routes: Array<RouteRecordRaw> = [
    {
        path: "/",
        name: "Home",
        component: Home
    },
    {
        path: "/features",
        name: "Features",
        component: Features
    },
    {
        path: "/about",
        name: "About",
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () =>
            import(/* webpackChunkName: "about" */ "../views/About.vue")
    },
    {
        path: "/discord",
        name: "Discord",
        component: Discord
    },
    {
        path: "/twitch",
        name: "Twitch",
        component: Twitch,
        children: [
            {
                path: "",
                component: TwitchConfiguration
            },
            {
                path: ":channelName",
                component: TwitchChannelConfiguration,
                props: true,
                children: [
                    {
                        path: "features",
                        component: TwitchFeature,
                        props: true
                    },
                    {
                        path: "rewards",
                        component: TwitchReward,
                        props: true
                    },
                    {
                        path: "",
                        component: TwitchReward,
                        props: true
                    },
                ]
            }
        ]
    }
];

const router = createRouter({
    history: createWebHashHistory(),
    routes
});

export default router;
