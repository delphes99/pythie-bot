import {RouteRecordRaw} from "vue-router";

export const overlayRoutes: Array<RouteRecordRaw> = [
    {
        path: "/overlay",
        children: [
            {
                path: "",
                name: "overlays",
                component: () => import("@/overlay/overlays-list.vue"),
            },
            {
                path: ":overlayId",
                name: "overlay_detail",
                component: () => import("@/overlay/editor/overlay-editor.vue"),
                props: true,
            },
        ],
    }
]