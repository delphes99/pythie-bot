import {NotificationService} from "@/common/designSystem/notification/NotificationService";
import DynamicFormService from "@/common/dynamicForm/DynamicFormService";
import FeatureService from "@/features/FeatureService";
import MediaService from "@/media/MediaService";
import {MonitoringService} from "@/monitoring/MonitoringService";
import {InjectionKey} from "vue";

export const AppInjectionKeys = {
    BACKEND_URL: Symbol() as InjectionKey<string>,
    MEDIA_SERVICE: Symbol() as InjectionKey<MediaService>,
    FEATURE_SERVICE: Symbol() as InjectionKey<FeatureService>,
    NOTIFICATION_SERVICE: Symbol() as InjectionKey<NotificationService>,
    MONITORING_SERVICE: Symbol() as InjectionKey<MonitoringService>,
    DYNAMIC_FORM_SERVICE: Symbol() as InjectionKey<DynamicFormService>,
}