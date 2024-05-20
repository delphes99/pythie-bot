import {Meta, StoryObj} from "@storybook/vue3";
import {lorem_ipsum} from "../../../../.storybook/constant"
import UiCard from "./UiCard.vue"

const meta: Meta<typeof UiCard> = {
    component: UiCard,
    title: "Design System/Card",
    argTypes: {
        title: String,
        content: String,
    },
    args: {
        title: "Card title",
        content: "Card content",
    },
    render: (args) => ({
        components: {UiCard},
        setup() {
            return {args};
        },
        template: `
          <UiCard :title="args.title">
            {{ args.content }}
          </UiCard>
        `,
    }),
}

export default meta
type Story = StoryObj<typeof UiCard>;

export const With_text = {
    args: {
        title: "With text",
        content: lorem_ipsum,
    }
}
