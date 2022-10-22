import AboutPage from "@/about/AboutPage.vue"
import DiscordConfiguration from "@/discord/DiscordConfiguration.vue"
import FeaturesList from "@/features/FeaturesList.vue"
import HomePage from "@/home/HomePage.vue"
import MediasPage from "@/media/MediasPage.vue"
import ObsConfiguration from "@/obs/ObsConfiguration.vue"
import OverlayEditor from "@/overlay/editor/OverlayEditor.vue"
import OverlaysList from "@/overlay/OverlaysList.vue"
import OverlaysRouter from "@/overlay/OverlaysRouter.vue"
import SettingsPage from "@/settings/SettingsPage.vue"
import TwitchFeature from "@/twitch/feature/TwitchFeature.vue"
import TwitchReward from "@/twitch/reward/TwitchReward.vue"
import TwitchChannelConfiguration from "@/twitch/TwitchChannelConfiguration.vue"
import TwitchConfiguration from "@/twitch/TwitchConfiguration.vue"
import TwitchRouter from "@/twitch/TwitchRouter.vue"
import { createRouter, createWebHashHistory, RouteRecordRaw } from "vue-router"

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "Home",
    component: HomePage,
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
    path: "/settings",
    name: "Settings",
    component: SettingsPage,
  },
  {
    path: "/medias",
    name: "Medias",
    component: MediasPage,
  },
  {
    path: "/about",
    name: "About",
    component: AboutPage,
  },
  {
    path: "/discord",
    name: "Discord",
    component: DiscordConfiguration,
  },
  {
    path: "/obs",
    name: "Obs",
    component: ObsConfiguration,
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
