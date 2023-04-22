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
import FeatureDetail from "@/features/details/FeatureDetail.vue";

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
    component: ()=> import("@/about/AboutPage.vue"),
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
