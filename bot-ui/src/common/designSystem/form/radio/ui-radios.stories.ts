import {Options} from "@/common/designSystem/form/radio/Options";
import UiRadios from "@/common/designSystem/form/radio/ui-radios.vue";
import {Meta, StoryObj} from "@storybook/vue3";

const meta: Meta<typeof UiRadios> = {
    component: UiRadios,
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
}

export default meta
type Story = StoryObj<typeof UiRadios>;

export const Default: Story = {
    render: (args: any) => ({
        components: {UiRadios},
        setup() {
            return {args};
        },
        template: `
          <ui-radios name="name" :title="args.title" :model-value="args.value" :options="args.options"/>
        `,
    }),
};

export const Empty = {
    ...Default,
    args: {
        value: null,
        options: 'empty',
    }
}

export const WithData = {
    ...Default,
    args: {
        value: null,
        options: 'with data',
    }
}

export const WithValue = {
    ...Default,
    args: {
        value: 'option 2',
        options: 'with data',
    }
}

export const WithLabel = {
    ...Default,
    args: {
        value: 'option 2',
        options: 'with data',
        title: 'title'
    }
}