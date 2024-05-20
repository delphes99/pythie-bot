import {RouteRecordRaw} from "vue-router";

export const settingsRoutes: Array<RouteRecordRaw> = [
    {
        path: "/settings",
        name: "settings",
        component: () => import("@/settings/SettingsPage.vue"),
    }
]