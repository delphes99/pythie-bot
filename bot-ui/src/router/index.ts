import {createRouter, createWebHashHistory, RouteRecordRaw} from "vue-router";
import Home from "../views/Home.vue";
import Discord from "../views/Discord.vue";
import Twitch from "@/views/Twitch.vue";
import TwitchConfiguration from "@/components/twitch/TwitchConfiguration.vue";
import TwitchReward from "@/components/twitch/TwitchReward.vue";
import TwitchChannelConfiguration from "@/components/twitch/TwitchChannelConfiguration.vue";

const routes: Array<RouteRecordRaw> = [
    {
        path: "/",
        name: "Home",
        component: Home
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
    },
    {
        path: "/about",
        name: "About",
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () =>
            import(/* webpackChunkName: "about" */ "../views/About.vue")
    }
];

const router = createRouter({
    history: createWebHashHistory(),
    routes
});

export default router;
