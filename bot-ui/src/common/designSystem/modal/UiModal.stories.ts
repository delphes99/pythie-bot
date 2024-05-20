import UiToggle from "@/common/designSystem/form/toggle/UiToggle.vue";
import {Meta, StoryObj} from "@storybook/vue3";
import {lorem_ipsum} from "../../../../.storybook/constant"
import UiButton from "../button/UiButton.vue"
import UiModal from "./UiModal.vue"
import {useModal} from "./useModal"

const meta: Meta<typeof UiModal> = {
    component: UiModal,
    title: "Design System/Modal",
    render: (args) => ({
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
          <ui-button label="Open Modal" @on-click="open"/>
        `,
    }),
}

export default meta
type Story = StoryObj<typeof UiToggle>;

export const Empty = {
    args: {
        title: "Without content",
    }
}

export const With_text = {
    args: {
        title: "With text",
        content: lorem_ipsum,
    }
}

export const With_scroll = {
    args: {
        title: "With text",
        content: lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum + lorem_ipsum,
    }
}