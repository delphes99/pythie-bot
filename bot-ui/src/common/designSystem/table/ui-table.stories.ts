import UiTableColumn from "@/common/designSystem/table/ui-table-column.vue";
import UiTable from "@/common/designSystem/table/ui-table.vue";
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
}

export default meta
type Story = StoryObj<typeof UiTable>;

export const Default: Story = {
    render: (args: any) => ({
        components: {UiTable, UiTableColumn},
        setup() {
            return {args};
        },
        template: `
          <ui-table :data="args.data" :empty-message="args.emptyMessage">
          <ui-table-column property-name="name" header-name="name"></ui-table-column>
          <ui-table-column property-name="value" header-name="value"></ui-table-column>
          </ui-table>
        `,
    }),
};

export const WithNoData = {
    ...Default,
    args: {
        data: null
    }
}

export const WithEmptyData = {
    ...Default,
    args: {
        data: 'empty'
    }
}

export const WithData = {
    ...Default,
    args: {
        data: 'with data'
    }
}
