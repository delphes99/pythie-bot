import {lorem_ipsum} from "../../../../../.storybook/constant"
import UiModal from "./UiModal.vue"
import UiButton from "../button/UiButton.vue"
import {useModal} from "./useModal"

export default {
    title: "Design System/Modal",
    component: UiModal,
    argTypes: {},
}

const Template = (args) => ({
    components: {UiButton, UiModal},
    setup() {
        const {isOpen, open} = useModal()

        return {
            isOpen,
            open,
            args,
        }
    },
    template: `
      <ui-modal :title="args.title" v-model:isOpen="isOpen">
      {{ args.content }}
      </ui-modal>
      <ui-button label="Open Modal" @on-click="open"/>`,
})

export const Empty = Template.bind({})
Empty.args = {
    title: "Without content",
}

export const With_text = Template.bind({})
With_text.args = {
    title: "With text",
    content: lorem_ipsum,
}

export const With_scroll = Template.bind({})
With_scroll.args = {
    title: "With text",
    content: lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum,
}