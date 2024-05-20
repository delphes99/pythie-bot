import {RouteRecordRaw} from "vue-router";

export const featureRoutes: Array<RouteRecordRaw> = [
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
    }
]