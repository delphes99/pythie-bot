<template>
  <fieldset class="border p-2">
    <ui-button label="common.add"
               @click="openAddEvent"
               :type="UiButtonType.Primary"/>
    <fieldset v-for="item in items" :key="item.id" class="border p-2">
      <legend>{{ item.type }}</legend>
      <div v-for="descriptor in item.descriptors" :key="descriptor.fieldName">
        <component :is="descriptor.viewComponent()"
                   :descriptor="descriptor"
        />
      </div>
      <ui-button label="common.delete"
                 :type="UiButtonType.Warning"/>
    </fieldset>
  </fieldset>
  <ui-modal title="common.add" v-model:is-open="isAddEventOpened">
    <ui-select v-model="selectedType" :options="itemTypes" label="feature.outgoingEvents">
    </ui-select>
    <ui-button
        label="common.add"
        @click="addEvent"
        :type="UiButtonType.Primary"/>
  </ui-modal>
</template>

<script setup lang="ts">
import {AppInjectionKeys} from "@/app.injection.keys";
import UiButtonType from "@/common/designSystem/button/ui-button.type";
import UiButton from "@/common/designSystem/button/ui-button.vue";
import {UiSelectOption} from "@/common/designSystem/form/select/ui-select.option";
import UiSelect from "@/common/designSystem/form/select/ui-select.vue";
import UiModal from "@/common/designSystem/modal/ui-modal.vue";
import {useModal} from "@/common/designSystem/modal/useModal";
import DynamicFormService from "@/common/dynamicForm/dynamic-form.service";
import {autowired} from "@/common/utils/injection.util";
import {ref} from "vue";
import {DynamicFormType} from "../dynamic-form-type";

interface Item {
  id: string;
  type: string;
  descriptors: any[];
}

const props = defineProps({
  tag: {
    type: String,
    required: true,
  }
})

const {isOpen: isAddEventOpened, open: openAddEvent, close: closeAddEvent} = useModal()
const selectedType = ref<DynamicFormType | null>(null)
const itemTypes = ref<UiSelectOption<DynamicFormType>[]>([])
const items = ref<Item[]>([])

const backendUrl = autowired(AppInjectionKeys.BACKEND_URL)
const dynamicFormService = new DynamicFormService(backendUrl)

dynamicFormService.getFormsByTag(props.tag).then(itemsType => {
  itemTypes.value = UiSelectOption.forString(itemsType)
})

async function addEvent() {
  if (selectedType.value) {
    const description = await dynamicFormService.getForm(selectedType.value)
    items.value = [
      ...items.value,
      {
        id: crypto.randomUUID(),
        type: description.type,
        descriptors: description.descriptors
      }
    ]
    closeAddEvent()
  }
}
</script>