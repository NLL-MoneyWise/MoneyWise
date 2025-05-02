'use client';
import Text from '@/app/common/components/Text/Text';
import ReactECharts from 'echarts-for-react';

const MyChart = () => {
    const option = {
        tooltip: {
            trigger: 'item'
        },
        legend: {
            bottom: '0%',
            left: 'center'
        },
        series: [
            {
                type: 'pie',
                data: [
                    { value: 1048, name: 'Search Engine' },
                    { value: 735, name: 'Direct' },
                    { value: 580, name: 'Email' },
                    { value: 484, name: 'Union Ads' },
                    { value: 300, name: 'Video Ads' }
                ]
            }
        ]
    };
    return <ReactECharts option={option} style={{ height: '400px' }} />;
};

export default function PieChart() {
    return (
        <div className="shadow-md rounded-md p-6">
            <Text>안녕</Text>
            <MyChart />
        </div>
    );
}
