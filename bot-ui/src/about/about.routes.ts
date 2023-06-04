import {RouteRecordRaw} from "vue-router";

export const aboutRoutes: Array<RouteRecordRaw> = [
    {
        path: "/about",
        name: "about",
        component: () => import("@/about/about-page.vue"),
    }
]