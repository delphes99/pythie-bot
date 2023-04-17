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
import FeatureDetail from "@/features/FeatureDetail.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "Home",
    component: HomePage,
  },
  {
    path: "/features",
    name: "features",
    component: FeaturesList,
  },
  {
    path: "/feature",
    name: "feature",
    children: [
        {
          path: ":featureId",
          name: "featureDetail",
          props: true,
          component: FeatureDetail,
        }
    ]
  },
  {
    path: "/overlay",
    component: OverlaysRouter,
    children: [
      {
        path: "",
        name: "overlays",
        component: OverlaysList,
      },
      {
        path: ":overlayId",
        name: "overlay_detail",
        component: OverlayEditor,
        props: true,
      },
    ],
  },
  {
    path: "/settings",
    name: "settings",
    component: SettingsPage,
  },
  {
    path: "/medias",
    name: "medias",
    component: MediasPage,
  },
  {
    path: "/about",
    name: "about",
    component: AboutPage,
  },
  {
    path: "/discord",
    name: "discord",
    component: DiscordConfiguration,
  },
  {
    path: "/obs",
    name: "obs",
    component: ObsConfiguration,
  },
  {
    path: "/twitch",
    component: TwitchRouter,
    children: [
      {
        path: "",
        name: "twitch",
        component: TwitchConfiguration,
      },
      {
        path: ":channelName",
        component: TwitchChannelConfiguration,
        props: true,
        children: [
          {
            path: "features",
            name: "twitch_channel_features",
            component: TwitchFeature,
            props: true,
          },
          {
            path: "rewards",
            name: "twitch_channel_rewards",
            component: TwitchReward,
            props: true,
          },
          {
            path: "",
            name: "twitch_channel_rewards",
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
