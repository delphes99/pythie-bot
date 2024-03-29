import {AppInjectionKeys} from "@/app.injection.keys";
import {UiSelectOption} from "@/common/designSystem/form/select/ui-select.option";
import {autowired} from "@/common/utils/injection.util";
import FeatureService, {FeatureType} from "@/features/feature.service";
import {computed, ref} from "vue";


export function useGetFeatureTypes() {
    const backendUrl = autowired(AppInjectionKeys.BACKEND_URL)
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