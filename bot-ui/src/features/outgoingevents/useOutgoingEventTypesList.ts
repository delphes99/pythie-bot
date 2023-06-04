import {AppInjectionKeys} from "@/app.injection.keys";
import {UiSelectOption} from "@/common/designSystem/form/select/UiSelectOption";
import {autowired} from "@/common/utils/injection.util";
import {OutgoingEventType} from "@/features/outgoingevents/outgoing-event";
import {OutgoingEventCreateService} from "@/features/outgoingevents/outgoing-event.create.service";
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