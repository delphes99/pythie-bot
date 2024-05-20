import {AppInjectionKeys} from "@/AppInjectionKeys";
import {UiSelectOption} from "@/common/designSystem/form/select/UiSelectOption";
import {autowired} from "@/common/utils/Injection.util";
import {OutgoingEventType} from "@/features/outgoingevents/OutgoingEvent";
import {OutgoingEventCreateService} from "@/features/outgoingevents/OutgoingEventCreateService";
import {computed, ref} from "vue";

export function useGetOutgoingEventTypes() {
    const backendUrl = autowired(AppInjectionKeys.BACKEND_URL)
    const service = new OutgoingEventCreateService(backendUrl);

    function refreshAllTypes() {
        service.getAllTypes().then(types => allTypes.value = types);
    }

    const allTypes = ref<OutgoingEventType[]>([]);

    const allTypesAsSelectOptions = computed(() => {
        return UiSelectOption.forString(allTypes.value)
    })

    refreshAllTypes()

    return {
        refreshAllTypes,
        allTypes,
        allTypesAsSelectOptions
    }
}