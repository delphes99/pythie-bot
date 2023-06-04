import {aboutRoutes} from "@/about/about.routes";
import {connectorRoutes} from "@/connector/connector.routes";
import {featureRoutes} from "@/features/feature.routes";
import {homeRoutes} from "@/home/home.routes";
import {mediaRoutes} from "@/media/media.routes";
import {monitoringRoutes} from "@/monitoring/monitoring.routes";
import {overlayRoutes} from "@/overlay/overlay.routes";
import {settingsRoutes} from "@/settings/settings.routes";
import {createRouter, createWebHashHistory, RouteRecordRaw} from "vue-router"

const routes: Array<RouteRecordRaw> = [
    ...aboutRoutes,
    ...connectorRoutes,
    ...featureRoutes,
    ...homeRoutes,
    ...mediaRoutes,
    ...monitoringRoutes,
    ...overlayRoutes,
    ...settingsRoutes,
]

const router = createRouter({
    history: createWebHashHistory(),
    routes,
})

export default router
