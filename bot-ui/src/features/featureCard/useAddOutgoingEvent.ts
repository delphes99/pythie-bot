import { UiSelectOption } from "@/ds/form/select/UiSelectOption"
import { TranslateFunction } from "@/lang/TranslateFunction"
import { ref } from "vue"

export function useAddOutgoingEvent(backendUrl: string, t: TranslateFunction) {
  async function getOutgoingEventsTypes(): Promise<string[]> {
    const response = await fetch(`${backendUrl}/feature/outgoingEventTypes`)

    return response.json()
  }
  const outgoingEventTypeToAdd = ref<string | null>(null)

  const availableOutgoingEventsTypeForSelect = ref<UiSelectOption<string>[] | null>(
    null,
  )

  async function getAvailableOutgoingEventsTypeForSelect() {
    return await getOutgoingEventsTypes().then(
      (data) =>
        (availableOutgoingEventsTypeForSelect.value = UiSelectOption.for(
          data,
          (value: string) => t("configuration.outgoingEvents." + value),
        )),
    )
  }

  return {
    getOutgoingEventsTypes,
    outgoingEventTypeToAdd,
    availableOutgoingEventsTypeForSelect,
    getAvailableOutgoingEventsTypeForSelect,
  }
}