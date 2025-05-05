'use client';
import ReactECharts from 'echarts-for-react';
import { ConsumptioneResponse } from '../../types/reponse/response-consumptione';

interface ConsumptioneChartProps {
    data: ConsumptioneResponse;
}

const formateData = (data: ConsumptioneResponse) => {
    return data.byCategory.map((item) => ({
        name: item.name,
        value: item.amount
    }));
};

const ConsumptioneChartChart = ({ data }: ConsumptioneChartProps) => {
    console.log(data);
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
                data: formateData(data),
                label: {
                    show: true,
                    formatter: '{b}: {c}원 ({d}%)'
                }
            }
        ]
    };

    return (
        <div className="text-center">
            <ReactECharts option={option} style={{ height: '300px' }} />
        </div>
    );
};

export default ConsumptioneChartChart;
