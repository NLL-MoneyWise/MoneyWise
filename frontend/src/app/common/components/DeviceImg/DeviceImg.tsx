import Image from 'next/image';
import React from 'react';

const DeviceImg = () => {
    return (
        <div className="min-w-[350px]">
            <Image
                src={'/device.svg'}
                width={450}
                height={200}
                alt="사용자 화면"
                unoptimized={true}
            ></Image>
        </div>
    );
};

export default DeviceImg;
