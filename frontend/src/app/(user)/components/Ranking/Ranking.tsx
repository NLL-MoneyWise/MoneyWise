import Text from '@/app/common/components/Text/Text';
import React from 'react';
import { CategoryItem } from '../../types/reponse/response-consumptione';

interface RankingProps {
    title: string;
    data: CategoryItem[];
}

const getRankStyle = (rank: number) => {
    switch (rank) {
        case 0:
            return 'bg-amber-400';
        case 1:
            return 'bg-gray-300';
        case 2:
            return 'bg-amber-700';
        case 3:
            return 'bg-blue-500';
        default:
            return 'bg-gray-500';
    }
};

const Ranking = ({ title, data }: RankingProps) => {
    return (
        <div className="shadow-lg p-2 rounded-2xl bg-white flex flex-col h-full">
            <Text className="mb-2">{title}</Text>

            <div className="overflow-y-auto overflow-x-hidden flex-1 px-2 space-y-2 ">
                {data.map((val, idx) => (
                    <div
                        key={idx}
                        className="flex items-center p-3 shadow-md rounded-xl transition-all duration-300 ease-in-out hover:shadow-xl hover:scale-[1.02] hover:bg-gray-50 cursor-pointer "
                    >
                        {/* 순위  */}
                        <div
                            className={` w-12 h-6 rounded-full ${getRankStyle(idx)} text-white flex items-center justify-center font-bold `}
                        >
                            {idx + 1}
                        </div>

                        {/* 내용 */}
                        <div className=" flex-1 whitespace-nowrap text-ellipsis overflow-hidden md:visible ">
                            {val.name}
                        </div>

                        {/* 금액 */}
                        <div className=" invisible absolute font-medium sm:visible sm:relative">
                            {val.amount}원
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Ranking;
