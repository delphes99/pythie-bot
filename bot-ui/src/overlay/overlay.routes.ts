import {RouteRecordRaw} from "vue-router";

export const overlayRoutes: Array<RouteRecordRaw> = [
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
    }
]