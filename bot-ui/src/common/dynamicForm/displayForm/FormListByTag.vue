<template>
  <fieldset class="border p-2">
    <legend v-if="props.title">{{ props.title }}</legend>
    <UiButton label="common.add"
              @click="openAddForm"
              :type="UiButtonType.Primary"/>
    <fieldset v-for="item in items" :key="item.id" class="border p-2">
      <legend>{{ item.type }}</legend>
      <div v-for="descriptor in item.descriptors" :key="descriptor.fieldName">
        <component :is="descriptor.viewComponent()"
                   :descriptor="descriptor"
        />
      </div>
      <UiButton label="common.delete"
                :type="UiButtonType.Warning"
                @click="deleteForm(item.id)"/>
    </fieldset>
  </fieldset>
  <UiModal title="common.add" v-model:is-open="isAddFormOpened">
    <UiSelect v-model="selectedType" :options="itemTypes" :label="tag"/>
    <UiButton
        label="common.add"
        @click="addForm"
        :type="UiButtonType.Primary"/>
  </UiModal>
</template>

<script setup lang="ts">
import {AppInjectionKeys} from "@/AppInjectionKeys";
import UiButton from "@/common/designSystem/button/UiButton.vue";
import UiButtonType from "@/common/designSystem/button/UiButtonType";
import UiSelect from "@/common/designSystem/form/select/UiSelect.vue";
import {UiSelectOption} from "@/common/designSystem/form/select/UiSelectOption";
import UiModal from "@/common/designSystem/modal/UiModal.vue";
import {useModal} from "@/common/designSystem/modal/useModal";
import DynamicFormService from "@/common/dynamicForm/DynamicFormService";
import {DynamicFormType} from "@/common/dynamicForm/DynamicFormType";
import {autowired} from "@/common/utils/Injection.util";
import {ref} from "vue";

interface Item {
  id: string;
  type: string;
  descriptors: any[];
}

const props = defineProps({
  tag: {
    type: String,
    required: true,
  },
  title: {
    type: String,
    default: null,
  }
})

const {isOpen: isAddFormOpened, open: openAddForm, close: closeAddForm} = useModal()
const selectedType = ref<DynamicFormType | null>(null)
const itemTypes = ref<UiSelectOption<DynamicFormType>[]>([])
const items = ref<Item[]>([])

const backendUrl = autowired(AppInjectionKeys.BACKEND_URL)
const dynamicFormService = new DynamicFormService(backendUrl)

dynamicFormService.getFormsByTag(props.tag).then(itemsType => {
  itemTypes.value = UiSelectOption.forString(itemsType)
})

async function addForm() {
  if (selectedType.value) {
    const description = await dynamicFormService.getForm(selectedType.value)
    items.value = [
      ...items.value,
      {
        id: crypto.randomUUID(),
        type: description.type,
        descriptors: description.fields
      }
    ]
    closeAddForm()
  }
}

function deleteForm(id: string) {
  items.value = items.value.filter(item => item.id !== id)
}
</script>