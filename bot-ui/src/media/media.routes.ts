import {RouteRecordRaw} from "vue-router";

export const mediaRoutes: Array<RouteRecordRaw> = [
    {
        path: "/medias",
        name: "medias",
        component: () => import("@/media/medias-page.vue"),
    }
]