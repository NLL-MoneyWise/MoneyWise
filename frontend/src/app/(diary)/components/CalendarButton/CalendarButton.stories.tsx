import type { Meta, StoryObj } from '@storybook/react';

import CalendarButton from './CalendarButton';

const meta: Meta<typeof CalendarButton> = {
    component: CalendarButton,
    title: 'atoms/CalendarButton',
    tags: ['autodocs'],
    argTypes: {}
};
export default meta;

type Story = StoryObj<typeof CalendarButton>;

export const Default: Story = {
    args: {}
};
