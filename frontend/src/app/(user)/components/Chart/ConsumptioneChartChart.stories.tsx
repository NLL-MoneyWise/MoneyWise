import type { Meta, StoryObj } from '@storybook/react';

import ConsumptioneChartChart from './ConsumptioneChartChart';

const meta: Meta<typeof ConsumptioneChartChart> = {
    component: ConsumptioneChartChart,
    title: 'ConsumptioneChartChart',
    tags: ['autodocs'],
    argTypes: {}
};
export default meta;

type Story = StoryObj<typeof ConsumptioneChartChart>;

export const Default: Story = {
    args: {}
};
