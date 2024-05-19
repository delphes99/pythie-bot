import {NotificationService} from "@/common/designSystem/notification/notification.service";
import DynamicFormService from "@/common/dynamicForm/dynamic-form.service";
import FeatureService from "@/features/feature.service";
import MediaService from "@/media/media.service";
import {MonitoringService} from "@/monitoring/monitoring.service";
import {InjectionKey} from "vue";

export const AppInjectionKeys = {
    BACKEND_URL: Symbol() as InjectionKey<string>,
    MEDIA_SERVICE: Symbol() as InjectionKey<MediaService>,
    FEATURE_SERVICE: Symbol() as InjectionKey<FeatureService>,
    NOTIFICATION_SERVICE: Symbol() as InjectionKey<NotificationService>,
    MONITORING_SERVICE: Symbol() as InjectionKey<MonitoringService>,
    DYNAMIC_FORM_SERVICE: Symbol() as InjectionKey<DynamicFormService>,
}