import type { Meta, StoryObj } from '@storybook/react';

import EventForm from './EventForm';

const meta: Meta<typeof EventForm> = {
    component: EventForm,
    title: 'molecules/EventForm',
    tags: ['autodocs'],
    argTypes: {}
};
export default meta;

type Story = StoryObj<typeof EventForm>;

export const Default: Story = {
    args: {}
};
