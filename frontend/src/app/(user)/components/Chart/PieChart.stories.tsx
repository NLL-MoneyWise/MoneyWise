import type { Meta, StoryObj } from '@storybook/react';

import PieChart from './PieChart';

const meta: Meta<typeof PieChart> = {
    component: PieChart,
    title: 'atoms/PieChart',
    tags: ['autodocs'],
    argTypes: {}
};
export default meta;

type Story = StoryObj<typeof PieChart>;

export const Default: Story = {
    args: {}
};
