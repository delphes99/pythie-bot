import {UiSelectOption} from "@/common/components/common/form/select/UiSelectOption";
import {OutgoingEventType} from "@/features/outgoingevents/outgoing-event";
import {OutgoingEventCreateService} from "@/features/outgoingevents/outgoing-event.create.service";
import {InjectionKeys} from "@/injection.keys";
import {computed, inject, ref} from "vue";


export function useGetOutgoingEventTypes() {
    const backendUrl = inject(InjectionKeys.BACKEND_URL) as string
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