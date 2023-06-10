<template>
  <Dialog :open="isOpen" @close="setIsOpen" :class="themeClass">

    <div class="fixed inset-0 bg-black/30" aria-hidden="true"/>
    <div class="fixed inset-0 flex items-center justify-center p-4">
      <DialogPanel class="w-full max-w-sm bg-backgroundColor">
        <ui-panel
            :title="title"
        >
          <template #withoutContainer>
            <div class="overflow-auto max-h-screen-4/5 p-4">
              <slot/>
            </div>
          </template>
        </ui-panel>
      </DialogPanel>
    </div>
  </Dialog>
</template>

<script lang="ts" setup>
import UiPanel from "@/common/designSystem/panel/ui-panel.vue"
import {useApplicationTheme} from "@/common/style/UseApplicationThemeStore";
import {Dialog, DialogPanel,} from '@headlessui/vue'

defineProps({
  isOpen: {
    type: Boolean,
    required: true,
  },
  title: {
    type: String,
    required: true,
  },
})

const emit = defineEmits(["update:isOpen"])

function setIsOpen(isOpen: boolean) {
  emit("update:isOpen", isOpen)
}

const {themeClass} = useApplicationTheme()
</script>