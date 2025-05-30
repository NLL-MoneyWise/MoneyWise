import type { Meta, StoryObj } from '@storybook/react';

import Calendar from './Calendar';

const meta: Meta<typeof Calendar> = {
    component: Calendar,
    title: 'templates/Calendar',
    tags: ['autodocs'],
    argTypes: {}
};
export default meta;

type Story = StoryObj<typeof Calendar>;

export const Default: Story = {
    args: {}
};
