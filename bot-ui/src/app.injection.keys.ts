import {NotificationService} from "@/common/components/common/notification/notification.service";
import FeatureService from "@/features/feature.service";
import MediasService from "@/media/MediasService";
import {MonitoringService} from "@/monitoring/monitoringService";
import {InjectionKey} from "vue";

export const AppInjectionKeys = {
    BACKEND_URL: Symbol() as InjectionKey<string>,
    MEDIA_SERVICE: Symbol() as InjectionKey<MediasService>,
    FEATURE_SERVICE: Symbol() as InjectionKey<FeatureService>,
    NOTIFICATION_SERVICE: Symbol() as InjectionKey<NotificationService>,
    MONITORING_SERVICE: Symbol() as InjectionKey<MonitoringService>,
}