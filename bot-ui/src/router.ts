import About from "@/about/About.vue"
import Obs from "@/obs/Obs.vue"
import TwitchChannelConfiguration from "@/twitch/TwitchChannelConfiguration.vue"
import TwitchConfiguration from "@/twitch/TwitchConfiguration.vue"
import TwitchFeature from "@/twitch/feature/TwitchFeature.vue"
import TwitchReward from "@/twitch/reward/TwitchReward.vue"
import Discord from "@/discord/Discord.vue"
import FeaturesList from "@/features/FeaturesList.vue"
import OverlaysRouter from "@/overlay/OverlaysRouter.vue"
import OverlaysList from "@/overlay/OverlaysList.vue"
import OverlayEditor from "@/overlay/editor/OverlayEditor.vue"
import Home from "@/home/GlobalHome.vue"
import TwitchRouter from "@/twitch/TwitchRouter.vue"
import Settings from "@/settings/Settings.vue"
import { createRouter, createWebHashHistory, RouteRecordRaw } from "vue-router"

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "Home",
    component: Home,
  },
  {
    path: "/features",
    name: "Features",
    component: FeaturesList,
  },
  {
    path: "/overlay",
    name: "Overlays",
    component: OverlaysRouter,
    children: [
      {
        path: "",
        component: OverlaysList,
      },
      {
        path: ":overlayId",
        component: OverlayEditor,
        props: true,
      },
    ],
  },
  {
    path: "/about",
    name: "About",
    component: About,
  },
  {
    path: "/settings",
    name: "Settings",
    component: Settings,
  },
  {
    path: "/discord",
    name: "Discord",
    component: Discord,
  },
  {
    path: "/obs",
    name: "Obs",
    component: Obs,
  },
  {
    path: "/twitch",
    name: "Twitch",
    component: TwitchRouter,
    children: [
      {
        path: "",
        component: TwitchConfiguration,
      },
      {
        path: ":channelName",
        component: TwitchChannelConfiguration,
        props: true,
        children: [
          {
            path: "features",
            component: TwitchFeature,
            props: true,
          },
          {
            path: "rewards",
            component: TwitchReward,
            props: true,
          },
          {
            path: "",
            component: TwitchReward,
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
