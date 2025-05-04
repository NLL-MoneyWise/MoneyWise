import type { Meta, StoryObj } from '@storybook/react';

import DateSelector from './DateSelector';

const meta: Meta<typeof DateSelector> = {
    component: DateSelector,
    title: 'DateSelector',
    tags: ['autodocs'],
    argTypes: {}
};
export default meta;

type Story = StoryObj<typeof DateSelector>;

export const Default: Story = {
    args: {}
};
