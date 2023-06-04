import {RouteRecordRaw} from "vue-router";

export const homeRoutes: Array<RouteRecordRaw> = [
    {
        path: "/",
        name: "Home",
        component: () => import("@/home/home-page.vue"),
    }
]