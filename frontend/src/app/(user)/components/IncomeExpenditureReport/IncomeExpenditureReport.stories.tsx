import type { Meta, StoryObj } from '@storybook/react';

import IncomeExpenditureReport from './IncomeExpenditureReport';

const meta: Meta<typeof IncomeExpenditureReport> = {
    component: IncomeExpenditureReport,
    title: 'IncomeExpenditureReport',
    tags: ['autodocs'],
    argTypes: {}
};
export default meta;

type Story = StoryObj<typeof IncomeExpenditureReport>;

export const Default: Story = {
    args: {}
};
