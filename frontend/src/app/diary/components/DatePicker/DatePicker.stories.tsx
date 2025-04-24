import type { Meta, StoryObj } from '@storybook/react';

import DatePicker from './DatePicker';

const meta: Meta<typeof DatePicker> = {
    component: DatePicker,
    title: 'DatePicker',
    tags: ['autodocs'],
    argTypes: {}
};
export default meta;

type Story = StoryObj<typeof DatePicker>;

export const Default: Story = {
    args: {}
};
