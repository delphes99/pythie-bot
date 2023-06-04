import {RouteRecordRaw} from "vue-router";

export const obsRoutes: Array<RouteRecordRaw> = [
    {
        path: "/obs",
        name: "obs",
        component: () => import("@/connector/obs/obs-configuration.vue"),
    }
]