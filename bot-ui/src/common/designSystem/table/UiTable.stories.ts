import UiTable from "@/common/designSystem/table/UiTable.vue";
import UiTableColumn from "@/common/designSystem/table/UiTableColumn.vue";
import {Meta, StoryObj} from "@storybook/vue3";

const meta: Meta<typeof UiTable> = {
    component: UiTable,
    title: "Design System/Table",
    argTypes: {
        emptyMessage: String,
        data: {
            options: ['empty', 'with data'],
            mapping: {
                'empty': [],
                'with data': [
                    {name: 'name1', value: 'value1'},
                    {name: 'name2', value: 'value3'},
                    {name: 'name2', value: 'value3'},
                ]
            }
        }
    },
    args: {},
    render: (args: any) => ({
        components: {UiTable, UiTableColumn},
        setup() {
            return {args};
        },
        template: `
          <UiTable :data="args.data" :empty-message="args.emptyMessage">
            <UiTableColumn property-name="name" header-name="name"></UiTableColumn>
            <UiTableColumn property-name="value" header-name="value"></UiTableColumn>
          </UiTable>
        `,
    }),
}

export default meta
type Story = StoryObj<typeof UiTable>;

export const WithNoData = {
    args: {
        data: null
    }
}

export const WithEmptyData = {
    args: {
        data: 'empty'
    }
}

export const WithData = {
    args: {
        data: 'with data'
    }
}
