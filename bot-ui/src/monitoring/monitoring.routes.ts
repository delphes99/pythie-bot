import {RouteRecordRaw} from "vue-router";

export const monitoringRoutes: Array<RouteRecordRaw> = [
    {
        path: "/monitoring",
        name: "Monitoring",
        component: () => import("@/monitoring/MonitoringPage.vue"),
    }
]