import {UiSelectOption} from "@/common/components/common/form/select/UiSelectOption";
import FeatureService, {FeatureType} from "@/features/feature.service";
import {InjectionKeys} from "@/injection.keys";
import {computed, inject, ref} from "vue";


export function useGetFeatureTypes() {
    const backendUrl = inject(InjectionKeys.BACKEND_URL) as string
    const service = new FeatureService(backendUrl);

    function refreshAllTypes() {
        service.getAllTypes().then(types => allTypes.value = types);
    }

    const allTypes = ref<FeatureType[]>([]);

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