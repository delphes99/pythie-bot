import { UiSelectOption } from "@/ds/form/select/UiSelectOption"
import { TranslateFunction } from "@/lang/TranslateFunction"
import { computed, ref } from "vue"

export function useAddOutgoingEvent(backendUrl: string, t: TranslateFunction, sync = false) {

  const outgoingEventTypes = ref<string[] | null>(null)

  async function getOutgoingEventsTypes() {
    const response = await fetch(`${backendUrl}/feature/outgoingEventTypes`)

    response.json().then(types => outgoingEventTypes.value = types)
  }

  const outgoingEventTypeToAdd = ref<string | null>(null)

  const availableOutgoingEventsTypeForSelect = computed(() => {
    return UiSelectOption.for(
      outgoingEventTypes.value,
      (value: string) => t("configuration.outgoingEvents." + value),
    )
  })

  if(sync) {
    getOutgoingEventsTypes()
  }

  return {
    outgoingEventTypes,
    getOutgoingEventsTypes,
    outgoingEventTypeToAdd,
    availableOutgoingEventsTypeForSelect,
  }
}