import {Options} from "@/common/designSystem/form/radio/Options";
import UiRadios from "@/common/designSystem/form/radio/UiRadios.vue";
import {Meta, StoryObj} from "@storybook/vue3";

const meta: Meta<typeof UiRadios> = {
    title: "Design System/Form/Radios",
    argTypes: {
        value: String,
        title: String,
        options: {
            options: ['empty', 'with data'],
            mapping: {
                'empty': Options.for([]),
                'with data': Options.for([
                    "option 1",
                    "option 2",
                    "option 3",
                ])
            }
        }
    },
    args: {
        value: null,
        options: 'empty',
    },
    render: (args: any) => ({
        components: {UiRadios},
        setup() {
            return {args};
        },
        template: `
          <UiRadios name="name" :title="args.title" :model-value="args.value" :options="args.options"/>
        `,
    }),
}

export default meta

type Story = StoryObj<typeof UiRadios>;

export const Empty = {
    args: {
        value: null,
        options: 'empty',
    }
}

export const WithData = {
    args: {
        value: null,
        options: 'with data',
    }
}

export const WithValue = {
    args: {
        value: 'option 2',
        options: 'with data',
    }
}

export const WithLabel = {
    args: {
        value: 'option 2',
        options: 'with data',
        title: 'title'
    }
}